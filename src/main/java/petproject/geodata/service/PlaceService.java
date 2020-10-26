package petproject.geodata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import petproject.geodata.dto.PlaceDto;

import java.util.List;
import java.util.Optional;

public interface PlaceService {

    List<PlaceDto> getAllPlaces();

    Optional<PlaceDto> findPlaceOrFindAndSaveIfNotYetSaved(Double latitude, Double longitude) throws JsonProcessingException;

}
