
import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.*;
import org.junit.Test;

public class LinearRegressionTest {

	@Test
	public void test() {
		/** Testing identity matrix simple test **/
		double[][] matrixData = { { 1, 0 }, { 0, 1 } };
		RealMatrix A = MatrixUtils.createRealMatrix(matrixData);
		double[][] matrixData2 = { { 1 }, { 1 } };
		RealMatrix B = MatrixUtils.createRealMatrix(matrixData2);
		double[][] matrixData3 = LinearRegression.findCoef(A, B);
		RealMatrix D = MatrixUtils.createRealMatrix(matrixData3);

		double[][] matrixData4 = { { 1 }, { 1 } };
		RealMatrix E = MatrixUtils.createRealMatrix(matrixData4);
		assertEquals(D, E);

		/** Slightly more difficult matrix to solve **/

		double[][] matrixData5 = { { 1, 2 }, { 3, 4 } };
		RealMatrix F = MatrixUtils.createRealMatrix(matrixData5);
		double[][] matrixData6 = { { 1 }, { 2 } };
		RealMatrix G = MatrixUtils.createRealMatrix(matrixData6);
		double[][] matrixData7 = LinearRegression.findCoef(F, G);
		System.out.println(matrixData7);

		/**
		 * No solution matrix, Number of Rows in B do not match the number of
		 * rows in A
		 **/

//		double[][] matrixData9 = { { 1, 2 }, { 3, 4 } };
//		RealMatrix J = MatrixUtils.createRealMatrix(matrixData9);
//		double[][] matrixData10 = { { 1 }, { 2 }, { 3 } };
//		RealMatrix K = MatrixUtils.createRealMatrix(matrixData10);
//		double[][] matrixData11;
//		matrixData11 = LinearRegression.findCoef(J, K);
//		RealMatrix L = MatrixUtils.createRealMatrix(matrixData11);

	}

}
