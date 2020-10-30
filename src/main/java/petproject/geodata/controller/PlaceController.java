package petproject.geodata.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import petproject.geodata.dto.PlaceDto;
import petproject.geodata.service.PlaceService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@RequestMapping(value = "/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlaceDto> findPlaceAndSave(@RequestParam(name = "lat") @Min(value = -180) @Max(value = 180) Double latitude,
                                                     @RequestParam(name = "lon") @Min(value = -90) @Max(value = 90) Double longitude)
            throws JsonProcessingException {

        return ResponseEntity.ok(placeService.findPlaceOrFindAndSaveIfNotYetSaved(latitude, longitude));
    }

    @GetMapping("/list")
    public List<PlaceDto> getAllPlaces() {
        return placeService.getAllPlaces();
    }

    @GetMapping("/list/north")
    public List<PlaceDto> getPlacesOfNorthernHemisphere() {
        return placeService.getPlacesOfNorthernHemisphere();
    }

    @GetMapping("/list/south")
    public List<PlaceDto> getPlacesOfSouthernHemisphere() {
        return placeService.getPlacesOfSouthernHemisphere();
    }
    
    @ExceptionHandler
    public ResponseEntity<PlaceDto> handle(Exception ex) {
        PlaceDto placeDto = new PlaceDto();
        placeDto.setName("There is mistake in coordinates. " + ex.getMessage());
        return new ResponseEntity<>(placeDto, HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
