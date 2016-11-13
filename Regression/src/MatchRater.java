import java.util.Scanner;

import com.google.gson.JsonObject;

public class MatchRater {
	
	@SuppressWarnings("resource")
	public void main(String[] args) {
		JsonObject js = MatchCleaner.getCleanData();
		Model m = ModelParser.parseModel();
		
	}
}
