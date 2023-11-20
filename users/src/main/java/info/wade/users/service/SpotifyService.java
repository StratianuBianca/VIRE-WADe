package info.wade.users.service;

import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;

@Service
public class SpotifyService {
    private final String clientID = "e973f45bf14d4f269e5919eb053e706f";
    private final String clientSecret = "3b3fff73c3d94fc3b9014dfa92f3475a";

    public SpotifyApi getSpotifyObject() {
        return new SpotifyApi
                .Builder()
                .setClientId(clientID)
                .setClientSecret(clientSecret)
                .setRedirectUri(SpotifyHttpManager.makeUri("http://localhost:8090/api/hello"))
                .build();
    }


}
