import java.io.*;
import java.util.ArrayList;
import org.apache.commons.math3.util.CombinatoricsUtils;

public class ModelValidator {

	public static void main(String[] args) {

		String[] params = { "kills", "deaths", "assists", "goldEarned", "minionsKilled" };

		File dir = new File("./data/clean/");
		if (dir.isDirectory()) {
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

			System.out.println("Total data: " + files.length);
			for (int n = 2; n <= files.length; n++) {
				double res = nFold(n, fileNames, params);
				System.out.println(n + "-fold validation: " + Math.round(res * 100) / 100.0);
			}
			int n = 4;
			double res = leavePOut(n, fileNames, params);
			System.out.println("Leave " + n + " out: " + Math.round(res * 100) / 100.0);
		} else {
			System.out.println("error: not a directory");
		}

	}

	public static double leavePOut(int p, String[] files, String[] params) {
		double differences = 0;
		int n = files.length;

		// get the combination by index
		// e.g. 01 --> AB , 23 --> CD
		int testSetIndexes[] = new int[p];
		// position of current index
		//   if (r = 1)              r*
		//   index ==>        0   |   1   |   2
		//   element ==>      A   |   B   |   C
		int r = 0;
		int index = 0;
		while (r >= 0) {
			// possible indexes for 1st position "r=0" are "0,1,2" --> "A,B,C"
			// possible indexes for 2nd position "r=1" are "1,2,3" --> "B,C,D"
			// for r = 0 ==> index < (4+ (0 - 2)) = 2
			if (index <= (n + (r - p))) {
				testSetIndexes[r] = index;
				// if we are at the last position print and increase the index
				if (r == p - 1) {
					// do something with the combination e.g. add to list or
					// print
					// print(combination, elements);
					String[] testset = new String[p];
					String[] trainset = new String[n - p];
					int i1 = 0;
					int i2 = 0;
					for (int i = 0; i < files.length; i++) {
						if (i1 < testSetIndexes.length && i == testSetIndexes[i1]) {
							testset[i1] = files[i];
							i1++;
						} else {
							trainset[i2] = files[i];
							i2++;
						}
					}
					// now we have a test set and a train set
					// we now run regression which I copied
					// run the regression
					ParseJson trainJSON = new ParseJson(trainset, params);
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
					differences += (sum / testset.length);

					index++;
				} else {
					// select index for next position
					index = testSetIndexes[r] + 1;
					r++;
				}
			} else {
				r--;
				if (r > 0)
					index = testSetIndexes[r] + 1;
				else
					index = testSetIndexes[0] + 1;
			}
		}
		int numberofComb = (int) CombinatoricsUtils.binomialCoefficient(n, p);
		return differences / numberofComb;
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
		for (int i = 0; i < n; i++) {
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
			differences += (sum / testset.length);
		}

		return differences / (double) n;
	}
}
