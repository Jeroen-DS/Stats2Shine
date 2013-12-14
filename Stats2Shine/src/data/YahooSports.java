package data;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.YahooApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import fantasy.Player;

/**
 * 
 */

/**
 * @author Jeroen
 * 
 */
public class YahooSports {
	
	private Authorisation auth;
	
	public YahooSports(String key, String secret) {
		this.auth = new Authorisation(key, secret);
	}

	public JsonObject convertToJson(Response response) {
		 JsonReader reader = Json.createReader(new StringReader(response.getBody()));
		 JsonObject jsonObject = (JsonObject) reader.read();

		 return jsonObject;
	}
	
	public HashMap<Integer, Player> getRoster() throws MalformedURLException {
		URL url = new URL("http://fantasysports.yahooapis.com/fantasy/v2/team/322.l.47722.t.2/roster/players?format=json");
		Response response = this.auth.requestURL(url);
		JsonObject jsonFantasyTeam = convertToJson(response);
		
		Set<String> test = jsonFantasyTeam.keySet();
		
		HashMap<Integer, Player> roster = new HashMap<Integer, Player>();
		JsonArray jsonTeam = (JsonArray) ((JsonObject) jsonFantasyTeam.get("fantasy_content")).get("team");
		JsonObject jsonRoster = (JsonObject) ((JsonObject) jsonTeam.get(1)).get("roster");
		JsonObject jsonPlayers = (JsonObject) ((JsonObject) jsonRoster.get("0")).get("players");
		
		for(int i = 0; i < 13; i++) {
			JsonArray jsonPlayer = (JsonArray) ((JsonObject) jsonPlayers.get(String.valueOf(i))).get("player");
			JsonArray jsonPlayerInfo = (JsonArray) jsonPlayer.get(0);
			Integer ysPlayerId = Integer.parseInt(((JsonObject) jsonPlayerInfo.get(1)).getString("player_id"));
			JsonObject jsonName = (JsonObject) ((JsonObject) jsonPlayerInfo.get(2)).get("name");
			String fullName =  jsonName.getString("full");
			System.out.println(fullName);
			JsonObject jsonTemp = (JsonObject) jsonPlayerInfo.get(5);
			if (!jsonTemp.containsKey("editorial_team_full_name")) {
				jsonTemp = (JsonObject) jsonPlayerInfo.get(6);
			}
			String teamName = jsonTemp.getString("editorial_team_full_name");
			Player player = new Player(ysPlayerId, fullName, null, teamName);
			roster.put(ysPlayerId, player);
		}
		
		return roster;
	}
	
	private void printResult(String jsonData) {
		JsonParser parser = Json.createParser(new StringReader(jsonData));
		while (parser.hasNext()) {
			JsonParser.Event event = parser.next();
			switch (event) {
			case START_ARRAY:
			case END_ARRAY:
			case START_OBJECT:
			case END_OBJECT:
			case VALUE_FALSE:
			case VALUE_NULL:
			case VALUE_TRUE:
				System.out.println(event.toString());
				break;
			case KEY_NAME:
				System.out.print(event.toString() + " " + parser.getString() + " - ");
				break;
			case VALUE_STRING:
			case VALUE_NUMBER:
				System.out.println(event.toString() + " " + parser.getString());
				break;
			}
		}
	}
	
	class Authorisation {
		private boolean authSucces;
		OAuthService service;
		private Token accessToken;
		
		public Authorisation(String key, String secret) {
			this.service = new ServiceBuilder().provider(YahooApi.class).apiKey(key).apiSecret(secret).build();
			this.authSucces = false;
		}
		
		protected void setAccessToken(Token accessToken) {
			this.accessToken = accessToken;
		}
		
		protected Token getAccessToken() {
			return accessToken;
		}
		
		protected boolean isGood() {
			return authSucces;
		}
		
		protected void authorise() {
			
			// Get the request token
			Token requestToken = this.service.getRequestToken();
			// Making the user validate your request token
			System.out.println(this.service.getAuthorizationUrl(requestToken));
			
			// Get the access Token
			Scanner in = new Scanner(System.in);
			Verifier v = new Verifier(in.nextLine());
			in.close();
			
			setAccessToken(this.service.getAccessToken(requestToken, v));
			
			this.authSucces = true;
		}
		
		protected Response requestURL(URL url) {
			if(!authSucces) {
				authorise();
			}
			
			OAuthRequest request = new OAuthRequest(Verb.GET, url.toString());
			this.service.signRequest(accessToken, request);
			Response response = request.send();
			
			return response;
		}
	}
}
