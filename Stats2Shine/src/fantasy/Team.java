/**
 * 
 */
package fantasy;

import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	private Map<String, Player> roster;
	private Map<String, Integer> nbGamesPlayed;
	
	private YahooSports yahooSports;
	
	public Team() throws ParseException {
		super();
		this.yahooSports = new YahooSports();
		this.dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dfm.setTimeZone(TimeZone.getTimeZone("UTC"));
		this.lastGamedaySynced = START_SEASON;
		this.nbGamesPlayed = new HashMap<String, Integer>();
		String[] positions = {"PG", "SG", "G", "SF", "PF", "F", "C", "UTIL"};
		for(String position : positions) {
			this.nbGamesPlayed.put(position, 0);
		}
	}
		
	/**
	 * @param nbGamesPlayed
	 */
	public Team(Map<String, Integer> nbGamesPlayed) {
		super();
		this.yahooSports = new YahooSports();
		this.nbGamesPlayed = nbGamesPlayed;
	}
	
	public void updateTeam(String key, String secret) throws MalformedURLException {
		lastSynced = Calendar.getInstance();
		System.out.println(lastSynced.getTimeInMillis());
		System.out.println(lastGamedaySynced.getTimeInMillis());
		System.out.println(lastSynced.getTimeInMillis() - lastGamedaySynced.getTimeInMillis());
		System.out.println(60*60*24*1000);
		while((lastSynced.getTimeInMillis() - lastGamedaySynced.getTimeInMillis()) > 60*60*24*1000 ) {
			String[] positions = {"PG", "SG", "G", "SF", "PF", "F", "C", "UTIL"};
			for(String position : positions) {
				Player player = yahooSports.getPlayer(key, secret, position);
				if(player!=null) {
					nbGamesPlayed.put(position, nbGamesPlayed.get(position) + 1);
				}
			}
			
			lastGamedaySynced.add(Calendar.DATE, 1);
		}
		
	}

	public Integer getTotalNbGamesPlayed() {
		Integer totalNbGamesPlayed = 0;
		for (String position: nbGamesPlayed.keySet()) {
			totalNbGamesPlayed += nbGamesPlayed.get(position);
		}
		return totalNbGamesPlayed;
	}

	public Integer getNbGamesPlayed(String position) {
		return nbGamesPlayed.get(position);
	}
	
}
