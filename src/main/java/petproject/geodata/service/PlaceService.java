package petproject.geodata.service;

import fr.dudie.nominatim.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petproject.geodata.domain.Place;
import petproject.geodata.mapper.AddressToPlaceMapper;
import petproject.geodata.repository.PlaceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private AddressToPlaceMapper addressToPlaceMapper;

    // use api to extract address
    public Optional<Place> findPlaceAndSave(Double longitude, Double latitude) {
        Optional<Address> address = findAddress(longitude, latitude);
        if (address.isPresent()) {
            Place place = addressToPlaceMapper.map(address.get());
            placeRepository.save(place);
            return Optional.of(place);
        }
        return Optional.empty();
    }

    private Optional<Address> findAddress(Double longitude, Double latitude) {
        return Optional.empty();
    }

    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    public List<Place> findByCountry(String country) {
        return placeRepository.findByCountry(country);
    }
    
}
