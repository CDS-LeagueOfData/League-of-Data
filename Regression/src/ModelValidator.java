import java.io.*;
public class ModelValidator {
	
	public static void main(String[] args) {
		/* String[] files = { 
				"david-clean-1.json", "david-clean-2.json", "david-clean-3.json", "david-clean-4.json",
				"david-clean-5.json", "david-clean-6.json", "david-clean-7.json", "david-clean-8.json",
				"david-clean-9.json", "sam-clean-1.json",   "sam-clean-2.json",   "sam-clean-3.json", 
				"sam-clean-4.json",   "wilson-clean-1.json","amber-clean-1.json", "amber-clean-2.json"};
				*/
		

		String[] params = { "kills", "deaths", "assists", "goldEarned", "minionsKilled" };
		
		File dir = new File("./data/clean/");
		if(dir.isDirectory()){
			
			// Get file names in ./data/clean/
			File[] files = dir.listFiles();
			String[] fileNames = new String[files.length];
			for(int i = 0; i < files.length; i++){
				fileNames[i] = files[i].getAbsolutePath();
			}
			

			for (int n = 2; n < files.length; n++) {
				double res = nFold(n, fileNames, params);
				System.out.println(n + "-fold validation: " + Math.round(res*100)/100.0);
			}
		} else {
			System.out.println("error: not a directory");
		}
		
	}

	public static double nFold(int n, String[] files, String[] params) {
		String[] testSet = new String[files.length / n];
		String[] trainingSet = new String[files.length - testSet.length];

		double sum = 0;
		// run n times total
		for (int iteration = 0; iteration < n; iteration++) {
			// distribute the files into test and training sets
			int testSetInd = 0;
			int trainingSetInd = 0;
			for (int i = 0; i < files.length; i++) {
				if ((i + iteration) % n == 0 && testSetInd<testSet.length) {
					testSet[testSetInd] = files[i];
					testSetInd++;
				} else {
					trainingSet[trainingSetInd] = files[i];
					trainingSetInd++;
				}
			}

			// run the regression
			ParseJson trainJSON = new ParseJson(trainingSet, params);
			double[][] values = trainJSON.getValues();
			double[] ratings = trainJSON.getRatings();

			// run the regression on trainingSet to get coefficients
			double[][] coefficients = LinearRegression.approximateRatingCoef(values, ratings);

			// test on testSet
			ParseJson testJSON = new ParseJson(testSet, params);
			double[][] testValues = testJSON.getValues();
			double[] realRatings = testJSON.getRatings();

			double[][] predictions = LinearRegression.predict(testValues, coefficients);
			for (int i = 0; i < testSet.length; i++) {
				sum += Math.abs(realRatings[i] - (int)predictions[i][0]);
			}
		}

		return sum/n;
	}
}
