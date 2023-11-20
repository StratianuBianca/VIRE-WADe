package info.wade.users.controller;

import info.wade.users.service.SpotifyService;
import info.wade.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.SavedAlbum;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.library.GetCurrentUsersSavedAlbumsRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import se.michaelthelin.spotify.model_objects.specification.User;

import java.net.URI;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyController {

    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String spotifyLogin() {
        SpotifyApi object = spotifyService.getSpotifyObject();

        AuthorizationCodeUriRequest authorizationCodeUriRequest = object.authorizationCodeUri()
                .scope("user-library-read")
                .show_dialog(true)
                .build();

        final URI uri = authorizationCodeUriRequest.execute();
        return uri.toString();
    }

    @GetMapping("/getToken")
    public void getUserSpotifyToken(@RequestParam("code") String token){
        SpotifyApi object = spotifyService.getSpotifyObject();
        AuthorizationCodeRequest authorizationCodeRequest = object.authorizationCode(token).build();
        User user = null;
        try {
            final AuthorizationCodeCredentials authorizationCode = authorizationCodeRequest.execute();

            object.setAccessToken(authorizationCode.getAccessToken());
            object.setRefreshToken(authorizationCode.getRefreshToken());

            final GetCurrentUsersProfileRequest getCurrentUsersProfile = object.getCurrentUsersProfile().build();
            user = getCurrentUsersProfile.execute();
            userService.setSpotifyToken(Long.valueOf(user.getId()), authorizationCode.getAccessToken(), authorizationCode.getRefreshToken());

        } catch (Exception e) {
            System.out.println("Exception occured while getting user code: " + e);
        }
    }

    @GetMapping(value = "/userTopSongs")
    public Track[] getUserTopTracks(@RequestParam Long userId) {
        info.wade.users.entity.User user = userService.getUserById(userId);

        SpotifyApi object = spotifyService.getSpotifyObject();
        object.setAccessToken(user.getSpotifyToken());
        object.setRefreshToken(user.getRefreshToken());

        final GetUsersTopTracksRequest getUsersTopTracksRequest = object.getUsersTopTracks()
                .time_range("medium_term")
                .limit(10)
                .offset(0)
                .build();

        try {
            final Paging<Track> trackPaging = getUsersTopTracksRequest.execute();

            return trackPaging.getItems();
        } catch (Exception e) {
            System.out.println("Exception occured while fetching top songs: " + e);
        }

        return new Track[0];
    }

    @GetMapping(value = "/userSavedAlbum")
    public SavedAlbum[] getCurrentUserSavedAlbum(@RequestParam Long userId) {
        info.wade.users.entity.User user = userService.getUserById(userId);

        SpotifyApi object = spotifyService.getSpotifyObject();
        object.setAccessToken(user.getSpotifyToken());
        object.setRefreshToken(user.getRefreshToken());

        final GetCurrentUsersSavedAlbumsRequest getUsersTopArtistsRequest = object.getCurrentUsersSavedAlbums()
                .limit(50)
                .offset(0)
                .build();

        try {
            final Paging<SavedAlbum> artistPaging = getUsersTopArtistsRequest.execute();

            return artistPaging.getItems();
        } catch (Exception e) {
            System.out.println("Exception occured while fetching user saved album: " + e);
        }

        return new SavedAlbum[0];
    }
}
