package petproject.geodata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petproject.geodata.domain.Place;
import petproject.geodata.dto.PlaceDto;
import petproject.geodata.mapper.PlaceMapper;
import petproject.geodata.repository.AddressRepository;
import petproject.geodata.repository.PlaceRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PlaceApiService placeApiService;
    @Autowired
    private PlaceMapper pLaceMapper;

    public List<PlaceDto> getAllPlaces() {
        List<Place> placeList = placeRepository.findAll();
        
        return placeList.stream()
                .map(place -> pLaceMapper.map(place))
                .collect(Collectors.toList());
    }

    public Optional<PlaceDto> findPlaceOrFindAndSaveIfNotYetSaved(Double latitude, Double longitude) throws JsonProcessingException {
        Optional<Place> byLatitudeAndLongitude = placeRepository.findByLatitudeAndLongitude(latitude, longitude);

        if (byLatitudeAndLongitude.isPresent()) {
            Place place = byLatitudeAndLongitude.get();
            PlaceDto placeDto = pLaceMapper.map(place);

            return Optional.of(placeDto);
        }
        return findAndSaveNewPlace(latitude, longitude);
    }

    private Optional<PlaceDto> findAndSaveNewPlace(Double latitude, Double longitude) throws JsonProcessingException {
        Optional<PlaceDto> optionalPlace = placeApiService.findPlace(latitude, longitude);

        if (optionalPlace.isPresent()) {
            PlaceDto placeDto = optionalPlace.get();
            placeDto.setLongitude(longitude);
            placeDto.setLatitude(latitude);

            Place place = pLaceMapper.map(placeDto);
            addressRepository.save(place.getAddress());
            placeRepository.save(place);

            return optionalPlace;
        }
        return Optional.empty();
    }

}
