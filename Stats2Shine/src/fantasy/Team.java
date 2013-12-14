/**
 * 
 */
package fantasy;

import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import data.YahooSports;

/**
 * @author Jeroen
 *
 */
public class Team {
	private Calendar START_SEASON = new GregorianCalendar(2013,10,28,0,0,0);
	
	private DateFormat dfm;
	private Calendar lastSynced, lastGamedaySynced;
	
	private Map<Integer, Player> roster = new HashMap<Integer, Player>();
	private Integer[] starters = new Integer[10];
	
	private Integer[] nbGamesPlayed = new Integer[8];
	
	private YahooSports yahooSports;
	
	public Team(YahooSports ys) throws ParseException {
		super();
		this.yahooSports = ys;
		this.dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dfm.setTimeZone(TimeZone.getTimeZone("UTC"));
		this.lastGamedaySynced = START_SEASON;
		this.nbGamesPlayed = new Integer[] {0,0,0,0,0,0,0,0};
	}
		
	/**
	 * @param nbGamesPlayed
	 */
	public Team(YahooSports ys, Integer[] nbGamesPlayed) {
		super();
		this.yahooSports = ys;
		this.nbGamesPlayed = nbGamesPlayed;
	}
	
	public void updateTeam(String key, String secret) throws MalformedURLException {
		lastSynced = Calendar.getInstance();
//		System.out.println(lastSynced.getTimeInMillis());
//		System.out.println(lastGamedaySynced.getTimeInMillis());
//		System.out.println(lastSynced.getTimeInMillis() - lastGamedaySynced.getTimeInMillis());
//		System.out.println(60*60*24*1000);
//		while((lastSynced.getTimeInMillis() - lastGamedaySynced.getTimeInMillis()) > 60*60*24*1000 ) {
			this.roster = yahooSports.getRoster();
			
//			lastGamedaySynced.add(Calendar.DATE, 1);
//		}
		
	}

	public Integer getTotalNbGamesPlayed() {
		Integer totalNbGamesPlayed = 0;
		for (int i = 0; i < 8; i++) {
			totalNbGamesPlayed += nbGamesPlayed[i];
		}
		return totalNbGamesPlayed;
	}

	public void addNbGamesPlayed(String position, Integer nbGames) {
		Integer index = null;
		switch (position) {
		case "PG":
			index = 0;
			break;
		case "SG":
			index = 1;
			break;
		case "G":
			index = 2;
			break;
		case "SF":
			index = 3;
			break;
		case "PF":
			index = 4;
			break;
		case "F":
			index = 5;
			break;
		case "C":
			index = 6;
			break;
		case "Util":
			index = 7;
			break;
		}

		if(index != null) {
			nbGamesPlayed[index] += nbGames;
		}
	}
	
	public Integer getNbGamesPlayed(String position) {
		Integer index = null;
		switch (position) {
		case "PG":
			index = 0;
			break;
		case "SG":
			index = 1;
			break;
		case "G":
			index = 2;
			break;
		case "SF":
			index = 3;
			break;
		case "PF":
			index = 4;
			break;
		case "F":
			index = 5;
			break;
		case "C":
			index = 6;
			break;
		case "Util":
			index = 7;
			break;
		}
		return nbGamesPlayed[index];
	}

	public void printGamesToPlay(HashMap<String, Integer> gamesPlayedByTeam) {
		for(Integer ysPlayerId : roster.keySet()) {
			Player player = roster.get(ysPlayerId);
			String teamPlayer = player.getTeam();
			
			// get number of games to play for each player
			Integer nbGamesPlayedByTeam = gamesPlayedByTeam.get(teamPlayer);
			
			System.out.println("Games played by " + player.getName() + " (" + teamPlayer + "): " + nbGamesPlayedByTeam);
		}
		
	}
	
}
