package petproject.geodata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import petproject.geodata.dto.PlaceDto;
import petproject.geodata.repository.AddressRepository;
import petproject.geodata.repository.PlaceRepository;
import petproject.geodata.service.impl.PlaceServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class AutoUpdatePlaceEntityServiceTest {

    private static final double LONGITUDE1 = 1;
    private static final double LATITUDE1 = 2;
    private static final double LONGITUDE2 = 3;
    private static final double LATITUDE2 = 4;

    @Mock
    private AddressRepository addressRepository;
    @Mock
    private PlaceRepository placeRepository;
    @Mock
    private PlaceServiceImpl placeServiceImpl;

    @InjectMocks
    private AutoUpdatePlaceEntityService autoUpdatePlaceEntityService;

    @Test
    public void refreshPlaceListEvery12HoursTest() throws JsonProcessingException {
        // given
        PlaceDto placeDto1 = new PlaceDto();
        placeDto1.setLatitude(LATITUDE1);
        placeDto1.setLongitude(LONGITUDE1);
        PlaceDto placeDto2 = new PlaceDto();
        placeDto2.setLatitude(LATITUDE2);
        placeDto2.setLongitude(LONGITUDE2);
        List<PlaceDto> placeDtoList = Arrays.asList(placeDto1, placeDto2);
        
        given(placeServiceImpl.getAllPlaces()).willReturn(placeDtoList);
        
        // when
        autoUpdatePlaceEntityService.refreshPlaceListEvery12Hours();
        
        // then
        verify(placeRepository).deleteAll();
        verify(addressRepository).deleteAll();
        
        verify(placeServiceImpl).findPlaceOrFindAndSaveIfNotYetSaved(placeDto1.getLatitude(), placeDto1.getLongitude());
        verify(placeServiceImpl).findPlaceOrFindAndSaveIfNotYetSaved(placeDto2.getLatitude(), placeDto2.getLongitude());
    }

    @Test
    public void shouldThrowOriginalExceptionWhenPlaceServiceThrowsException() throws JsonProcessingException {
        // given
        PlaceDto placeDto1 = new PlaceDto();
        placeDto1.setLatitude(LATITUDE1);
        placeDto1.setLongitude(LONGITUDE1);
        PlaceDto placeDto2 = new PlaceDto();
        placeDto2.setLatitude(LATITUDE2);
        placeDto2.setLongitude(LONGITUDE2);
        List<PlaceDto> placeDtoList = Arrays.asList(placeDto1, placeDto2);

        given(placeServiceImpl.getAllPlaces()).willReturn(placeDtoList);
        given(placeServiceImpl.findPlaceOrFindAndSaveIfNotYetSaved(LATITUDE1, LONGITUDE1)).willThrow(JsonProcessingException.class);
        
        // when
        autoUpdatePlaceEntityService.refreshPlaceListEvery12Hours();
        
        // then
        verify(placeRepository).deleteAll();
        verify(addressRepository).deleteAll();
        verify(placeServiceImpl).findPlaceOrFindAndSaveIfNotYetSaved(LATITUDE1, LONGITUDE1);
        verify(placeServiceImpl).findPlaceOrFindAndSaveIfNotYetSaved(LATITUDE2, LONGITUDE2);
    }

}
