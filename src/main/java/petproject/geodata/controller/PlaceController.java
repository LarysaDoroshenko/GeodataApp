package petproject.geodata.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import petproject.geodata.domain.Place;
import petproject.geodata.service.PlaceService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/place")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    Optional<Place> findPlaceAndSave(@RequestParam(name = "lat") Double latitude, @RequestParam(name = "lon") Double longitude) throws JsonProcessingException {
        return placeService.findPlaceOrFindAndSaveIfNotYetSaved(latitude, longitude);
    }

    @GetMapping("/list")
    List<Place> getAllPlaces() {
        return placeService.getAllPlaces();
    }

}
