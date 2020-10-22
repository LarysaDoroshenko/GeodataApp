package petproject.geodata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petproject.geodata.domain.Place;
import petproject.geodata.repository.AddressRepository;
import petproject.geodata.repository.PlaceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PlaceApiService placeApiService;

    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    public Optional<Place> findPlaceOrFindAndSaveIfNotYetSaved(Double latitude, Double longitude) throws JsonProcessingException {
        Optional<Place> byLatitudeAndLongitude = placeRepository.findByLatitudeAndLongitude(latitude, longitude);

        if (byLatitudeAndLongitude.isPresent()) {
            return byLatitudeAndLongitude;
        }
        return findAndSaveNewPlace(latitude, longitude);
    }

    private Optional<Place> findAndSaveNewPlace(Double latitude, Double longitude) throws JsonProcessingException {
        Optional<Place> optionalPlace = placeApiService.findPlace(latitude, longitude);

        if (optionalPlace.isPresent()) {
            Place place = optionalPlace.get();
            place.setLongitude(longitude);
            place.setLatitude(latitude);

            addressRepository.save(place.getAddress());
            placeRepository.save(place);

            return optionalPlace;
        }
        return Optional.empty();
    }

}
