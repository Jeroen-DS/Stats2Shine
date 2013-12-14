/**
 * 
 */
package fantasy;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Jeroen
 *
 */
public class Player {
	private Integer ysPlayerId;
	private String name;
	private Collection<String> ysPositions = new HashSet<String>();
	private String team;
	private Integer teamGamesToPlay;
	
	public Player(Integer ysPlayerId, String name, Collection<String> ysPositions, String team) {
		this.ysPlayerId = ysPlayerId;
		this.name = name;
		this.ysPositions = ysPositions;
		this.team = team;
	}

	public Integer getYsPlayerId() {
		return ysPlayerId;
	}

	public void setYsPlayerId(Integer ysPlayerId) {
		this.ysPlayerId = ysPlayerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<String> getYsPositions() {
		return ysPositions;
	}

	public void setYsPositions(Collection<String> ysPositions) {
		this.ysPositions = ysPositions;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public Integer getTeamGamesToPlay() {
		return teamGamesToPlay;
	}

	public void setTeamGamesToPlay(Integer teamGamesToPlay) {
		this.teamGamesToPlay = teamGamesToPlay;
	}

	public void printGamesToPlay() {
		boolean notFirst = false;
		for(String ysPosition : ysPositions) {
			if(notFirst) {
				System.out.print(", ");
			} else {
				notFirst = true;
			}
			System.out.print(ysPosition);
		}
		System.out.println(" - ");
		System.out.println(name + ": " + teamGamesToPlay.toString());
	}
}
