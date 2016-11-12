
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class ModelOptimizer {
	
	static final double PENALTY = 10.0;
	static final double THRESHOLD = 0.7;
	
	static class Model{
		LinkedList<String> params;
		double score;
		public Model(LinkedList<String> p){
			params = p;
			score = calculateScore(params);
		}
		
		public static Model getUpdatedModel(Model m, String p){
			Model newM = new Model(m.params);
			newM.params.add(p);
			return newM;
		}
	}
	
	public static void main(String[] args){
	
		// get all params in String[]
		JsonObject stats = ParseJson.getStatsFromCleanJson("amber-clean-1.json");
		Set<Map.Entry<String,JsonElement>> entries = stats.entrySet();
		LinkedList<String> p = new LinkedList<String>();
		for (Map.Entry<String, JsonElement> entry : entries) {
			p.add(entry.getKey());
		}
		String[] allParams = new String[p.size()];
		allParams = p.toArray(allParams);
		for(String s : allParams) {
		    System.out.println(s);
		}
	}
	
	public static Model optimize(String[] p){
		// Create set of unused parameters
		LinkedList<String> available = new LinkedList<String>();
		for(String s: p)
			available.add(s);
		
		// initialize dummy model
		Model bestModel = new Model(new LinkedList<String>());
		
		boolean modelChanged = true;
		while(available.size() != 0 && modelChanged){
			modelChanged = false;
			
			// pick next parameter to include
			String bestP = null;
			Model bestUpdated = null;
			for(String param : available){
				// has to not show significant correlation
				if(passCorrelationCheck(param, bestModel)){
					Model testModel = Model.getUpdatedModel(bestModel, param);
					if(testModel.score < bestUpdated.score){
						bestP = param;
						bestUpdated = testModel;
					}					
				}
			}
			
			boolean status = available.remove(bestP);
			if(!status)
				System.out.println("Error removing parameter " + bestP);
			bestModel = bestUpdated;			
		}
		return bestModel;
	}
	
	//should use non-exhaustive 10-fold cross validation
	public static double calculateScore(LinkedList<String> params){
		if(params.size() == 0)
			return Double.MAX_VALUE;
		return 0.0;
	}

	public static boolean passCorrelationCheck(String param, Model model){
		return false;
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
