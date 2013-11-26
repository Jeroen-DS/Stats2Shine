import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.zip.GZIPInputStream;

import javax.json.Json;
import javax.json.stream.JsonParser;

/**
 * 
 */

/**
 * @author Jeroen
 *
 */
public class XMLStats {
	
	static final String USER_AGENT_NAME = "Stats2Shine";
	static final String AUTHORIZATION = "Authorization";
	static final String USER_AGENT = "User-agent";
	static final String ACCEPT_ENCODING = "Accept-encoding";
	static final String GZIP = "gzip";
	
	static final String REQUEST_URL = "https://erikberg.com/nba/standings/20131126.json";
	static final String TIME_ZONE = "America/New_York";
	static final String ISO_8601_FMT = "yyyy-MM-dd'T'HH:mm:ssXXX";
	static final SimpleDateFormat XMLSTATS_DATE = new SimpleDateFormat(ISO_8601_FMT);

	public static void main(String[] args) {
        InputStream in = null;
        try {
            URL url = new URL(REQUEST_URL);
            URLConnection connection = url.openConnection();
            // Set Authorization header
            
            connection.setRequestProperty(AUTHORIZATION, "Bearer " + args[0]);
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

        StringBuilder sb = new StringBuilder();
        if (in != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (sb != null) {
            printResult(sb.toString());
        }
    }    

    static void printResult(String jsonData) {
    	JsonParser parser = Json.createParser(new StringReader(jsonData));
    	while (parser.hasNext()) {
    	   JsonParser.Event event = parser.next();
    	   switch(event) {
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
    	         System.out.print(event.toString() + " " +
    	                          parser.getString() + " - ");
    	         break;
    	      case VALUE_STRING:
    	      case VALUE_NUMBER:
    	         System.out.println(event.toString() + " " +
    	                            parser.getString());
    	         break;
    	   }
    	}
    }
}
