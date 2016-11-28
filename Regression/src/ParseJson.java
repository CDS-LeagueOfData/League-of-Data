import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;



public class ParseJson {
	private static String pathName;
	private static String[] inputVars;
	private static String[] gameFiles;
	
	/**
	 * Constructor: initialize file path name (String)
	 * @param pN
	 */
	public ParseJson(String pN) {
		pathName = pN;
	}

	/** Constructor: initialize file path name (String) and
	 * .input variables (String[])
	 * @param pN
	 * @param iVs
	 */
	public ParseJson(String pN, String[] iVs) {
		pathName= pN;
		inputVars= iVs;
	}

	/** Constructor: initializes String array to hold multiple games and
	 *  input variables (String[])
	 * @param iVs
	 */
	public ParseJson(String[] fileNames, String[] iVs) {
		gameFiles = fileNames;
		inputVars = iVs;
	}

	/** 
	 * @return path name
	 */
	public String getPathName() {
		return pathName;
	}

	/** Set path name 
	 * @param pN
	 */
	public void setPathName(String pN) {
		pathName = pN;
	}

	/** 
	 * @return input variables
	 */
	public String[] getInputVars() {
		return inputVars;
	}

	public void setInputVars(String[] iVs) {
		inputVars = iVs;
	}

	//*********** JUST ME TESTING CODE THIS WILL NOT BE IN FINAL CLASS FILE **********////
	public static void main(String[] args) {
		ParseJson pjtest = new ParseJson("/Users/samdickerman/Documents/CDS/League-of-Data/Regression/data/clean/doublelift-clean-1.json");
		int test = pjtest.giveMeIntData("totalCS");
		System.out.println("This is a test: " +test);
		//        try {
		//          String pathName = "/Users/samdickerman/Downloads/noTimeline.json";
		//          FileReader file = new FileReader(pathName);
		//          JsonReader jsonReader = new JsonReader(file);
		//          
		//          ArrayList<JsonObject> matchesArray = new ArrayList<JsonObject>();
		//          // Convert to a JSON object to print data
		//          JsonParser jp = new JsonParser(); //from gson
		//          JsonElement root = jp.parse(jsonReader);
		//          JsonArray matches = root.getAsJsonObject().getAsJsonArray("matches");
		//          for (int matchIndex = 0; matchIndex < matches.size(); matchIndex++) {
		//              matchesArray.add((JsonObject) matches.get(matchIndex));
		//              System.out.println(matchesArray.get(matchIndex));
		//          }
		String[] input = {"minionsKilled", "kills", "deaths", "assists"};
		ParseJson pj = new ParseJson("/Users/samdickerman/Downloads/matches1.json", input);
		ArrayList<JsonObject> matches = pj.getMatchesArrayFromJson();
		JsonObject game1 = getGame(matches, 0);
		JsonArray participants = game1.getAsJsonArray("participants");
		JsonObject p = (JsonObject) participants.get(0);
		//          JsonObject stats = (JsonObject) player.get("stats");
		//          JsonPrimitive kills = (JsonPrimitive) stats.get("kills");
		//          System.out.println(stats);
		//          System.out.println(kills);
		int total = getATotalStat(game1, "minionsKilled");
		int individual = getAStatFromPlayer(p, "minionsKilled");
		int p0 = getAStatFromGame(game1, 0, "minionsKilled");
		int p1 = getAStatFromGame(game1, 1, "minionsKilled");
		int p2 = getAStatFromGame(game1, 2, "minionsKilled");
		int p3 = getAStatFromGame(game1, 3, "minionsKilled");
		int p4 = getAStatFromGame(game1, 4, "minionsKilled");
		int p5 = getAStatFromGame(game1, 5, "minionsKilled");
		int p6 = getAStatFromGame(game1, 6, "minionsKilled");
		int p7 = getAStatFromGame(game1, 7, "minionsKilled");
		int p8 = getAStatFromGame(game1, 8, "minionsKilled");
		int p9 = getAStatFromGame(game1, 9, "minionsKilled");

		System.out.print("total minions killed: " + total + "\n player 1: " + p0 + "\n player 2: " + p1 + "\n player 3: " + p2  + "\n player 4: " + p3  + 
				"\n player 5: " + p4  + "\n player 6: " + p5  + "\n player 7: " + p6  + "\n player 8: " + p7  + "\n player 9: " + p8  + "\n player 10: " + p9 + "\n");

		ArrayList<Integer> statArray = getArrayOfStat(game1, "minionsKilled");
		System.out.println(Arrays.toString(statArray.toArray()));

		//String[] myStatArray = {"minionsKilled", "kills", "deaths", "assists"};
		double[][] myMatrix = getValues(game1);
		printMatrix(myMatrix);


		String[] inputClean = {"kills", "deaths", "assists", "goldEarned", "minionsKilled"};
		String dataPath = "/Users/samdickerman/Documents/CDS/League-of-Data/Regression/data/clean/";
		String[] fileNames = new String[] {dataPath + "sam-clean-1.json",dataPath + "sam-clean-2.json",dataPath + "sam-clean-3.json",dataPath + "sam-clean-4.json",};

		System.out.println("VALUES");
		ParseJson pjClean = new ParseJson(fileNames, inputClean);
		double [][] a =pjClean.getValues();
		printMatrix(a);

		System.out.println("RATINGS");
		for (int j = 0; j< gameFiles.length; j++) {
			System.out.println(pjClean.getRatings()[j]);

			// TESTING CLEANJSON METHODS
			String path = "/Users/Amber/Documents/Cornell/CDSLeague/League-of-Data/Regression/data/clean";
			String[] files = new String[] {path + "/amber-clean-1.json", path + "/amber-clean-2.json"};
			ParseJson parsey = new ParseJson(files, input);

			double[][] a1 = parsey.getValues();
			System.out.println("VALUES");
			printMatrix(a1);

			double[] b1 = parsey.getRatings();
			System.out.println("RATINGS");
			for (int k = 0; k < gameFiles.length; k++) {
				System.out.println(b1[k]);
			}
		}
	}

