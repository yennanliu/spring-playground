package com.yen.SpotifyPlayList.controller;

import com.yen.SpotifyPlayList.model.dto.Response.RedirectResp;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

@Slf4j
@RestController
public class SpotifyOAuthController {

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    @Value("${spotify.redirect.url}")
    private String redirectURL;

    @Value("${spotify.authorize.scope}")
    private String authScope;

    @GetMapping("/authorize")
    public ResponseEntity authorize() {

        log.info("authorize start");
        URI uri = null;
        try {

            final URI redirectUri = SpotifyHttpManager
                    .makeUri(redirectURL);

            final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .setRedirectUri(redirectUri)
                    .build();
            final AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi
                    .authorizationCodeUri()
                    /**
                     *  Scope doc : https://developer.spotify.com/documentation/web-api/concepts/scopes
                     *  code ref : https://github.com/spotify-web-api-java/spotify-web-api-java/tree/master/examples
                     */
                    .scope(authScope)
                    .show_dialog(true)
                    .build();

            uri = authorizationCodeUriRequest.execute();

        } catch (Exception e) {
            log.error("authorize failed : " + e);
        }
        //return "redirect:" + uri.toString();
        log.info("redirect URL = " + uri.toString());
        RedirectResp redirectResp = new RedirectResp();
        redirectResp.setUrl(uri.toString());
        return ResponseEntity.status(HttpStatus.OK).body(redirectResp);
    }

    // This is your callback URL where Spotify will redirect the user after authorization
    @GetMapping("/callback")
    public String callback() {
        // Handle the callback logic here (e.g., exchange authorization code for access token)
        return "callback-page"; // Redirect to a page or do further processing
    }

    // get authorized result
    @GetMapping("/authorized-url")
    public String authorizedEndpoint(Authentication authentication) {
        log.info("authentication = " + authentication.toString());
        String username = authentication.getUsername();
        return "Hello, " + username + "! You have access to this authorized endpoint.";
    }

}

