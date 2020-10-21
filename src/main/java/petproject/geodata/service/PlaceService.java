package petproject.geodata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import petproject.geodata.domain.Place;
import petproject.geodata.repository.AddressRepository;
import petproject.geodata.repository.PlaceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {

    private static final String URL_TEMPLATE = "https://nominatim.openstreetmap.org/reverse?format=geojson&lat=%s&lon=%s";

    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private RestTemplate restTemplate;

    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    public List<Place> findByCountry(String country) {
        return placeRepository.findByAddressCountry(country);
    }

    public Optional<Place> findPlaceAndSave(Double latitude, Double longitude) throws JsonProcessingException {
        Optional<Place> optionalPlace = findPlace(latitude, longitude);

        if (optionalPlace.isPresent()) {
            Place place = optionalPlace.get();
            place.setLongitude(longitude);
            place.setLatitude(latitude);
            System.out.println(optionalPlace);

            addressRepository.save(place.getAddress());
            placeRepository.save(place);

            return optionalPlace;
        }

        return Optional.empty();
    }

    private Optional<Place> findPlace(Double latitude, Double longitude) throws JsonProcessingException {
        String url = String.format(URL_TEMPLATE, latitude, longitude);
        String response = restTemplate.getForObject(url, String.class);

        if (response == null) {
            return Optional.empty();
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);
        
        if (jsonNode.get("error") != null) {
            return Optional.empty();
        }

        JsonNode featuresNode = jsonNode.get("features");
        JsonNode firstFeature = featuresNode.get(0);
        JsonNode firstFeatureProperties = firstFeature.get("properties");
        String details = firstFeatureProperties.toString();
        
        Place place = mapper.readValue(details, Place.class);

        return Optional.of(place);
    }

}
