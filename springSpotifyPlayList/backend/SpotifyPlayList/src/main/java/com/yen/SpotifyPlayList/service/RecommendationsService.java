package com.yen.SpotifyPlayList.service;

import com.yen.SpotifyPlayList.model.dto.GetRecommendationsDto;
import com.yen.SpotifyPlayList.model.dto.GetRecommendationsWithFeatureDto;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.AudioFeatures;
import se.michaelthelin.spotify.model_objects.specification.Recommendations;
import se.michaelthelin.spotify.requests.data.browse.GetRecommendationsRequest;

@Service
@Slf4j
public class RecommendationsService {

  @Autowired private AuthService authService;

  @Autowired private PlayListService playListService;

  private SpotifyApi spotifyApi;

  public RecommendationsService() {}

  public Recommendations getRecommendation(GetRecommendationsDto getRecommendationsDto)
      throws SpotifyWebApiException {
    try {
      this.spotifyApi = authService.initializeSpotifyApi();
      GetRecommendationsRequest getRecommendationsRequest =
          prepareRecommendationsRequest(getRecommendationsDto);
      Recommendations recommendations = getRecommendationsRequest.execute();
      log.info("Fetched recommendations: {}", recommendations);
      return recommendations;
    } catch (IOException | SpotifyWebApiException | ParseException e) {
      log.error("Error fetching recommendations: {}", e.getMessage());
      throw new SpotifyWebApiException("getRecommendation error: " + e.getMessage());
    }
  }

  public Recommendations getRecommendationWithPlayList(String playListId)
      throws SpotifyWebApiException {
    try {

      this.spotifyApi = authService.initializeSpotifyApi();
      List<AudioFeatures> audioFeaturesList = playListService.getSongFeatureByPlayList(playListId);
      log.debug(">>> audioFeaturesList = " + audioFeaturesList);

      // Use functional programming to calculate the averages and cast Double to float
      // TODO : modify GetRecommendationsWithFeatureDto with attr as "float" type, and modify below
      // code (e.g. : averagingDouble)
      double energy =
          audioFeaturesList.stream().collect(Collectors.averagingDouble(AudioFeatures::getEnergy));
      double acousticness =
          audioFeaturesList.stream()
              .collect(Collectors.averagingDouble(AudioFeatures::getAcousticness));
      double danceability =
          audioFeaturesList.stream()
              .collect(Collectors.averagingDouble(AudioFeatures::getDanceability));
      double liveness =
          audioFeaturesList.stream()
              .collect(Collectors.averagingDouble(AudioFeatures::getLiveness));
      double loudness =
          audioFeaturesList.stream()
              .collect(Collectors.averagingDouble(AudioFeatures::getLoudness));
      double speechiness =
          audioFeaturesList.stream()
              .collect(Collectors.averagingDouble(AudioFeatures::getSpeechiness));

      GetRecommendationsWithFeatureDto featureDto = new GetRecommendationsWithFeatureDto();
      featureDto.setEnergy(energy);
      // featureDto.setAcousticness(acousticness);
      featureDto.setDanceability(danceability);
      featureDto.setLiveness(liveness);
      featureDto.setLoudness(loudness);
      featureDto.setSpeechiness(speechiness);

      GetRecommendationsRequest getRecommendationsRequest =
          prepareRecommendationsRequestWithPlayList(featureDto);
      Recommendations recommendations = getRecommendationsRequest.execute();

      return recommendations;
    } catch (Exception e) {
      log.error("Error fetching recommendations with playlist features: {}", e.getMessage());
      throw new SpotifyWebApiException("getRecommendationWithPlayList error: " + e.getMessage());
    }
  }

  private GetRecommendationsRequest prepareRecommendationsRequestWithPlayList(
      GetRecommendationsWithFeatureDto featureDto)
      throws IOException, SpotifyWebApiException, ParseException {
    return spotifyApi
        .getRecommendations()
        .limit(featureDto.getAmount())
        .market(featureDto.getMarket())
        .max_popularity(featureDto.getMaxPopularity())
        .min_popularity(featureDto.getMinPopularity())
        .seed_artists(featureDto.getSeedArtistId())
        .seed_genres(featureDto.getSeedGenres())
        .seed_tracks(featureDto.getSeedTrack())
        // TODO : undo float cast once modify GetRecommendationsWithFeatureDto with attr as "float"
        // type
        .target_danceability((float) featureDto.getDanceability())
        .target_energy((float) featureDto.getEnergy())
        .target_instrumentalness((float) featureDto.getInstrumentalness())
        .target_liveness((float) featureDto.getLiveness())
        .target_loudness((float) featureDto.getLoudness())
        .target_speechiness((float) featureDto.getSpeechiness())
        .build();
  }

  private GetRecommendationsRequest prepareRecommendationsRequest(GetRecommendationsDto dto) {
    return spotifyApi
        .getRecommendations()
        .limit(dto.getAmount())
        .market(dto.getMarket())
        .max_popularity(dto.getMaxPopularity())
        .min_popularity(dto.getMinPopularity())
        .seed_artists(dto.getSeedArtistId())
        .seed_genres(dto.getSeedGenres())
        .seed_tracks(dto.getSeedTrack())
        .target_popularity(dto.getTargetPopularity())
        .build();
  }

}
