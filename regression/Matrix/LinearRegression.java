package Matrix;

import org.apache.commons.math3.linear.*;

public class LinearRegression {

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
		return MatrixUtils.inverse(AtA).multiply(At).multiply(B).getData();
	}
}
