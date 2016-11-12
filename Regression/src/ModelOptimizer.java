import java.util.LinkedList;


public class ModelOptimizer {
	
	static final double PENALTY = 10.0;
	static final double THRESHOLD = 0.7;
	
	static class Model{
		String[] params;
		double score;
		public Model(String[] p, double s){
			params = p;
			score = s;
		}
	}
	
	public static void main(String[] args){
	
		//TODO: maybe move to a method if necessary
		String[] allParams; 
	}
	
	public static Model optimize(String[] p){
		LinkedList<String> available = new LinkedList<String>();
		for(String s: p)
			available.add(s);
		
		Model bestModel = new Model(new String[0], Double.MAX_VALUE);
		boolean complete = false;
		while(!complete){
			// pick next parameter to include
			String testP = "";
			
			if(passCorrelationCheck(testP, bestModel)){
			}
			if(available.size() == 0){
				complete = true;
			}
		}
		return bestModel;
	}
	
	
	public static double calculateScore(String[] params){
		return 0.0;
	}

	public static boolean passCorrelationCheck(String param, Model model){
		return false;
	}
	
}
