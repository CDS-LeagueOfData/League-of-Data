


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
    


public class ParseJson {
    private static String pathName;
    private static String[] inputVars;
    
    /** Constructor: initialize file path name (String) and
     * .input variables (String[])
     * @param pN
     * @param iVs
     */
    public ParseJson(String pN, String[] iVs) {
        pathName= pN;
        inputVars= iVs;
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
            ParseJson pj = new ParseJson("/Users/samdickerman/Downloads/noTimeline.json", input);
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
    
    
}








