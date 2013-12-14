/**
 * 
 */
package fantasy;

import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

	public void printGamesToPlay(HashMap<String, Integer> gamesPlayedByTeam) {
		for (Integer ysPlayerId : roster.getStarters()) {
			Player player = roster.get(ysPlayerId);
			String position = roster.getPosition(ysPlayerId);
			String teamPlayer = player.getTeam();

			// get number of games to play for each player
			Integer nbGamesToPlayByTeam = 82 - gamesPlayedByTeam.get(teamPlayer);
			Integer nbGamesPlayedAtPosition = 0;
			if (Util.positionToIndex(position) != null) {
				nbGamesPlayedAtPosition = nbGamesPlayed[Util.positionToIndex(position)];
				if(Util.positionToIndex(position) > 5) {
					nbGamesPlayedAtPosition = nbGamesPlayedAtPosition/2;
				}
			}
			
			Integer total = nbGamesPlayedAtPosition + nbGamesToPlayByTeam;
			Integer diff = total - 82;

			System.out.println(position + ": " + player.getName() + " ("
					+ teamPlayer + "): " + nbGamesPlayedAtPosition + " + " + nbGamesToPlayByTeam + " = " + total + " (" + diff + ")");
		}

	}

	public void setNbGamesPlayed(Integer[] nbGamesPlayed) {
		this.nbGamesPlayed = nbGamesPlayed;
		
	}

}
