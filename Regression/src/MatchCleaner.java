import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.dto.Match.MatchDetail;
import net.rithms.riot.dto.Match.Participant;
import net.rithms.riot.dto.Match.ParticipantIdentity;
import net.rithms.riot.dto.Match.ParticipantStats;
import net.rithms.riot.dto.Match.ParticipantTimeline;
import net.rithms.riot.dto.Match.Player;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MatchCleaner {
	// change to own summoner key:
	private static String apiKey = "RGAPI-7260C264-0D73-40D1-BE14-B13998AE15DE";
	
	// change to pull and save different games
	private static String playerName = "Buttface69";
	private static long matchId = 2236005877L;
	private static String rating = "A";
	
	
	
	private int totalCS;

	public MatchCleaner(long id, String r, String pN) {
		matchId = id;
		rating = r;
		playerName = pN;
		
	}
	
	public MatchDetail getMatch() throws RiotApiException {
		RiotApi api = new RiotApi(apiKey);
		MatchDetail md = api.getMatch(matchId);
		return md;
	}
	
	public int getParticipantId() throws RiotApiException {
		MatchDetail md = getMatch();
		List<ParticipantIdentity> p = md.getParticipantIdentities();
		for (int k = 0; k < p.size(); k++) {
			ParticipantIdentity pi = p.get(k);
			Player player = pi.getPlayer();
			if (player.getSummonerName().equals(playerName)) {
				return pi.getParticipantId();
			}
		}
		return 0;
	}
	
	public Participant getParticipant() throws RiotApiException {
		int id = getParticipantId();
		MatchDetail md = getMatch();
		List<Participant> lp = md.getParticipants();
		Participant p = lp.get(id - 1);
		return p;
	}
	
	public JsonObject buildPlayer() throws RiotApiException {
		Participant p = getParticipant();
		ParticipantStats pstats = p.getStats();
		ParticipantTimeline ptime = p.getTimeline();
		
		totalCS = (int) (pstats.getMinionsKilled() + pstats.getNeutralMinionsKilled());
		
		Gson g = new Gson();
		String s = g.toJson(pstats);
		JsonParser jp = new JsonParser();
		JsonObject stats = (JsonObject)jp.parse(s);
		stats.addProperty("rating", rating);
		stats.addProperty("totalCS", totalCS);
		
		Gson gg = new Gson();
		String t = gg.toJson(ptime);
		JsonElement time = jp.parse(t);
		
		JsonObject player = new JsonObject();
		player.addProperty("teamId", p.getTeamId());
		player.addProperty("spell1Id", p.getSpell1Id());
		player.addProperty("spell2Id", p.getSpell2Id());
		player.addProperty("championId", p.getChampionId());
		player.addProperty("highestAchievedSeasonTier", p.getHighestAchievedSeasonTier());
		player.add("timeline", time);
		player.add("stats", stats);
		return player;
	}
	
	public JsonObject buildJson() throws RiotApiException {
		MatchDetail md = getMatch();
		JsonObject cleanGame = new JsonObject();
		cleanGame.addProperty("matchId", md.getMatchId());
		cleanGame.addProperty("region", md.getRegion());
		cleanGame.addProperty("platformId", md.getPlatformId());
		cleanGame.addProperty("matchMode", md.getMatchMode());
		cleanGame.addProperty("matchType", md.getMatchType());
		cleanGame.addProperty("matchCreation", md.getMatchCreation());
		cleanGame.addProperty("matchDuration", md.getMatchDuration());
		cleanGame.addProperty("queueType", md.getQueueType());
		cleanGame.addProperty("mapId", md.getMapId());
		cleanGame.addProperty("season", md.getSeason());
		cleanGame.addProperty("matchVersion", md.getMatchVersion());
		cleanGame.add("player", buildPlayer());
		return cleanGame;
	}

	public static void main(String[] args) throws RiotApiException, IOException {
		MatchCleaner mc = new MatchCleaner(matchId, rating, playerName);
		JsonObject jo = mc.buildJson();
		System.out.println("The matchId is: " + jo.get("matchId"));
		System.out.println(jo.toString());
		try (FileWriter file = new FileWriter("data/clean/" + playerName + " - " + matchId + ".json")) {
			file.write(jo.toString());
			System.out.println("Successfully Copied JSON Object to File...");
			System.out.println("\nJSON Object: " + jo);
		}
	}
}
