/**
 * 
 */
package program;

import java.io.IOException;
import java.text.ParseException;

import data.XMLStats;
import fantasy.Team;

/**
 * @author Jeroen
 *
 */
public class RosterManagement {
	
	private static String[] positions = {"PG", "SG", "G", "SF", "PF", "F", "C", "UTIL"};
	private static Team myTeam;
	private static String key_xmlstats;
	private static String key_yahoo;
	private static String secret_yahoo;
	
	private static XMLStats xmlStats = new XMLStats();
	
	/**
	 * @param args = keys for application
	 * arg[0] = xmlstats
	 * arg[1] = yahoo
	 * @throws IOException 
	 * 
	 */
	public static void main(String[] args) throws IOException {
		key_xmlstats = args[0];
		key_yahoo = args[1];
		secret_yahoo = args[2];
		
		try {
			myTeam = new Team();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Integer nbGamesProjected = manageGamesPlayed();
		System.out.println(nbGamesProjected);

	}
	
	/**
	 * @return total projected games with current roster
	 * @throws IOException 
	 */
	private static Integer manageGamesPlayed() throws IOException {
		
		Integer projTotalNbGamesPlayed = 0;
		
		myTeam.updateTeam(key_yahoo, secret_yahoo);
		
		// for each position
		for (String position : positions) {
			Integer projNbGamesPlayedAtPos = 0;
		
			// get number of played at this position
			Integer nbGamesPlayedAtPos = myTeam.getNbGamesPlayed(position);
			projNbGamesPlayedAtPos += nbGamesPlayedAtPos;
			projTotalNbGamesPlayed += nbGamesPlayedAtPos;
			System.out.println("Games played at " + position + ": " + nbGamesPlayedAtPos);
			
			// get team of each player at this position
			String fullTeamName = "San Antonio Spurs";
			
			// get number of games to play for each player
			Integer nbGamesPlayedByTeam = xmlStats.getNbGamesPlayedByTeam(key_xmlstats, fullTeamName);
			projNbGamesPlayedAtPos += nbGamesPlayedByTeam;
			projTotalNbGamesPlayed += nbGamesPlayedByTeam;
			System.out.println("Games played by " + fullTeamName + ": " + nbGamesPlayedByTeam);
			System.out.println("Projected games played at " + position + ": " + projNbGamesPlayedAtPos);
		}
		
		return projTotalNbGamesPlayed;
	}

}
