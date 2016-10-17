
public class ModelValidator {
	
	public static void main(String[] args){
		String[] files = {"david-clean-1.json","david-clean-2.json","david-clean-3.json","david-clean-4.json",
		                  "david-clean-5.json","david-clean-6.json","david-clean-7.json","david-clean-8.json"};
		String[] params = {"kills","deaths","assists","goldEarned","minionsKilled"};
		
		for (int n = 2; n < files.length; n++){
			double res = nFold(n, files, params);
			System.out.println(n + "-fold validation: " + res);
		}
	}
	
	public static double nFold(int n, String[] files, String[] params){
		String[] testSet = new String[files.length/n];
		String[] trainingSet = new String[files.length - testSet.length];
		
		double sum = 0;
		// run n times total
		for(int iteration = 0; iteration < n; iteration ++) {
			// distribute the files into test and training sets
			int testSetInd = 0;
			int trainingSetInd = 0;
			for(int i = 0; i < files.length; i++){
				if( (i+iteration)%n == 0){
					testSet[testSetInd] = files[i];
					testSetInd++;
				} else {
					trainingSet[trainingSetInd] = files[i];
					trainingSetInd++;
				}
			}
			
			// run the regression
			ParseJson trainJSON = new ParseJson(trainingSet, params);
			double[][] values  = trainJSON.getValues();
			double[]   ratings = trainJSON.getRatings();
			
			// 	run the regression on trainingSet to get coefficients
			double[][] coefficients = LinearRegression.approximateRatingCoef(values, ratings);
			
			// test on testSet
			ParseJson testJSON = new ParseJson(testSet, params);
			double[][] testValues  = testJSON.getValues();
			double[]   realRatings = testJSON.getRatings();
			
			double[][] predictions = LinearRegression.predict(testValues,  coefficients);
			for(int i = 0; i < testSet.length; i++){
				sum += Math.abs( realRatings[i] - predictions[i][0] );
			}
		}
		
		return sum/n;
	}
}
