/**
 * 
 */
package fantasy;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jeroen
 *
 */
public class Team {
	private Date lastSynced = new Date(0);
	private Map<String, Player> roster;
	private Map<String, Integer> nbGamesPlayed;
	
	public Team() {
		super();
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
		this.nbGamesPlayed = nbGamesPlayed;
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
