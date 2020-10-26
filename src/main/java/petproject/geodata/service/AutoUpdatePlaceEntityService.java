package petproject.geodata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import petproject.geodata.dto.PlaceDto;
import petproject.geodata.repository.AddressRepository;
import petproject.geodata.repository.PlaceRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoUpdatePlaceEntityService {

    private static final int UPDATE_EVERY_12_HOURS = 12 * 60 * 60 * 1_000;

    private final PlaceRepository placeRepository;
    private final AddressRepository addressRepository;
    private final PlaceService placeService;

    @Scheduled(fixedRate = UPDATE_EVERY_12_HOURS)
    @Transactional
    public void refreshPlaceListEvery12Hours() {
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
        log.info("refreshPlaceListEvery12Hours()");
    }

}
