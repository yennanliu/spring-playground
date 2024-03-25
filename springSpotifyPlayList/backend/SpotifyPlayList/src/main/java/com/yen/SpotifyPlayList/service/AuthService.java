package com.yen.SpotifyPlayList.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.io.IOException;

@Service
@Slf4j
public class AuthService {

    @Value("${spotify.clientId}")
    private String clientId;

    @Value("${spotify.clientSecret}")
    private String clientSecret;

    public AuthService(){
    }

    public String getToken(){

        log.info(">>> getToken ...");

        String token = "";

        final SpotifyApi spotifyApi = new SpotifyApi
                .Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();

        final ClientCredentialsRequest clientCredentialsRequest = spotifyApi
                .clientCredentials()
                .build();

        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            token = clientCredentials.getAccessToken();
            log.info(">>> token = " + token);

            // Set access token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            log.info("Auth OK !!");
            log.info(">>> Expires in: " + clientCredentials.getExpiresIn());

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error(">>> Error: " + e.getMessage());
        }
        return token;
    }

}
