package petproject.geodata.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import petproject.geodata.dao.PlaceDao;
import petproject.geodata.domain.PlaceEntity;
import petproject.geodata.dto.PlaceDto;
import petproject.geodata.repository.AddressRepository;
import petproject.geodata.repository.PlaceRepository;
import petproject.geodata.service.PlaceApiService;
import petproject.geodata.service.PlaceService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final AddressRepository addressRepository;
    private final PlaceApiService placeApiService;
    private final ModelMapper modelMapper;
    private final PlaceDao placeDao;

    @Override
    public List<PlaceDto> getAllPlaces() {
        List<PlaceEntity> placeEntityList = placeRepository.findAll();

        return placeEntityList.stream()
                .map(placeEntity -> modelMapper.map(placeEntity, PlaceDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaceDto> getPlacesOfNorthernHemisphere() {
        List<PlaceEntity> placeEntityList = placeDao.getPlacesOfNorthernHemisphere();

        return placeEntityList.stream()
                .map(placeEntity -> modelMapper.map(placeEntity, PlaceDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaceDto> getPlacesOfSouthernHemisphere() {
        List<PlaceEntity> placeEntityList = placeDao.getPlacesOfSouthernHemisphere();

        return placeEntityList.stream()
                .map(placeEntity -> modelMapper.map(placeEntity, PlaceDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PlaceDto findPlaceOrFindAndSaveIfNotYetSaved(Double latitude, Double longitude) throws JsonProcessingException {
        Optional<PlaceEntity> byLatitudeAndLongitude = placeRepository.findByLatitudeAndLongitude(latitude, longitude);

        if (byLatitudeAndLongitude.isPresent()) {
            PlaceEntity placeEntity = byLatitudeAndLongitude.get();
            return modelMapper.map(placeEntity, PlaceDto.class);
        }
        return findAndSaveNewPlace(latitude, longitude);
    }

    private PlaceDto findAndSaveNewPlace(Double latitude, Double longitude) throws JsonProcessingException {
        Optional<PlaceDto> optionalPlace = placeApiService.findPlace(latitude, longitude);

        if (optionalPlace.isPresent()) {
            PlaceDto placeDto = optionalPlace.get();
            placeDto.setLongitude(longitude);
            placeDto.setLatitude(latitude);

            PlaceEntity placeEntity = modelMapper.map(placeDto, PlaceEntity.class);
            addressRepository.save(placeEntity.getAddressEntity());
            placeRepository.save(placeEntity);

            return optionalPlace.get();
        }
        return saveUnknownPlace(latitude, longitude);
    }

    private PlaceDto saveUnknownPlace(Double latitude, Double longitude) {
        PlaceDto placeDto = new PlaceDto();
        placeDto.setLongitude(longitude);
        placeDto.setLatitude(latitude);
        placeDto.setName("Unknown place");

        PlaceEntity placeEntity = modelMapper.map(placeDto, PlaceEntity.class);
        placeRepository.save(placeEntity);

        return placeDto;
    }

}
