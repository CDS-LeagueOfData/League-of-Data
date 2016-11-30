import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ModelOptimizer {

	static final double PENALTY = 0.5;
	static final double THRESHOLD = 0.90;
	static String[]   allFiles;
	static String[]   allParams;
	static double[][] allValues;

	static class Model {
		LinkedList<String> params;
		double score;

		public Model(LinkedList<String> p) {
			params = p;
			score = calculateScore(params);
		}

		public static Model getUpdatedModel(Model m, String p) {
			LinkedList<String> newp = new LinkedList<String>(m.params);
			newp.add(p);
			return new Model(newp);
		}
	}

	public static void main(String[] args) throws IOException {

		// get all params in String[]
		JsonObject stats = ParseJson.getStatsFromCleanJson("./data/clean/amber-clean-1.json");
		//remove stats we don't think belong:
		stats.remove("item0");
		stats.remove("item1");
		stats.remove("item2");
		stats.remove("item3");
		stats.remove("item4");
		stats.remove("item5");
		stats.remove("item6");
		//retrieve all other params out from the file
		Set<Map.Entry<String, JsonElement>> entries = stats.entrySet();
		LinkedList<String> p = new LinkedList<String>();
		for (Map.Entry<String, JsonElement> entry : entries) {
			//only interested in taking out int values for now
			if (!(entry.getValue().getAsJsonPrimitive().isBoolean()) && 
				!(entry.getValue().getAsJsonPrimitive().isString() )) {
					p.add(entry.getKey());
			}
		}

		System.out.println("Penalty  : " + PENALTY);
		System.out.println("Threshold: " + THRESHOLD);
		
		//get all params into an array, allows for easier indexing
		allParams = p.toArray(new String[p.size()]);
		Arrays.sort(allParams);
		System.out.println("Number of parameters: " + allParams.length);

		//get all values from the files for our matrix
		allFiles = getFilesFromDir();
		ParseJson parsey = new ParseJson(allFiles, allParams);
		allValues= parsey.getValues();
		
		System.out.println(allValues.length +" by " + allValues[0].length);
		
		//write values to file for debugging
		PrintWriter out = new PrintWriter(new FileWriter(new File("values.txt")));
		for(String s : allParams)
			out.println(s);
		out.println();
		/*
		for(int r = 0; r < allValues.length; r++){
			for(int c = 0; c < allValues[r].length; c++){
				out.print(allValues[r][c] + "\t");
			}
			out.println();
		}*/
		
		System.out.println("Optimizing parameters...");
		Model opt = optimize(allParams);
		
		String[] params = new String[opt.params.size()];
	
		System.out.println("Saving to file...");
		System.out.println("Score of: " + opt.score);
		System.out.println("# of params used: " + opt.params.size());
		for(String s : opt.params)
			out.println(s);
		out.close();
		saveModel(getFilesFromDir(), opt.params.toArray(params));

	}

	public static Model optimize(String[] p) {
		Model actualBestModel = new Model(new LinkedList<String>());
		actualBestModel.score = Double.MAX_VALUE;
		for( String s : p){
			//System.out.println("NEW MODEL starting with " + s);
			Model m = optimizeOnParam(p, s);
			if (m.score < actualBestModel.score) {
				actualBestModel = m;
			}
		}		
		return actualBestModel;
	}
	
	public static Model optimizeOnParam(String[] p, String startParam){
		//initial model
		LinkedList<String> ps = new LinkedList<String>();
		ps.add(startParam);
		Model bestModel = new Model(ps);
		
		// Create set of unused parameters
		LinkedList<String> available = new LinkedList<String>();
		for (String s : p){
			available.add(s);
		}
		//remove starting param
		available.remove(startParam);
		
		//loop while we can and still want to add parameters
		boolean modelChanged = true;
		String paramToAdd;
		Model bestUpdated;
		while (available.size() >1 && modelChanged) {
			//reset values
			modelChanged = false;
			paramToAdd   = null;
			bestUpdated  = bestModel;
			
			// pick next parameter to include
			for (String param : available) {
				// has to not show significant correlation
				if (passCorrelationCheck(param, bestModel)) {
					Model testModel = Model.getUpdatedModel(bestModel, param);
					if (testModel.score < bestUpdated.score) {
						paramToAdd  = param;
						bestUpdated = testModel;
						System.out.println("bestUpdated now " + paramToAdd);
					}
				} 
			}
			if (available.remove(paramToAdd)) {
				bestModel = bestUpdated;
				modelChanged = true;
				System.out.println("  added " + paramToAdd);
			} else {
				//System.out.println("  tried to remove " + bestP);
			}
		}
		return bestModel;
	}

	/**
	 * Score(params) = V(param) + penalty * params.length
	 * 
	 * @param params
	 * @return
	 */
	public static double calculateScore(LinkedList<String> params) {
		String[] param = params.toArray(new String[params.size()]);
		double score;
		try {
			score = ModelValidator.nFold(10, allFiles, param);
			//System.out.println("SCORE:   " + score);
			//System.out.println("Penalty: " + param.length*PENALTY);
			score += param.length*PENALTY;
			
		} catch (Exception e){
			score = Integer.MAX_VALUE;
			//System.out.println("singular matrix");
		}
		return score;
	}

	public static void saveModel(String[] fileNames, String[] params) {
		
		//Name the text file
		String saveFileName = "./model.txt/";
		
		try {
			PrintWriter outputStream = new PrintWriter(saveFileName);
			
			// Get the values and the ratings from the given file names and parameters
			ParseJson opJSON = new ParseJson(fileNames, params);
			double[][] values = opJSON.getValues();
			double[] ratings = opJSON.getRatings();
	
			// Run the regression on optimizedSet to get coefficients
			double[][] coefficients = LinearRegression.approximateRatingCoef(values, ratings);
			
			for(int i = 0; i <params.length; i++){				
				//Print to text file
				outputStream.println(params[i]+" : "+coefficients[i][0]);
				
				//Print to console
				System.out.println(params[i]+" : "+coefficients[i][0]);
			}
			
			//Close text file and create
			outputStream.close();
			System.out.println("text file created in Regression directory");
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
	}

	public static String[] getFilesFromDir() {
		File dir = new File("./data/clean/");
		// Get file names in ./data/clean/
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".json");
			}
		});
		String[] fileNames = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			if (!files[i].isHidden())
				fileNames[i] = files[i].getAbsolutePath();
		}
		System.out.println(files.length + " files");

		return fileNames;
	}

	public static boolean passCorrelationCheck(String param, Model model) {
		int ind = Arrays.binarySearch(allParams, param);
		double[] paramVs = new double[allValues[0].length];
		for(int r = 0; r < allValues[0].length; r++)
			paramVs[r] = allValues[r][ind];
		for(String p: model.params){
			ind = Arrays.binarySearch(allParams, p);
			double[] paramT = new double[allValues[0].length];
			for(int r = 0; r < allValues[0].length; r++)
				paramT[r] = allValues[r][ind];
			
			if(Math.abs(calcCorrelation(paramVs, paramT)) >= THRESHOLD)
				return false;
		}
		return true;
	}

	public static double getMean(double[] a) {
		double temp = 0;
		for (double b : a) {
			temp += b;
		}
		return temp / a.length;
	}

	public static double stdDev(double[] a) {
		double sumSq = 0;
		double mean = getMean(a);
		for (double b : a) {
			sumSq += Math.pow(b - mean, 2);
		}
		return Math.sqrt(sumSq / (a.length - 1));
	}

	public static double calcCorrelation(double[] a, double[] b) {
		double sigma = 0;
		double aMean = getMean(a);
		double bMean = getMean(b);
		double aSTD = stdDev(a);
		double bSTD = stdDev(b);
		for (int i = 0; i < a.length; i++) {
			sigma += (a[i] - aMean) * (b[i] - bMean);
		}
		sigma = sigma / (aSTD * bSTD * (a.length - 1));

		return sigma;

	}

}