package data;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
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
	private Token accessToken;
	private String key;
	private String secret;

	/**
	 * @param args
	 */
	public JsonObject requestURL(URL url) {
		 Scanner in = new Scanner(System.in);
		 // Create the OAuthService object
		 OAuthService service = new ServiceBuilder().provider(YahooApi.class).apiKey(key).apiSecret(secret).build();
		 // Get the request token
		 Token requestToken = service.getRequestToken();
		 // Making the user validate your request token
		 System.out.println(service.getAuthorizationUrl(requestToken));
		
		 // Get the access Token
		 Verifier v = new Verifier(in.nextLine());
		 this.accessToken = service.getAccessToken(requestToken, v);
		 // Sign request
		 OAuthRequest request = new OAuthRequest(Verb.GET, url.toString());
		 service.signRequest(this.accessToken, request);
		 Response response = request.send();
		 System.out.println(response.getBody());
		 JsonReader reader = Json.createReader(new StringReader(response.getBody()));
		 JsonObject jsonObject = (JsonObject) reader.read();
		 in.close();
		 return jsonObject;
	}
	
	public Player getPlayer(String key, String secret, String position) throws MalformedURLException {
		this.key = key;
		this.secret = secret;
		URL url = new URL("http://fantasysports.yahooapis.com/fantasy/v2/team/322.l.47722.t.2/roster/players?format=json");
		JsonObject jsonRoster = requestURL(url);
		Player player = new Player();
		return player;
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

}
