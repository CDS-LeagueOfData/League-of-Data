public class LinearRegression {

	public LinearRegression() {
	}

	/**
	 * Performs matrix multiplication with the two matrix in parameter.
	 * 
	 * @param matrix1
	 *            The left matrix in the matrix multiplication.
	 * @param matrix2
	 *            The right matrix in the matrix multiplication.
	 * @return The resultant matrix of multiplication.
	 */
	public static double[][] multiply(double[][] matrix1, double[][] matrix2) {
		int c, d, k = 0;
		double sum = 0;
		double multiply[][] = new double[0][0];

		int m1rows = matrix1.length;
		int m1cols = matrix1[0].length;

		double first[][] = matrix1;

		int m2rows = matrix2.length;
		int m2cols = matrix2[0].length;

		if (m1cols != m2rows) {
			System.err.println("Matrices with entered orders can't be multiplied with each other.");
			return null;
		} else {
			double second[][] = matrix2;
			multiply = new double[m1rows][m2cols];

			for (c = 0; c < m1rows; c++) {
				for (d = 0; d < m2cols; d++) {
					for (k = 0; k < m2rows; k++) {
						sum = sum + first[c][k] * second[k][d];
					}
					multiply[c][d] = sum;
					sum = 0;
				}
			}
		}
		return multiply;
	}

	/**
	 * Prints a matrix
	 * 
	 * @param a
	 *            The matrix to be printed
	 */
	public static void printMatrix(double[][] a) {
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++)
				System.out.print(a[i][j] + "\t");
			System.out.print("\n");
		}
		System.out.println();
	}
}
