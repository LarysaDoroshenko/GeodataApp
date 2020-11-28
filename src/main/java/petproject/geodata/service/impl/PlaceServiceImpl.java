package petproject.geodata.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import petproject.geodata.dao.PlaceDao;
import petproject.geodata.dto.PlaceDto;
import petproject.geodata.entity.PlaceEntity;
import petproject.geodata.repository.AddressRepository;
import petproject.geodata.repository.PlaceRepository;
import petproject.geodata.service.PlaceApiService;
import petproject.geodata.service.PlaceService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    public static final String SRC_MAIN_RESOURCES_XML = "src/main/resources/xml";
    public static final String XML = ".xml";
    private final PlaceRepository placeRepository;
    private final AddressRepository addressRepository;
    private final PlaceApiService placeApiService;
    private final ModelMapper modelMapper;
    private final PlaceDao placeDao;
    private final XmlMapper xmlMapper;

    @Override
    public List<PlaceDto> getAllPlaces() {
        return getFromSupplierAndMapToPlaceDto(placeRepository::findAll);
    }

    @Override
    public List<PlaceDto> getPlacesOfNorthernHemisphere() {
        return getFromSupplierAndMapToPlaceDto(placeDao::getPlacesOfNorthernHemisphere);
    }

    @Override
    public List<PlaceDto> getPlacesOfSouthernHemisphere() {
        return getFromSupplierAndMapToPlaceDto(placeDao::getPlacesOfSouthernHemisphere);
    }

    @Override
    public List<PlaceDto> getPlacesOfEasternHemisphereBeyondTheArcticCircle() {
        return getFromSupplierAndMapToPlaceDto(placeRepository::findPlacesOfEasternHemisphereBeyondTheArcticCircle);
    }

    @Override
    public void getTheMostEasternPlaceAndSaveItToXml() throws IOException {
        Optional<PlaceEntity> theMostEasternPlace = placeRepository.findTheMostEasternPlace();

        if (theMostEasternPlace.isPresent()) {
            xmlMapper.writeValue(new File("src/main/resources/xml/the_most_eastern_place.xml"), theMostEasternPlace.get());
        }
    }

    @Override
    public void getTheMostEasternPlaceAndSaveItToXmlVersion2() throws IOException {
        Optional<PlaceEntity> theMostEasternPlace = placeRepository.findFirstOrderByLatitude();

        if (theMostEasternPlace.isPresent()) {
            xmlMapper.writeValue(new File("src/main/resources/xml/the_most_eastern_place.xml"), theMostEasternPlace.get());
        }
    }

    @Override
    public List<PlaceDto> getAllPlacesFromXmlFile() throws IOException {
        File fileDirectory = new File(SRC_MAIN_RESOURCES_XML);

        List<PlaceDto> placeDtoList = new ArrayList<>();
        if (fileDirectory.exists()) {
            String[] fileList = fileDirectory.list();

            if (fileList != null) {
                for (String file : fileList) {
                    if (file.endsWith(XML)) {
                        String readContent = new String(Files.readAllBytes(Paths.get(SRC_MAIN_RESOURCES_XML + "/" + file)));
                        PlaceDto placeDto = xmlMapper.readValue(readContent, PlaceDto.class);
                        placeDtoList.add(placeDto);
                        log.info(placeDto.toString());
                    }
                }
            } else {
                log.info("There are no files in the directory {}", SRC_MAIN_RESOURCES_XML);
            }
        } else {
            log.info("There is no such directory: {}", SRC_MAIN_RESOURCES_XML);
        }
        return placeDtoList;
    }

    private List<PlaceDto> getFromSupplierAndMapToPlaceDto(Supplier<List<PlaceEntity>> supplier) {
        return supplier.get()
                .stream()
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
