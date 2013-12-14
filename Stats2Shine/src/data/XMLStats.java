package data;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

/**
 * 
 */

/**
 * @author Jeroen
 * 
 */
public class XMLStats {

	private String key;

	static final String USER_AGENT_NAME = "Stats2Shine";
	static final String AUTHORIZATION = "Authorization";
	static final String USER_AGENT = "User-agent";
	static final String ACCEPT_ENCODING = "Accept-encoding";
	static final String GZIP = "gzip";

	static final String TIME_ZONE = "America/New_York";
	static final String ISO_8601_FMT = "yyyy-MM-dd'T'HH:mm:ssXXX";
	static final SimpleDateFormat XMLSTATS_DATE = new SimpleDateFormat(
			ISO_8601_FMT);
	
	private HashMap<String, Integer> nbGamesPlayedByTeam = new HashMap<String, Integer>();
	
	public XMLStats(String key) {
		this.key = key;
	}

	private JsonObject requestURL(URL url) {
		InputStream in = null;
		try {
			URLConnection connection = url.openConnection();
			// Set Authorization header

			connection.setRequestProperty(AUTHORIZATION, "Bearer " + key);
			// Set User agent header
			connection.setRequestProperty(USER_AGENT, USER_AGENT_NAME);
			// Let server know we can handle gzip content
			connection.setRequestProperty(ACCEPT_ENCODING, GZIP);

			in = connection.getInputStream();
			// Check if response was sent gzipped and decompress it
			String encoding = connection.getContentEncoding();
			if (GZIP.equals(encoding)) {
				in = new GZIPInputStream(in);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		JsonReader reader = Json.createReader(in);
		JsonObject jsonObject = (JsonObject) reader.read();

		return jsonObject;
	}

	private void syncNbGamesPlayedByTeam() throws IOException {
		try {
			URL url = new URL("https://erikberg.com/nba/standings/20131214.json");
			JsonObject jsonStandings = requestURL(url);

			JsonArray jsonStanding = (JsonArray) jsonStandings.get("standing");
			Iterator<JsonValue> it = jsonStanding.iterator();

			nbGamesPlayedByTeam.clear();
			while (it.hasNext()) {
				JsonObject jsonObject = (JsonObject) it.next();
				String firstName = jsonObject.getString("first_name");
				String lastName = jsonObject.getString("last_name");
				String fullName = firstName + " " + lastName;
				Integer gamesPlayed = jsonObject.getJsonNumber("games_played").intValue();
				
				nbGamesPlayedByTeam.put(fullName, gamesPlayed);
			}
		} catch (IOException exception) {
			System.out.println("Unexpected JSON-stream");
		}
	}

	public HashMap<String, Integer> getGamesPlayedByTeams() {
		try {
			syncNbGamesPlayedByTeam();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nbGamesPlayedByTeam;
	}

	
}
