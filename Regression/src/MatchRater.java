import com.google.gson.JsonObject;

public class MatchRater {
	
	public void main(String[] args) {
		JsonObject js = MatchCleaner.getCleanData();
		Model m = ModelParser.parseModel();
		
	}
}
