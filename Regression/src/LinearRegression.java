

import org.apache.commons.math3.linear.*;

import com.google.gson.JsonObject;

public class LinearRegression {
	
	public static double[][] predict(double[][] values, double[][] coeff){

		RealMatrix A = new Array2DRowRealMatrix(values);
		RealMatrix x = new Array2DRowRealMatrix(coeff);
		return A.multiply(x).getData();
	}
	
	public static double[][] approximateRatingCoef(double[][] values, double[] rating) {
		RealMatrix A = new Array2DRowRealMatrix(values);
		RealMatrix B = A.createMatrix(A.getRowDimension(), 1);
		for (int i = 0; i < rating.length; i++) {
			B.addToEntry(i, 1, rating[i]);
		}
		return findCoef(A,B);
	}
	
	public static double[][] approximateRatingCoef(JsonObject game, double[] rating) {
		RealMatrix A = new Array2DRowRealMatrix(ParseJson.getValues(game));
		RealMatrix B = A.createMatrix(A.getRowDimension(), 1);
		for (int i = 0; i < rating.length; i++) {
			B.addToEntry(i, 1, rating[i]);
		}
		return findCoef(A,B);
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
		return MatrixUtils.inverse(AtA).multiply(At).multiply(B).getData();
	}
	
	public static void printMatrix(double[][] m){
		for(int i=0;i<m.length;i++){
			for(int j=0;j<m[0].length;i++){
				System.out.println(m[i][j]+" ");
			}
			System.out.println();
		}
	}
}
