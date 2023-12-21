package info.wade.users.controller;

import info.wade.users.service.DiscogsService;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/discogs")
public class DiscogsController {

    @Autowired
    DiscogsService discogsService;

    @GetMapping(value = "/request_token")
    public ResponseEntity<?> getDiscogsRequestToken() throws Exception {

        String authenticationUrl = discogsService.getAuthenticationUrl();
        if(authenticationUrl != null) {
            return new ResponseEntity<>(authenticationUrl, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping(value = "/access_token")
    public ResponseEntity<?> getDiscogsAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestParam("oauth_verifier") String verifier) throws Exception {

        String token = discogsService.getAccessToken(verifier, auth);
        if (token != null) {
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {

            return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
