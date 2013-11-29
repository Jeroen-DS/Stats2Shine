package data;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;

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

	public Integer getNbGamesPlayedByTeam(String key_xmlstats, String fullTeamName) throws IOException {
		try {
			URL url = new URL("https://erikberg.com/nba/standings/20131127.json");
			JsonObject jsonStandings = requestURL(url);

			JsonArray jsonStanding = (JsonArray) jsonStandings.get("standing");
			Iterator<JsonValue> it = jsonStanding.iterator();

			while (it.hasNext()) {
				JsonObject jsonObject = (JsonObject) it.next();
				String firstName = jsonObject.get("first_name").toString();
				String lastName = jsonObject.get("last_name").toString();
				if (fullTeamName.equals(firstName.substring(1, firstName.length() - 1) + " " + lastName.substring(1, lastName.length() - 1))) {
					JsonNumber jsonGamesPlayed = (JsonNumber) jsonObject.get("games_played");
					return jsonGamesPlayed.intValue();
				}
			}
		} catch (IOException exception) {
			System.out.println("Unexpected JSON-stream");
		}
		return null;
	}

	
}