	/** 
	 * @return array list of matches from json file.
	 */
	public ArrayList<JsonObject> getMatchesArrayFromJson() {
		try {
			FileReader file = new FileReader(pathName);
			JsonReader jsonReader = new JsonReader(file);
			ArrayList<JsonObject> matchesArray = new ArrayList<JsonObject>();
			// Convert to a JSON object to print data
			JsonParser jp = new JsonParser(); //from gson
			JsonElement root = jp.parse(jsonReader);
			JsonArray matches = root.getAsJsonObject().getAsJsonArray("matches");
			// loop through matches array to get all 100 games in json
			for (int matchIndex = 0; matchIndex < matches.size(); matchIndex++) {
				matchesArray.add((JsonObject) matches.get(matchIndex));
			}
			file.close();
			jsonReader.close();
			return matchesArray;
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		} 
		return null;

	}


	/** 
	 * @param matchesArray
	 * @param index
	 * @return any game (JsonObject) in the matches array where index is 0-99. 
	 */
	public static JsonObject getGame(ArrayList<JsonObject> matchesArray, int index) {
		return matchesArray.get(index); 
	}

	/** 
	 * @param game
	 * @return the participants (JsonArray) for a given game.
	 */
	public static JsonArray getPartArray(JsonObject game) {
		return game.getAsJsonArray("participants"); 
	}

	/** 
	 * @param partArray
	 * @param index
	 * @return a player (JsonObject) in the participants array where index 0-9.
	 */
	public static JsonObject getPlayer(JsonArray partArray, int index) {
		return (JsonObject) partArray.get(index);
	}

	/** 
	 * @param p
	 * @return the stats (JsonObject) from a particular player.
	 */
	public static JsonObject getStatsFromPlayer(JsonObject p) {
		return (JsonObject) p.get("stats");
	}

	/** 
	 * @param p
	 * @param statStr
	 * @return an attribute (Int) from the stats object where p is a player and statStr 
	 * is a String in the stats object. (kills, deaths, assists, minionsKilled, goldEarned etc...) 
	 */
	public static int getAStatFromPlayer(JsonObject p, String statStr) {

		JsonObject stats = getStatsFromPlayer(p);
		return stats.get(statStr).getAsInt();
	}

	/** 
	 * @param game
	 * @param pIndex
	 * @param statStr
	 * @return an attribute (Int) from the stats object where pIndex 0-9 is a player in the game 
	 * and statStr is a String in the stats object. (kills, deaths, assists, minionsKilled, goldEarned etc...) 
	 */
	public static int getAStatFromGame(JsonObject game, int pIndex, String statStr) {

		JsonArray partArray = getPartArray(game);
		JsonObject p = getPlayer(partArray, pIndex);
		JsonObject stats = getStatsFromPlayer(p);
		return stats.get(statStr).getAsInt();
	}

	/** 
	 * @param game
	 * @param statStr
	 * @return total given attribute (Int) from the stats object where p is a player and statStr
	 * is a String in the Stats object. (kills, deaths, assists, minionsKilled, goldEarned etc...) 
	 */
	public static int getATotalStat(JsonObject game, String statStr){

		JsonArray partArray = getPartArray(game);
		int total = 0;
		int playerAmt = 0;
		for (int partIndex=0; partIndex < partArray.size(); partIndex++) {
			JsonObject p = getPlayer(partArray, partIndex);
			playerAmt = getAStatFromPlayer(p, statStr);
			total = total + playerAmt;
		}
		return total;
	}

