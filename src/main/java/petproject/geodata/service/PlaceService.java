package petproject.geodata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import petproject.geodata.dto.PlaceDto;

import java.util.List;

public interface PlaceService {

    List<PlaceDto> getAllPlaces();

    PlaceDto findPlaceOrFindAndSaveIfNotYetSaved(Double latitude, Double longitude) throws JsonProcessingException;

    List<PlaceDto> getPlacesOfNorthernHemisphere();

    List<PlaceDto> getPlacesOfSouthernHemisphere();

    List<PlaceDto> getPlacesOfEasternHemisphereBeyondTheArcticCircle();

}
