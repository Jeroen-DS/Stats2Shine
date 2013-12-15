package fantasy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import program.Util;

public class Roster {
	private HashMap<Integer, Player> players = new HashMap<Integer, Player>();
	private Integer[] starters = new Integer[10];
	
	private HashMap<String, Integer[]> indexConverter;
	
	public Roster() {
		this.indexConverter = new HashMap<String, Integer[]>();
		indexConverter.put("PG", new Integer[] {0});
		indexConverter.put("SG", new Integer[] {1});
		indexConverter.put("G", new Integer[] {2});
		indexConverter.put("SF", new Integer[] {3});
		indexConverter.put("PF", new Integer[] {4});
		indexConverter.put("F", new Integer[] {5});
		indexConverter.put("C", new Integer[] {6,7});
		indexConverter.put("Util", new Integer[] {8,9});
	}
	
	public void addPlayer(Player player, String position) {
		players.put(player.getYsPlayerId(), player);
		Integer[] indeces = indexConverter.get(position);
		if (indeces != null) {
			for (int i = 0; i < indeces.length; i++) {
				Integer index = indeces[i];
				if (starters[index] == null) {
					starters[index] = player.getYsPlayerId();
					i = indeces.length;
				}
			}
		}
	}
	
	public ArrayList<Player> getPlayers(String position) {
		ArrayList<Player> playersOnPosition = new ArrayList<Player>();
		Integer[] indeces = indexConverter.get(position);
		if (indeces != null) {
			for (int i = 0; i < indeces.length; i++) {
				Integer index = indeces[i];
				playersOnPosition.add(players.get(starters[index]));
			}
		}
		return playersOnPosition;
	}

	public Set<Integer> playerKeySet() {
		return players.keySet();
	}
	
	public HashMap<Integer, Player> getPlayers() {
		return players;
	}

	public void setPlayers(HashMap<Integer, Player> players) {
		this.players = players;
	}

	public void setStarters(Integer[] starters) {
		this.starters = starters;
	}

	public Integer[] getStarters() {
		return starters;
	}

	public Player get(Integer ysPlayerId) {
		return players.get(ysPlayerId);
	}

	public String getPosition(Integer ysPlayerId) {
		for (int i = 0; i < starters.length; i++) {
			if (starters[i] == ysPlayerId) {
				if( i == 7 ) i = 6;
				if (i == 8 || i == 9) i = 7;
				return Util.indexToPosition(i);
			}
		}
		
		return "BN";
	}
}
