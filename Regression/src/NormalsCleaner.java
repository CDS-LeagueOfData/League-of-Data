import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import net.rithms.riot.api.RateLimitException;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.dto.Match.MatchDetail;
import net.rithms.riot.dto.Match.Participant;
import net.rithms.riot.dto.Match.ParticipantIdentity;
import net.rithms.riot.dto.Match.ParticipantStats;
import net.rithms.riot.dto.Match.ParticipantTimeline;
import net.rithms.riot.dto.Match.Player;
import net.rithms.riot.dto.Static.Champion;
import net.rithms.riot.dto.Static.ChampionList;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class NormalsCleaner {
	// change to own summoner key:
	private static String apiKey;

	// change to pull and save different games
	private static String playerName;
	private static long matchId;
	private static String rating;
	private static boolean winner;
	private static String champion;

	private int totalCS;

	public NormalsCleaner(long id, String r, String pN, String c, boolean w) {
		matchId = id;
		rating = r;
		playerName = pN;
		winner = w;
		champion = c;
	}

	public MatchDetail getMatch() throws RiotApiException {
		RiotApi api = new RiotApi(apiKey);
		MatchDetail md = api.getMatch(matchId);
		return md;
	}

	public int getChampionId() throws RiotApiException {
		RiotApi api = new RiotApi(apiKey);
		ChampionList cl = api.getDataChampionList();
		Map<String, Champion> allChamps = cl.getData();	
		Champion ourChamp = allChamps.get(champion);
		int id = ourChamp.getId();
		return id;	
	}

	public Participant getParticipant() throws RiotApiException {
		int id = getChampionId();
		MatchDetail md = getMatch();
		List<Participant> lp = md.getParticipants();
		for (Participant p : lp) {
			if ((p.getChampionId() == id) && (p.getStats().isWinner() == winner)) {
				return p;
			}
		}
		return null;
	}

	public JsonObject buildPlayer() throws RiotApiException {
		Participant p = getParticipant();
		ParticipantStats pstats = p.getStats();
		ParticipantTimeline ptime = p.getTimeline();

		totalCS = (int) (pstats.getMinionsKilled() + pstats.getNeutralMinionsKilled());

		Gson g = new Gson();
		String s = g.toJson(pstats);
		JsonParser jp = new JsonParser();
		JsonObject stats = (JsonObject) jp.parse(s);
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

	/*
	 * Please set your working space under run configuration --> arguments -->
	 * working directory. Set it to be Regression/data/clean
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) throws RiotApiException, IOException {
		Scanner scanner = new Scanner(System.in).useDelimiter("\\n");
		System.out.println("Please paste your api-key: ");
		apiKey = scanner.nextLine();
		System.out.println("Please paste the matchID. You don't need to put the L at the end, just put in pure number: ");
		matchId = scanner.nextLong();
		System.out.println("Please paste the summoner's name: ");
		playerName = scanner.next();
		System.out.println("Which champion did they play? (no spaces e.g. TahmKench): ");
		champion = scanner.next();
		System.out.println("Did they win? (True or False): ");
		winner = scanner.nextBoolean();
		System.out.println("Please paste the rating that you recorded: ");
		// System.out.println("hi" + playerName);
		rating = scanner.next();
		
		NormalsCleaner mc = new NormalsCleaner(matchId, rating, playerName, champion, winner);
		JsonObject jo;
		try {
			jo = mc.buildJson();
		} catch (RateLimitException e) {
			System.out.println("Rate limit exceeded, trying again. Please don't quit.");
			jo = mc.buildJson();
		}
		// System.out.println("The matchId is: " + jo.get("matchId"));
		// System.out.println(jo.toString());
		FileWriter file = new FileWriter(playerName + " - " + matchId + ".json");
		file.write(jo.toString());
		file.close();
		System.out.println("Successfully Copied JSON Object to File...");
		// System.out.println("\nJSON Object: " + jo);

	}
}
