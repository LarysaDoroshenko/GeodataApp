package petproject.geodata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import petproject.geodata.dto.PlaceDto;

import java.util.Optional;

public interface PlaceApiService {

    Optional<PlaceDto> findPlace(Double latitude, Double longitude) throws JsonProcessingException;

}
