package program;

public class Util {
	
	final static Integer PG = 0;
	final static Integer SG = 1;
	final static Integer G = 2;
	final static Integer SF = 3;
	final static Integer PF = 4;
	final static Integer F = 5;
	final static Integer C = 6;
	final static Integer UTIL = 7;

	public static Integer positionToIndex(String position) {
		Integer index = null;
		switch (position) {
		case "PG":
			index = PG;
			break;
		case "SG":
			index = SG;
			break;
		case "G":
			index = G;
			break;
		case "SF":
			index = SF;
			break;
		case "PF":
			index = PF;
			break;
		case "F":
			index = F;
			break;
		case "C":
			index = C;
			break;
		case "Util":
			index = UTIL;
			break;
		}
		
		return index;
	}
	
	public static String indexToPosition(Integer index) {
		String position = null;
		switch (index) {
		case 0:
			position = "PG";
			break;
		case 1:
			position = "SG";
			break;
		case 2:
			position = "G";
			break;
		case 3:
			position = "SF";
			break;
		case 4:
			position = "PF";
			break;
		case 5:
			position = "F";
			break;
		case 6:
			position = "C";
			break;
		case 7:
			position = "Util";
			break;
		}
		
		return position;
	}
}
