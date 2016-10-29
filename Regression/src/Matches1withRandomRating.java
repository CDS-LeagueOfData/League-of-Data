
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;

import org.junit.Test;

import com.google.gson.*;

public class Matches1withRandomRating {

	@Test
	public void test() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		Object obmatches1 = (new JsonParser()).parse(new FileReader(new File("matches1.json")));
		JsonObject mathces1 = (JsonObject) obmatches1;
		double[] ratings = new double[1000];
		for (int i = 0; i < 1000; i++) {
			ratings[i] = ((new Random()).nextDouble() * 5) + 5;
		}
		//LinearRegression.printMatrix(LinearRegression.approximateRatingCoef(mathces1, ratings));
	}

}
