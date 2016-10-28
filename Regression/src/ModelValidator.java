import java.io.*;
import java.util.ArrayList;

public class ModelValidator {

	public static void main(String[] args) {

		String[] params = { "kills", "deaths", "assists", "goldEarned", "minionsKilled" };

		File dir = new File("./data/clean/");
		if (dir.isDirectory()) {
			// Get file names in ./data/clean/
			File[] files = dir.listFiles();
			String[] fileNames = new String[files.length];
			for (int i = 0; i < files.length; i++) {
				fileNames[i] = files[i].getAbsolutePath();
			}

			System.out.println("Total data: " + files.length);
			for (int n = 2; n <= files.length; n++) {
				double res = nFold(n, fileNames, params);
				System.out.println(n + "-fold validation: " + Math.round(res * 100) / 100.0);
			}
		} else {
			System.out.println("error: not a directory");
		}

	}

	/**
	 * Performs n-fold cross validation
	 * 
	 * @param n
	 * @param files
	 *            String array of filenames (absolute file paths)
	 * @param params
	 *            String array of parameters we are interested in.
	 * @return the mean squared error defined as 1/n * (sum of (actual -
	 *         prediction)^2)
	 */
	public static double nFold(int n, String[] files, String[] params) {
		double differences = 0;
		// distribute files into n samples
		ArrayList<ArrayList<String>> samples = new ArrayList<ArrayList<String>>();
		for(int i=0;i<n;i++){
			samples.add(new ArrayList<String>());
		}
		for (int i = 0; i < files.length; i++) {
			samples.get(i % n).add(files[i]);
		}
		//
		for (int i = 0; i < n; i++) {
			ArrayList<String> test = new ArrayList<String>();
			ArrayList<String> training = new ArrayList<String>();
			for (int j = 0; j < samples.size(); j++) {
				if (i == j)
					// makes subsample the test set
					test = samples.get(j);
				else {
					// puts every string in subsample into training
					for (int k = 0; k < samples.get(j).size(); k++) {
						training.add(samples.get(j).get(k));
					}
				}
			}

			String[] testset = test.toArray(new String[test.size()]);
			String[] trainingset = training.toArray(new String[training.size()]);

			// run the regression
			ParseJson trainJSON = new ParseJson(trainingset, params);
			double[][] values = trainJSON.getValues();
			double[] ratings = trainJSON.getRatings();

			// run the regression on trainingSet to get coefficients
			double[][] coefficients = LinearRegression.approximateRatingCoef(values, ratings);

			// test on testSet
			ParseJson testJSON = new ParseJson(testset, params);
			double[][] testValues = testJSON.getValues();
			double[] realRatings = testJSON.getRatings();

			double[][] predictions = LinearRegression.predict(testValues, coefficients);
			
			double sum = 0;
			for (int j = 0; j < testset.length; j++) {
				sum += Math.pow((realRatings[j] - predictions[j][0]), 2);
			}
			differences += sum/testset.length;
		}

		return differences / (double)n;
	}
}
