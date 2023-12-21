package info.wade.users.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class DiscogsService {

    private static final String CONSUMER_KEY = "yGrcPrZCSijvHSNdbtsk";
    private static final String CONSUMER_SECRET = "crhtnFdQcgXMVXYtgzBYJnbBGSWOmSZA";
    private static final String AUTHORIZE_URL = "https://www.discogs.com/oauth/authorize";
    private static final String ACCESS_TOKEN_URL = "https://api.discogs.com/oauth/access_token";
    private static final String CALLBACK_URL = "your_callback";
    private static final String REQUEST_TOKEN_URL = "https://api.discogs.com/oauth/request_token";
    private static String TOKEN = "";
    private static String TOKEN_SECRET = "";

    public String getAuthenticationUrl() throws IOException {
        HttpGet getTempToken = new HttpGet(REQUEST_TOKEN_URL);
        getTempToken.setHeader(HttpHeaders.AUTHORIZATION, getTempTokenHeader());

        HttpResponse response = HttpClients.createDefault().execute(getTempToken);
        HttpEntity entity = response.getEntity();
        String body = readInputStream(entity.getContent());
        return parseTempTokenBody(body);

    }
    private String getTempTokenHeader() {
        return "OAuth " +
                "oauth_consumer_key=\"" + CONSUMER_KEY + "\", " +
                "oauth_nonce=\"" + UUID.randomUUID() + "\", " +
                "oauth_signature=\"" + CONSUMER_SECRET + "&\", " +
                "oauth_signature_method=\"" + "PLAINTEXT" + "\", " +
                "oauth_timestamp=" + System.currentTimeMillis() / 1000 + "\", " +
                "oauth_callback=" + "\"http://localhost:8090/api/hello\"";
    }
    private String parseTempTokenBody(String body) throws IOException {

        String[] parts = body.split("&");
        System.out.println("User Token Body" + body);
        TOKEN = parts[0].substring(parts[0].indexOf("=") + 1);
        TOKEN_SECRET = parts[1].substring(parts[1].indexOf("=") + 1);
        System.out.println("TOk " + TOKEN);
        System.out.println("SEC " +TOKEN_SECRET);
        System.out.println("PAR " + parts[0]);

        return AUTHORIZE_URL + "?"+ parts[0];

    }
    public String getAccessToken(String verifierCode, String auth) throws Exception {

        HttpPost accessRequest = new HttpPost(ACCESS_TOKEN_URL);
        accessRequest.setHeader(HttpHeaders.AUTHORIZATION, getAccessTokenHeader(verifierCode));

        System.out.println("Access Body");
        System.out.println(getAccessTokenHeader(verifierCode));
        try {
            HttpResponse response = HttpClients.createDefault().execute(accessRequest);
            if (response.getStatusLine().getStatusCode() == 200) {

                String body = readInputStream(response.getEntity().getContent());



                String artistName = "Korn"; // Replace with the desired artist name
                String apiUrl = "https://api.discogs.com/database/search?q=" + artistName + "&type=artist";

                final HttpGet getOrders = new HttpGet(apiUrl);
                getOrders.setHeader(HttpHeaders.AUTHORIZATION, body);

                final HttpResponse response1 = HttpClients.createDefault().execute(getOrders);
                final HttpEntity entity = response1.getEntity();

                String bodyy = readInputStream(entity.getContent());
                System.out.println(bodyy);
                return bodyy;
            } else {
                System.out.println("Access_Token" + response.getStatusLine());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }

    public String readInputStream(InputStream inStream) throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        while ((length = inStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        return result.toString(StandardCharsets.UTF_8);
    }

    private String getApiOauthHeader(String userToken, String userSecret) {

        return "OAuth " +
                "oauth_consumer_key=\"" + CONSUMER_KEY + "\", " +
                "oauth_nonce=\"" + UUID.randomUUID() + "\", " +
                "oauth_token=\"" + userToken + "\", " +
                "oauth_signature=\"" + CONSUMER_SECRET + "&" + userSecret + "\", " +
                "oauth_signature_method=\"" + "PLAINTEXT" + "\", " +
                "oauth_timestamp=\"" + System.currentTimeMillis() / 1000 + "\"";
    }

    private String getAccessTokenHeader(String verifier) {

        return "OAuth " +
                "oauth_consumer_key=\"" + CONSUMER_KEY + "\", " +
                "oauth_nonce=\"" + UUID.randomUUID() + "\", " +
                "oauth_token=\"" + TOKEN + "\", " +
                "oauth_signature=\"" + CONSUMER_SECRET + "&" + TOKEN_SECRET + "\", " +
                "oauth_signature_method=\"" + "PLAINTEXT" + "\", " +
                "oauth_timestamp=\"" + System.currentTimeMillis() / 1000 + "\", " +
                "oauth_verifier=\"" + verifier + "\"";
    }

}
