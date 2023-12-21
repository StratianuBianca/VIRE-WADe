package info.wade.users.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;

public class Main {
    private static final String CONSUMER_KEY = "yGrcPrZCSijvHSNdbtsk";
    private static final String CONSUMER_SECRET = "crhtnFdQcgXMVXYtgzBYJnbBGSWOmSZA";
    private static final String AUTHORIZE_URL = "https://www.discogs.com/oauth/authorize";
    private static final String ACCESS_TOKEN_URL = "https://api.discogs.com/oauth/access_token";
    private static final String CALLBACK_URL = "your_callback";
    private static final String REQUEST_TOKEN_URL = "https://api.discogs.com/oauth/request_token";


    public static void main(String[] args) throws IOException {
        HttpGet getTempToken = new HttpGet(REQUEST_TOKEN_URL);
        getTempToken.setHeader(HttpHeaders.AUTHORIZATION, getTempTokenHeader());

        HttpResponse response = HttpClients.createDefault().execute(getTempToken);
        HttpEntity entity = response.getEntity();
        String body = readInputStream(entity.getContent());
        parseTempTokenBody(body);

    }

    private static void parseTempTokenBody(String body) throws IOException {

        String[] parts = body.split("&");
        System.out.println("User Token Body" + body);
        String TOKEN = parts[0].substring(parts[0].indexOf("=") + 1);
        String TOKEN_SECRET = parts[1].substring(parts[1].indexOf("=") + 1);
        System.out.println("TOk " + TOKEN);
        System.out.println("SEC " +TOKEN_SECRET);
        System.out.println("PAR " + parts[0]);


        String artistName = "Korn"; // Replace with the desired artist name
        String apiUrl = "https://api.discogs.com/database/search?q=" + artistName + "&type=artist";

        final HttpGet getOrders = new HttpGet(apiUrl);
        getOrders.setHeader(HttpHeaders.AUTHORIZATION, getTempTokenHeader());

        final HttpResponse response = HttpClients.createDefault().execute(getOrders);
        final HttpEntity entity = response.getEntity();

        String bodyy = readInputStream(entity.getContent());
        System.out.println(bodyy);

    }
    private static String getTempTokenHeader() {
        return "OAuth " +
                "oauth_consumer_key=\"" + CONSUMER_KEY + "\", " +
                "oauth_nonce=\"" + UUID.randomUUID() + "\", " +
                "oauth_signature=\"" + CONSUMER_SECRET + "&\", " +
                "oauth_signature_method=\"" + "PLAINTEXT" + "\", " +
                "oauth_timestamp=" + System.currentTimeMillis() / 1000 + "\", " +
                "oauth_callback=" + "\"http://localhost:8090/api/hello\"";
    }
    public static String readInputStream(InputStream inStream) throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        while ((length = inStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        return result.toString("UTF-8");
    }
    private static String getApiOauthHeader(String userToken, String userSecret) {

        return "OAuth " +
                "oauth_consumer_key=\"" + CONSUMER_KEY + "\", " +
                "oauth_nonce=\"" + UUID.randomUUID() + "\", " +
                "oauth_token=\"" + userToken + "\", " +
                "oauth_signature=\"" + CONSUMER_SECRET + "&" + userSecret + "\", " +
                "oauth_signature_method=\"" + "PLAINTEXT" + "\", " +
                "oauth_timestamp=\"" + System.currentTimeMillis() / 1000 + "\"";
    }
}
