/**
 * 
 */
package program;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import data.XMLStats;
import data.YahooSports;
import fantasy.Team;

/**
 * @author Jeroen
 *
 */
public class RosterManagement {
	private static Team myTeam;
	private static String key_xmlstats;
	private static String key_yahoo;
	private static String secret_yahoo;
	
	private static XMLStats xmlStats ;
	private static YahooSports yahooSports;
	
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
		
		xmlStats = new XMLStats(key_xmlstats);
		yahooSports = new YahooSports(key_yahoo, secret_yahoo);
		
		myTeam = new Team(yahooSports, manualInputNbGamesPlayed());
		sync();
		
		manageGamesPlayed();

	}
	
	private static Integer manageGamesPlayed() {
		Integer projTotalNbGamesPlayed = 0;
		
		myTeam.updateGamesToPlay(xmlStats.getGamesPlayedByTeams());
		myTeam.printGamesToPlay();
		
		
//		// for each position
//		for (String position : positions) {
//			Integer projNbGamesPlayedAtPos = 0;
//		
//			// get number of played at this position
//			Integer nbGamesPlayedAtPos = myTeam.getNbGamesPlayed(position);
//			projNbGamesPlayedAtPos += nbGamesPlayedAtPos;
//			projTotalNbGamesPlayed += nbGamesPlayedAtPos;
//			System.out.println("Games played at " + position + ": " + nbGamesPlayedAtPos);
//			
//			// get team of each player at this position
//			String fullTeamName = "San Antonio Spurs";
//			
//			// get number of games to play for each player
//			Integer nbGamesPlayedByTeam = xmlStats.getNbGamesPlayedByTeam(key_xmlstats, fullTeamName);
//			projNbGamesPlayedAtPos += nbGamesPlayedByTeam;
//			projTotalNbGamesPlayed += nbGamesPlayedByTeam;
//			System.out.println("Games played by " + fullTeamName + ": " + nbGamesPlayedByTeam);
//			System.out.println("Projected games played at " + position + ": " + projNbGamesPlayedAtPos);
//		}
		
		return projTotalNbGamesPlayed;
	}

	/**
	 * @return total projected games with current roster
	 * @throws IOException 
	 */
	private static void sync() throws IOException {
		myTeam.updateTeam(key_yahoo, secret_yahoo);
	}
	
	private static Integer[] manualInputNbGamesPlayed() throws IOException {
		Integer[] nbGamesPlayed = new Integer[] {0,0,0,0,0,0,0,0};
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Number of games played at PG: ");
		nbGamesPlayed[0] = new Integer(br.readLine());
		System.out.print("Number of games played at SG: ");
		nbGamesPlayed[1] = new Integer(br.readLine());
		System.out.print("Number of games played at G: ");
		nbGamesPlayed[2] = new Integer(br.readLine());
		System.out.print("Number of games played at SF: ");
		nbGamesPlayed[3] = new Integer(br.readLine());
		System.out.print("Number of games played at PF: ");
		nbGamesPlayed[4] = new Integer(br.readLine());
		System.out.print("Number of games played at F: ");
		nbGamesPlayed[5] = new Integer(br.readLine());
		System.out.print("Number of games played at C: ");
		nbGamesPlayed[6] = new Integer(br.readLine());
		System.out.print("Number of games played at Util: ");
		nbGamesPlayed[7] = new Integer(br.readLine());
		
		return nbGamesPlayed;
	}

}
