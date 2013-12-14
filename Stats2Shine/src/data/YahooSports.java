package data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.YahooApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import fantasy.Player;
import fantasy.Roster;

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
	
	public Roster getRoster() throws MalformedURLException {
		URL url = new URL("http://fantasysports.yahooapis.com/fantasy/v2/team/322.l.47722.t.2/roster/players?format=json");
		Response response = this.auth.requestURL(url);
		JsonObject jsonFantasyTeam = convertToJson(response);
		
		Roster roster = new Roster();
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
			
			String position = ((JsonObject) ((JsonArray) ((JsonObject) jsonPlayer.get(1)).get("selected_position")).get(1)).getString("position");
			roster.addPlayer(player, position);
		}
		
		return roster;
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
		
		protected void authorise() throws IOException {
			
			// Get the request token
			Token requestToken = this.service.getRequestToken();
			// Making the user validate your request token
			System.out.println(this.service.getAuthorizationUrl(requestToken));
			
			// Get the access Token
			Scanner sc = new Scanner(System.in);
			Verifier v = new Verifier(sc.nextLine());
			
			setAccessToken(this.service.getAccessToken(requestToken, v));
			
			this.authSucces = true;
		}
		
		protected Response requestURL(URL url) {
			if(!authSucces) {
				try {
					authorise();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			OAuthRequest request = new OAuthRequest(Verb.GET, url.toString());
			this.service.signRequest(accessToken, request);
			Response response = request.send();
			
			return response;
		}
	}
}
