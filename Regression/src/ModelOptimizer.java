
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ModelOptimizer {

	static final double PENALTY = 0.0;
	static final double THRESHOLD = 0.9;
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
			Model newM = new Model(m.params);
			newM.params.add(p);
			return newM;
		}
	}

	public static void main(String[] args) {

		// get all params in String[]
		JsonObject stats = ParseJson.getStatsFromCleanJson("./data/clean/amber-clean-1.json");
		stats.remove("item0");
		stats.remove("item1");
		stats.remove("item2");
		stats.remove("item3");
		stats.remove("item4");
		stats.remove("item5");
		stats.remove("item6");
		Set<Map.Entry<String, JsonElement>> entries = stats.entrySet();
		LinkedList<String> p = new LinkedList<String>();
		for (Map.Entry<String, JsonElement> entry : entries) {
			if (!(entry.getValue().getAsJsonPrimitive().isBoolean()) && !(entry.getValue().getAsJsonPrimitive().isString())) {
					p.add(entry.getKey());
			}
		}
		
		
		allParams = p.toArray(new String[p.size()]);
		Arrays.sort(allParams);

		allFiles = getFilesFromDir();
		ParseJson parsey = new ParseJson(allFiles, allParams);
		allValues= parsey.getValues();
		
		Model opt = optimize(allParams);
		
		String[] params = new String[opt.params.size()];
	
		saveModel(getFilesFromDir(), opt.params.toArray(params));

	}

	public static Model optimize(String[] p) {
		// Create set of unused parameters
		LinkedList<String> available = new LinkedList<String>();
		for (String s : p)
			available.add(s);

		// initialize dummy model
		Model bestModel = new Model(new LinkedList<String>());

		boolean modelChanged = true;
		while (available.size() != 0 && modelChanged) {
			modelChanged = false;

			// pick next parameter to include
			String bestP = null;
			Model bestUpdated = bestModel;
			for (String param : available) {
				// has to not show significant correlation
				if (passCorrelationCheck(param, bestModel)) {
					Model testModel = Model.getUpdatedModel(bestModel, param);
					if (testModel.score < bestUpdated.score) {
						bestP = param;
						bestUpdated = testModel;
					}
				} else {
					//System.out.println("failed corr check");
				}
			}

			boolean status = available.remove(bestP);
			if (!status)
				System.out.println("Error removing parameter " + bestP);
			else
				bestModel = bestUpdated;
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
		double score = ModelValidator.nFold(10, allFiles, param) + PENALTY;
		System.out.println("SCORE: " + score);
		return score;
	}

	public static void saveModel(String[] fileNames, String[] params) {
		
		//Name the text file
		String saveFileName = "./model.text/";
		
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

		return fileNames;
	}

	public static boolean passCorrelationCheck(String param, Model model) {
		int ind = Arrays.binarySearch(allParams, param);
		double[] paramVs = allValues[ind];
		for(String p: model.params){
			ind = Arrays.binarySearch(allParams, p);
			double[] paramT = allValues[ind];
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
