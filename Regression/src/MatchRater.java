import com.google.gson.JsonObject;

public class MatchRater {

	public static void main(String[] args) {
		JsonObject data = MatchCleaner.getCleanData();
		Model m = ModelParser.parseModel();
		double score = 0;
		for (int i = 0; i < m.params.size(); i++) {
			score += data.get(m.params.get(i)).getAsInt() * m.coefficients[i];
		}
		System.out.println("Your rating for the game is " + score);
	}
}
