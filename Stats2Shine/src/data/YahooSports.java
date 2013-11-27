package data;
import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.YahooApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * 
 */

/**
 * @author Jeroen
 * 
 */
public class YahooSports {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 Scanner in = new Scanner(System.in);
		 // Create the OAuthService object
		 OAuthService service = new ServiceBuilder().provider(YahooApi.class).apiKey(args[0]).apiSecret(args[1]).build();
		 // Get the request token
		 Token requestToken = service.getRequestToken();
		 // Making the user validate your request token
		 System.out.println(service.getAuthorizationUrl(requestToken));
		
		 // Get the access Token
		 Verifier v = new Verifier(in.nextLine());
		 Token accessToken = service.getAccessToken(requestToken, v);
		 // Sign request
		 OAuthRequest request = new OAuthRequest(Verb.GET, "http://fantasysports.yahooapis.com/fantasy/v2/league/322.l.47722/teams?format=json");
		 service.signRequest(accessToken, request);
		 Response response = request.send();
		 System.out.println(response.getBody());
		 in.close();
	}

}
