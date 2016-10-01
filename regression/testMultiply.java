import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class testMultiply {

	@Test
	public void testMultiply() {
		double[][] a = {{1,0},{0,1}};
		double[][] b = {{2,3},{3,2}};
		double [][] result = LinearRegression.multiply(a,b);
		for(int i=0;i<result.length;i++){
			for(int j=0;j<result[0].length;j++){
				assert(b[i][j]==result[i][j]);
			}
		}
		
		double[][] c = {{2,3,4},{1,2,5},{1,5,1}};
		double[][] result2 = LinearRegression.multiply(b, c);
		
		double[][] d = new double[3][4];
		double[][] e = new double[4][5];
		for(int i=0;i<3;i++){
			for(int j=0;j<4;j++){
				d[i][j] = (new Random()).nextFloat() * (100.0f - 0.0f) + 0.0f;
			}
		}
		for(int i=0;i<4;i++){
			for(int j=0;j<5;j++){
				e[i][j] = (new Random()).nextFloat() * (100.0f - 0.0f) + 0.0f;
			}
		}
		double[][] result3 = LinearRegression.multiply(d,e);
		LinearRegression.printMatrix(result3);
	}

}
