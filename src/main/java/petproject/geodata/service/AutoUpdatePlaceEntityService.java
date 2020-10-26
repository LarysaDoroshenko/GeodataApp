package petproject.geodata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import petproject.geodata.dto.PlaceDto;
import petproject.geodata.repository.AddressRepository;
import petproject.geodata.repository.PlaceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutoUpdatePlaceEntityService {

    private static final int UPDATE_EACH_12_HOURS = 12 * 60 * 60 * 1_000;
    
    private static Logger logger = LoggerFactory.getLogger(AutoUpdatePlaceEntityService.class);

    private final PlaceRepository placeRepository;
    private final AddressRepository addressRepository;
    private final PlaceService placeService;

    @Scheduled(fixedRate = UPDATE_EACH_12_HOURS)
    private void refreshPlaceListEvery12Hours() {
        List<PlaceDto> placesInDb = placeService.getAllPlaces();

        placeRepository.deleteAll();
        addressRepository.deleteAll();

        placesInDb.forEach(place -> {
            try {
                placeService.findPlaceOrFindAndSaveIfNotYetSaved(place.getLatitude(), place.getLongitude());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        
        logger.info("12 hours passed. The update of the Place table is done.");
    }

}
