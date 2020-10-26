package petproject.geodata.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import petproject.geodata.dto.PlaceDto;
import petproject.geodata.service.PlaceService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<PlaceDto> findPlaceAndSave(@RequestParam(name = "lat") Double latitude,
                                               @RequestParam(name = "lon") Double longitude) throws JsonProcessingException {

        return placeService.findPlaceOrFindAndSaveIfNotYetSaved(latitude, longitude);
    }

    @GetMapping("/list")
    public List<PlaceDto> getAllPlaces() {
        return placeService.getAllPlaces();
    }

}
