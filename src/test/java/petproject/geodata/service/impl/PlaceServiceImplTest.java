package petproject.geodata.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import petproject.geodata.domain.PlaceEntity;
import petproject.geodata.dto.AddressDto;
import petproject.geodata.dto.PlaceDto;
import petproject.geodata.mapper.PlaceMapper;
import petproject.geodata.repository.AddressRepository;
import petproject.geodata.repository.PlaceRepository;
import petproject.geodata.service.PlaceApiService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PlaceServiceImplTest {

    private static final double LONGITUDE = 30.5234;
    private static final double LATITUDE = 50.4501;

    @Mock
    private AddressRepository addressRepository;
    @Mock
    private PlaceRepository placeRepository;
    @Mock
    private PlaceMapper placeMapper;
    @Mock
    private PlaceApiService placeApiService;

    @InjectMocks
    private PlaceServiceImpl placeServiceImpl;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllPlacesTest() {
        // given
        PlaceEntity placeEntity1 = new PlaceEntity();
        PlaceEntity placeEntity2 = new PlaceEntity();
        List<PlaceEntity> placeEntityList = Arrays.asList(placeEntity1, placeEntity2);

        given(placeRepository.findAll()).willReturn(placeEntityList);

        PlaceDto placeDto = new PlaceDto();
        PlaceDto placeDto2 = new PlaceDto();
        List<PlaceDto> placeDtoList = Arrays.asList(placeDto, placeDto2);

        given(placeMapper.toDto(placeEntity1)).willReturn(placeDto);
        given(placeMapper.toDto(placeEntity2)).willReturn(placeDto2);

        // when
        List<PlaceDto> allPlaces = placeServiceImpl.getAllPlaces();

        // then
        assertThat(allPlaces).isEqualTo(placeDtoList);
    }

    @Test
    public void findPlaceThatAlreadySavedTest() throws JsonProcessingException {
        // given
        PlaceEntity placeEntity = new PlaceEntity();

        given(placeRepository.findByLatitudeAndLongitude(LATITUDE, LONGITUDE)).willReturn(Optional.of(placeEntity));

        PlaceDto placeDto = new PlaceDto();

        given(placeMapper.toDto(placeEntity)).willReturn(placeDto);

        // when
        Optional<PlaceDto> placeOrFindAndSaveIfNotYetSaved = placeServiceImpl.findPlaceOrFindAndSaveIfNotYetSaved(LATITUDE, LONGITUDE);

        // then
        assertThat(placeOrFindAndSaveIfNotYetSaved).isEqualTo(Optional.of(placeDto));
    }

    @Test
    public void findAndSavePlaceThatNotYetSavedTest() throws JsonProcessingException {
        given(placeRepository.findByLatitudeAndLongitude(LATITUDE, LONGITUDE)).willReturn(Optional.empty());

        PlaceDto placeDto = new PlaceDto();
        placeDto.setLatitude(LATITUDE);
        placeDto.setLongitude(LONGITUDE);
        placeDto.setAddressDto(new AddressDto());

        given(placeApiService.findPlace(LATITUDE, LONGITUDE)).willReturn(Optional.of(placeDto));
        
        assertThat(placeDto.getLatitude()).isEqualTo(LATITUDE);
        assertThat(placeDto.getLongitude()).isEqualTo(LONGITUDE);

        PlaceEntity placeEntity = new PlaceEntity();

        given(placeMapper.toEntity(placeDto)).willReturn(placeEntity);

        Optional<PlaceDto> placeOrFindAndSaveIfNotYetSaved = placeServiceImpl.findPlaceOrFindAndSaveIfNotYetSaved(LATITUDE, LONGITUDE);

        verify(addressRepository).save(placeEntity.getAddressEntity());
        verify(placeRepository).save(placeEntity);
        
        assertThat(placeOrFindAndSaveIfNotYetSaved).isEqualTo(Optional.of(placeDto));
    }

    @Test
    public void returnOptionalEmptyIfPlaceNotSavedAndCanNotBeFound() throws JsonProcessingException {
        // given
        given(placeRepository.findByLatitudeAndLongitude(LATITUDE, LONGITUDE)).willReturn(Optional.empty());
        given(placeApiService.findPlace(LATITUDE, LONGITUDE)).willReturn(Optional.empty());

        // when
        Optional<PlaceDto> placeOrFindAndSaveIfNotYetSaved = placeServiceImpl.findPlaceOrFindAndSaveIfNotYetSaved(LATITUDE, LONGITUDE);

        // then
        assertThat(placeOrFindAndSaveIfNotYetSaved).isEmpty();
    }
}
