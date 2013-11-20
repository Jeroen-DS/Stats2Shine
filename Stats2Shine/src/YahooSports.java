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
		// Create the OAuthService object
		OAuthService service = new ServiceBuilder().provider(YahooApi.class).apiKey("dj0yJmk9RWdwb2Z0Y3dNMFNXJmQ9WVdrOVRVOUhNRE5WTkhNbWNHbzlPVEF4TVRnMk1UWXkmcz1jb25zdW1lcnNlY3JldCZ4PTE4").apiSecret("0ca60a1123428d2daeaca717bc302be9f5f6aa7e").build();
		// Get the request token
		Token requestToken = service.getRequestToken();
		// Making the user validate your request token
		String authUrl = service.getAuthorizationUrl(requestToken);
		// Get the access Token
		Verifier v = new Verifier("verifier");
		Token accessToken = service.getAccessToken(requestToken, v);
		// Sign request
		OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yahoo.com/");
		service.signRequest(accessToken, request);
		Response response = request.send();
		System.out.println(response.getBody());
	}

}
