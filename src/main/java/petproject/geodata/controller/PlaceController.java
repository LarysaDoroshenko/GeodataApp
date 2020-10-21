package petproject.geodata.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import petproject.geodata.domain.Place;
import petproject.geodata.service.PlaceService;

import java.util.List;
import java.util.Optional;

@RestController
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @GetMapping
    String get() {
        return "Alive";
    }

    @GetMapping(value = "/place", produces = MediaType.APPLICATION_JSON_VALUE)
    Optional<Place> findPlace(@RequestParam(name = "lat") Double latitude, @RequestParam(name = "lon") Double longitude) throws JsonProcessingException {
        return placeService.findPlaceAndSave(latitude, longitude);
    }

    @GetMapping("/places")
    List<Place> getAllPlaces() {
        return placeService.getAllPlaces();
    }

    @GetMapping("/places/{country}")
    List<Place> getPlacesByCountry(@PathVariable String country) {
        return placeService.findByCountry(country);
    }

}
