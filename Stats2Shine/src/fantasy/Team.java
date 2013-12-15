/**
 * 
 */
package fantasy;

import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

import program.Util;
import data.YahooSports;

/**
 * @author Jeroen
 * 
 */
public class Team {
	private Calendar START_SEASON = new GregorianCalendar(2013, 10, 28, 0, 0, 0);

	private DateFormat dfm;
	private Calendar lastSynced, lastGamedaySynced;

	private Roster roster;

	private Integer[] nbGamesPlayed = new Integer[8];

	private YahooSports yahooSports;

	public Team(YahooSports ys) {
		super();
		this.yahooSports = ys;
		this.dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dfm.setTimeZone(TimeZone.getTimeZone("UTC"));
		this.lastGamedaySynced = START_SEASON;
		this.nbGamesPlayed = new Integer[] { 0, 0, 0, 0, 0, 0, 0, 0 };
	}

	/**
	 * @param nbGamesPlayed
	 */
	public Team(YahooSports ys, Integer[] nbGamesPlayed) {
		super();
		this.yahooSports = ys;
		this.dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dfm.setTimeZone(TimeZone.getTimeZone("UTC"));
		this.lastGamedaySynced = START_SEASON;
		this.nbGamesPlayed = nbGamesPlayed;
	}

	public void updateTeam(String key, String secret)
			throws MalformedURLException {
		lastSynced = Calendar.getInstance();
		// System.out.println(lastSynced.getTimeInMillis());
		// System.out.println(lastGamedaySynced.getTimeInMillis());
		// System.out.println(lastSynced.getTimeInMillis() -
		// lastGamedaySynced.getTimeInMillis());
		// System.out.println(60*60*24*1000);
		// while((lastSynced.getTimeInMillis() -
		// lastGamedaySynced.getTimeInMillis()) > 60*60*24*1000 ) {
		this.roster = yahooSports.getRoster();

		// lastGamedaySynced.add(Calendar.DATE, 1);
		// }

	}

	public Integer getTotalNbGamesPlayed() {
		Integer totalNbGamesPlayed = 0;
		for (int i = 0; i < 8; i++) {
			totalNbGamesPlayed += nbGamesPlayed[i];
		}
		return totalNbGamesPlayed;
	}

	public void addNbGamesPlayed(String position, Integer nbGames) {
		Integer index = Util.positionToIndex(position);

		if (index != null) {
			nbGamesPlayed[index] += nbGames;
		}
	}

	public Integer getNbGamesPlayed(String position) {
		Integer index = Util.positionToIndex(position);

		if (index != null) {
			return nbGamesPlayed[index];
		}

		return null;
	}
	
	public void updateGamesToPlay(HashMap<String, Integer> gamesPlayedByTeam) {
		HashMap<Integer, Player> players = this.roster.getPlayers();
		for(Integer playerId : players.keySet()) {
			Player player = players.get(playerId);
			String team = player.getTeam();
			player.setTeamGamesToPlay(82 - gamesPlayedByTeam.get(team));
		}
	}

	public void printGamesToPlay() {
		Integer totalGamesPlayed = 0;
		Integer totalGamesProjected = 0;
		
		for (int i = 0; i < 8; i++) {
			String str = "";
			String position = Util.indexToPosition(i);
			str += position + ":";
			ArrayList<Player> playersOnPosition = roster.getPlayers(Util.indexToPosition(i));
			for (Player pl : playersOnPosition) {
				str += " " + pl.getName() + " (" + pl.getTeamAbbr() + ")";
			}
			
			Integer projection = 0;
			Integer maxGamesProjection = 0;
			projection += nbGamesPlayed[i];
			totalGamesPlayed += nbGamesPlayed[i];
			str += ": " + nbGamesPlayed[i];
			
			for (Player pl : playersOnPosition) {
				projection += pl.getTeamGamesToPlay();
				maxGamesProjection += 82;
				
				str += " + " + pl.getTeamGamesToPlay();
			}
			
			totalGamesProjected += projection;
			Integer diff = projection - maxGamesProjection;
			str += " = " + projection + " (" + diff + ")";

			System.out.println(str);
		}
		
		Integer diffTotal = totalGamesProjected - 820;
		
		System.out.println("Total games played: " + totalGamesPlayed);
		System.out.println("Total games projected: " + totalGamesProjected + " (" + diffTotal + ")");

	}

	public void setNbGamesPlayed(Integer[] nbGamesPlayed) {
		this.nbGamesPlayed = nbGamesPlayed;
		
	}

}
