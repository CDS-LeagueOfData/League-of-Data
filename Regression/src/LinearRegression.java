
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class LinearRegression {

	public static double[][] predict(double[][] values, double[][] coeff) {
		values = addOne(values);
		RealMatrix A = new Array2DRowRealMatrix(values);
		RealMatrix x = new Array2DRowRealMatrix(coeff);
		return A.multiply(x).getData();
	}

	public static double[][] approximateRatingCoef(double[][] values, double[] rating) {
		values = addOne(values);
		RealMatrix A = new Array2DRowRealMatrix(values);
		RealMatrix B = A.createMatrix(A.getRowDimension(), 1);
		for (int i = 0; i < rating.length; i++) {
			B.addToEntry(i, 0, rating[i]);
		}
		return findCoef(A, B);
	}

	/**
	 * Perform regression on an independent variable data and dependent variable
	 * data.
	 * 
	 * @param A
	 *            The independent variables representing the raw data of
	 *            performance.
	 * @param B
	 *            The dependent variables representing the ratings.
	 * @return The best-fit coefficient under this rating system.
	 */
	public static double[][] findCoef(RealMatrix A, RealMatrix B) {
		RealMatrix At = A.transpose();
		RealMatrix AtA = A.transpose().multiply(A);
		return (MatrixUtils.inverse(AtA).multiply(At).multiply(B)).getData();

	}

	public static void printMatrix(double[][] m) {
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				System.out.print(m[i][j] + " ");
			}
			System.out.println();
		}
	}

	private static double[][] addOne(double[][] values) {
		double[][] ret = new double[values.length][values[0].length +1];
		for (int i = 0; i < values.length; i++) {
			ret[i][0] = 1;
			for (int j = 0; j < values[i].length; j++) {
				ret[i][j + 1] = values[i][j];
			}
		}
		return ret;
	}
	
	public static void main(String[] args)
	{
		double[][] test = new double[2][2];
		test[0][0] = 10;
		test[0][1] = 14;
		test[1][0] = 20;
		test[1][1] = 21;
		
		double[] rat = {5.0, 10.0};
		printMatrix(approximateRatingCoef(test, rat));
	}
}
