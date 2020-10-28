package petproject.geodata.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import petproject.geodata.dto.ErrorResponse;
import petproject.geodata.dto.PlaceDto;
import petproject.geodata.service.PlaceService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping(value = "/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<PlaceDto> findPlaceAndSave(@RequestParam(name = "lat") @Min(value = -180) @Max(value = 180) Double latitude,
                                               @RequestParam(name = "lon") @Min(value = -90) @Max(value = 90) Double longitude)
            throws JsonProcessingException {

        return placeService.findPlaceOrFindAndSaveIfNotYetSaved(latitude, longitude);
    }

    @GetMapping("/list")
    public List<PlaceDto> getAllPlaces() {
        return placeService.getAllPlaces();
    }
    
    @ExceptionHandler
    public ErrorResponse handle(Exception ex) {
        return new ErrorResponse(ex.getMessage());
    }

}
