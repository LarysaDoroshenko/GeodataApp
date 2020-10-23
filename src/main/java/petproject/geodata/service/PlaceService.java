package petproject.geodata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import petproject.geodata.domain.PlaceEntity;
import petproject.geodata.dto.PlaceDto;
import petproject.geodata.mapper.PlaceMapper;
import petproject.geodata.repository.AddressRepository;
import petproject.geodata.repository.PlaceRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlaceService {

    public static final int UPDATE_EACH_12_HOURS = 12 * 60 * 60 * 1_000;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PlaceApiService placeApiService;
    @Autowired
    private PlaceMapper pLaceMapper;

    public List<PlaceDto> getAllPlaces() {
        List<PlaceEntity> placeEntityList = placeRepository.findAll();

        return placeEntityList.stream()
                .map(placeEntity -> pLaceMapper.map(placeEntity))
                .collect(Collectors.toList());
    }

    public Optional<PlaceDto> findPlaceOrFindAndSaveIfNotYetSaved(Double latitude, Double longitude) throws JsonProcessingException {
        Optional<PlaceEntity> byLatitudeAndLongitude = placeRepository.findByLatitudeAndLongitude(latitude, longitude);

        if (byLatitudeAndLongitude.isPresent()) {
            PlaceEntity placeEntity = byLatitudeAndLongitude.get();
            PlaceDto placeDto = pLaceMapper.map(placeEntity);

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

            PlaceEntity placeEntity = pLaceMapper.map(placeDto);
            addressRepository.save(placeEntity.getAddressEntity());
            placeRepository.save(placeEntity);

            return optionalPlace;
        }
        return Optional.empty();
    }

    @Scheduled(fixedRate = UPDATE_EACH_12_HOURS)
    private void test() {
        List<PlaceDto> placesInDb = getAllPlaces();

        placeRepository.deleteAll();
        addressRepository.deleteAll();

        placesInDb.forEach(place -> {
            try {
                findPlaceOrFindAndSaveIfNotYetSaved(place.getLatitude(), place.getLongitude());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        System.out.println("test");
    }

}
