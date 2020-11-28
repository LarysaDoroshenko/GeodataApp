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
import petproject.geodata.vo.ErrorResponseVo;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.List;

@Validated
@RestController
@RequestMapping(value = "/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlaceDto> findPlaceAndSave(@RequestParam(name = "lat") @Min(value = -90) @Max(value = 90) Double latitude,
                                                     @RequestParam(name = "lon") @Min(value = -180) @Max(value = 180) Double longitude)
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

    @GetMapping("/list/east")
    public List<PlaceDto> getPlacesOfEasternHemisphereBeyondTheArcticCircle() {
        return placeService.getPlacesOfEasternHemisphereBeyondTheArcticCircle();
    }

    @PostMapping("/eastern/place/to/xml")
    @ResponseStatus(HttpStatus.OK)
    public void getTheMostEasternPlaceAndSaveItToXml() throws IOException {
        placeService.getTheMostEasternPlaceAndSaveItToXml();
    }

    @PostMapping("/eastern/place/to/xml/v2")
    @ResponseStatus(HttpStatus.OK)
    public void getTheMostEasternPlaceAndSaveItToXmlVersion2() throws IOException {
        placeService.getTheMostEasternPlaceAndSaveItToXmlVersion2();
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseVo> handle(ConstraintViolationException ex) {
        ErrorResponseVo errorResponseVo = new ErrorResponseVo("There is mistake in coordinates. " + ex.getMessage());
        return new ResponseEntity<>(errorResponseVo, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseVo> handle(JsonProcessingException ex) {
        ErrorResponseVo errorResponseVo = new ErrorResponseVo("JsonProcessingException: " + ex.getMessage());
        return new ResponseEntity<>(errorResponseVo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseVo> handle(Exception ex) {
        ErrorResponseVo errorResponseVo = new ErrorResponseVo("Exception occurred: " + ex.getMessage());
        return new ResponseEntity<>(errorResponseVo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
