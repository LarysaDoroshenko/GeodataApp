package petproject.geodata.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/place")
    Optional<Place> findPlace(@RequestParam Double longitude, @RequestParam Double latitude) {
        return placeService.findPlaceAndSave(longitude, latitude);
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
