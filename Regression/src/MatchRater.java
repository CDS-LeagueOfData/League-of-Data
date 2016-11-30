import java.io.IOException;

import com.google.gson.JsonObject;

public class MatchRater {

	public static void main(String[] args) throws IOException {
		JsonObject data = MatchCleaner.getCleanData();
		Model m = ModelParser.parseModel();
		double score = m.coefficients[0]; //constant term
		for (int i = 1; i < m.params.length; i++) {
			System.out.println(m.params[i] + ": " + data.get(m.params[i]));
			score += data.get(m.params[i]).getAsInt() * m.coefficients[i];
		}
		System.out.println("Your rating for the game is " + score);
	}
}