	/** 
	 * @param game
	 * @param statStr
	 * @return an array list of a given Stat (String) for a game
	 */
	public static ArrayList<Integer> getArrayOfStat(JsonObject game, String statStr) {
		ArrayList<Integer> statArray = new ArrayList<Integer>();
		for (int i=0; i < 10; i++) {
			statArray.add(getAStatFromGame(game, i, statStr));
		}
		return statArray;

	}

	/** Return 2D Matrix where the rows are the stats while the columns are each players respective stat value */
	public static double[][] getValues(JsonObject game) {
		double[][] valueMatrix = new double[inputVars.length][10];
		for (int statIndex = 0; statIndex < inputVars.length; statIndex++) {
			for (int playerIndex = 0; playerIndex < 10; playerIndex++) {
				String stat = inputVars[statIndex]; 
				int statValue = getAStatFromGame(game, playerIndex, stat);
				valueMatrix[statIndex][playerIndex] = (double) statValue;
			}
		}
		return valueMatrix;
	}

	static void printMatrix(double[][] grid) {
		for(int r=0; r<grid.length; r++) {
			for(int c=0; c<grid[r].length; c++)
				System.out.print(grid[r][c] + " ");
			System.out.println();
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////// CLEAN GAME FUNCTIONS/////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////
	public static JsonObject getStatsFromCleanJson(String pN) {
		try {
			FileReader file = new FileReader(pN);
			JsonReader jsonReader = new JsonReader(file);
			// Convert to a JSON object to print data
			JsonParser jp = new JsonParser(); //from gson
			JsonElement root = jp.parse(jsonReader);
			JsonObject cleanGame = root.getAsJsonObject();
			file.close();
			jsonReader.close();
			return (JsonObject) cleanGame.getAsJsonObject("player").get("stats");
		}
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		} 
		return null;
	}
	public List<Double> getGameTime(){
		List<Double> list = new ArrayList<Double>();
		for (int gameIndex = 0; gameIndex < gameFiles.length; gameIndex++) {
			try {
				FileReader file = new FileReader(gameFiles[gameIndex]);
				JsonReader jsonReader = new JsonReader(file);
				// Convert to a JSON object to print data
				JsonParser jp = new JsonParser(); //from gson
				JsonElement root = jp.parse(jsonReader);
				JsonObject cleanGame = root.getAsJsonObject();
				list.add(cleanGame.get("matchDuration").getAsDouble());
				file.close();
				jsonReader.close();
			}
			catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			} 
		}	
		
		
		return list;
		
	}

	public double[][] getValues() {
		double[][] valueMatrix = new double[gameFiles.length][inputVars.length];
		for (int gameIndex = 0; gameIndex < gameFiles.length; gameIndex++) {
			JsonObject stat = getStatsFromCleanJson(gameFiles[gameIndex]);
			for (int varIndex = 0; varIndex < inputVars.length; varIndex++) {
				String var = inputVars[varIndex]; 
//				System.out.println("var:  "+var);
//				System.out.println("stat.get(var):   " +stat.get(var));
				int value = 0;
				boolean b;
				try{
					value = stat.get(var).getAsInt();
				} catch (ClassCastException e){
					b = stat.get(var).getAsBoolean();
					value = b ? 1 : 0;
				}
				/*
				if(var.equals("winner")){
					boolean boolVal = stat.get(var).getAsBoolean();
					if(boolVal){
						value = 1;
					}
					else{
						value = 0;
					}
				}
				else{
					value = stat.get(var).getAsDouble();
				}
				valueMatrix[gameIndex][varIndex] = value;
				*/
				valueMatrix[gameIndex][varIndex] = (double) value;
			}
		}
		return valueMatrix;
	}

	public static double convertToRating(String grade) {
		double rating = 0;
		String[] grades = new String[]{"D-", "D", "D+", "C-", "C", "C+", "B-", "B", "B+", "A-", "A", "A+", "S-", "S", "S+"};
		for (int k = 0; k < grades.length; k++) {
			if (grades[k].equals(grade)){
				rating = k + 1;
			}
		}
		return rating;
	}

	public double[] getRatings(){
		double[] ratings = new double[gameFiles.length];
		for (int k = 0; k < gameFiles.length; k++) {
			JsonObject stats = getStatsFromCleanJson(gameFiles[k]);
			String letter = stats.get("rating").getAsString();
			ratings[k] = convertToRating(letter);
		}
		return ratings;
	}
	
	/* Can not get boolean or String data such as, winner or rating */
	public int giveMeIntData(String statStr) {
		int data;
		boolean d;
		JsonObject statsObj = getStatsFromCleanJson(pathName);
		try{
			data = statsObj.get(statStr).getAsInt();
		} catch (Exception e){
			d = statsObj.get(statStr).getAsBoolean();
			data = d ? 1:0;
		}
		return data;
	}
}








