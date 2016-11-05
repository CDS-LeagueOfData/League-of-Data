import java.util.List;

public class ModelOptimizer {
	
	static final double PENALTY = 10.0;
	static final double THRESHOLD = 0.7;
	
	static class Model{
		String[] params;
		double score;		
	}
	
	public static void main(String[] args){
	
		//TODO: maybe move to a method if necessary
		String[] allParams; 
	}
	
	public static Model optimize(){
		return null;
	}
	
	
	public static double calculateScore(String[] params){
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
