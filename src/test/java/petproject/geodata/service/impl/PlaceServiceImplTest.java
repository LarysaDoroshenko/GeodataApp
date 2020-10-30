package petproject.geodata.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import petproject.geodata.dao.PlaceDao;
import petproject.geodata.domain.PlaceEntity;
import petproject.geodata.dto.AddressDto;
import petproject.geodata.dto.PlaceDto;
import petproject.geodata.repository.AddressRepository;
import petproject.geodata.repository.PlaceRepository;
import petproject.geodata.service.PlaceApiService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private ModelMapper modelMapper;
    @Mock
    private PlaceApiService placeApiService;
    @Mock
    private PlaceDao placeDao;

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

        PlaceDto placeDto1 = new PlaceDto();
        PlaceDto placeDto2 = new PlaceDto();
        List<PlaceDto> placeDtoList = Arrays.asList(placeDto1, placeDto2);

        given(modelMapper.map(placeEntity1, PlaceDto.class)).willReturn(placeDto1);
        given(modelMapper.map(placeEntity2, PlaceDto.class)).willReturn(placeDto2);

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

        given(modelMapper.map(placeEntity, PlaceDto.class)).willReturn(placeDto);

        // when
        PlaceDto placeOrFindAndSaveIfNotYetSaved = placeServiceImpl.findPlaceOrFindAndSaveIfNotYetSaved(LATITUDE, LONGITUDE);

        // then
        assertThat(placeOrFindAndSaveIfNotYetSaved).isEqualTo(placeDto);
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

        given(modelMapper.map(placeDto, PlaceEntity.class)).willReturn(placeEntity);

        PlaceDto placeOrFindAndSaveIfNotYetSaved = placeServiceImpl.findPlaceOrFindAndSaveIfNotYetSaved(LATITUDE, LONGITUDE);

        verify(addressRepository).save(placeEntity.getAddressEntity());
        verify(placeRepository).save(placeEntity);
        
        assertThat(placeOrFindAndSaveIfNotYetSaved).isEqualTo(placeDto);
    }

    @Test
    public void returnUnknownPlaceIfPlaceNotSavedAndCanNotBeFound() throws JsonProcessingException {
        given(placeRepository.findByLatitudeAndLongitude(LATITUDE, LONGITUDE)).willReturn(Optional.empty());
        given(placeApiService.findPlace(LATITUDE, LONGITUDE)).willReturn(Optional.empty());

        PlaceDto placeDto = new PlaceDto();
        placeDto.setLatitude(LATITUDE);
        placeDto.setLongitude(LONGITUDE);
        placeDto.setName("Unknown place");

        assertThat(placeDto.getLatitude()).isEqualTo(LATITUDE);
        assertThat(placeDto.getLongitude()).isEqualTo(LONGITUDE);
        assertThat(placeDto.getName()).isEqualTo("Unknown place");

        PlaceEntity placeEntity = new PlaceEntity();

        given(modelMapper.map(placeDto, PlaceEntity.class)).willReturn(placeEntity);

        PlaceDto placeOrFindAndSaveIfNotYetSaved = placeServiceImpl.findPlaceOrFindAndSaveIfNotYetSaved(LATITUDE, LONGITUDE);

        verify(placeRepository).save(placeEntity);

        assertThat(placeOrFindAndSaveIfNotYetSaved).isEqualTo(placeDto);
    }

    @Test
    public void shouldThrowOriginalExceptionWhenPlaceApiServiceThrowsException() throws JsonProcessingException {
        // given
        given(placeRepository.findByLatitudeAndLongitude(LATITUDE, LONGITUDE)).willReturn(Optional.empty());
        given(placeApiService.findPlace(LATITUDE, LONGITUDE)).willThrow(JsonProcessingException.class);

        // then
        assertThatThrownBy(() -> placeServiceImpl.findPlaceOrFindAndSaveIfNotYetSaved(LATITUDE, LONGITUDE))
                .isInstanceOf(JsonProcessingException.class)
                .hasMessage("N/A");
    }

    @Test
    public void returnListOfPlacesOfNorthernHemisphere() {
        // given
        PlaceEntity placeEntity1 = new PlaceEntity();
        PlaceEntity placeEntity2 = new PlaceEntity();
        List<PlaceEntity> placeEntityList = Arrays.asList(placeEntity1, placeEntity2);

        given(placeDao.getPlacesOfNorthernHemisphere()).willReturn(placeEntityList);

        PlaceDto placeDto1 = new PlaceDto();
        PlaceDto placeDto2 = new PlaceDto();
        List<PlaceDto> placeDtoList = Arrays.asList(placeDto1, placeDto2);

        given(modelMapper.map(placeEntity1, PlaceDto.class)).willReturn(placeDto1);
        given(modelMapper.map(placeEntity2, PlaceDto.class)).willReturn(placeDto2);

        // when
        List<PlaceDto> northernPlaces = placeServiceImpl.getPlacesOfNorthernHemisphere();

        // then
        assertThat(northernPlaces).isEqualTo(placeDtoList);
    }

    @Test
    public void returnListOfPlacesOfSouthernHemisphere() {
        // given
        PlaceEntity placeEntity1 = new PlaceEntity();
        PlaceEntity placeEntity2 = new PlaceEntity();
        List<PlaceEntity> placeEntityList = Arrays.asList(placeEntity1, placeEntity2);

        given(placeDao.getPlacesOfSouthernHemisphere()).willReturn(placeEntityList);

        PlaceDto placeDto1 = new PlaceDto();
        PlaceDto placeDto2 = new PlaceDto();
        List<PlaceDto> placeDtoList = Arrays.asList(placeDto1, placeDto2);

        given(modelMapper.map(placeEntity1, PlaceDto.class)).willReturn(placeDto1);
        given(modelMapper.map(placeEntity2, PlaceDto.class)).willReturn(placeDto2);

        // when
        List<PlaceDto> allPlaces = placeServiceImpl.getPlacesOfSouthernHemisphere();

        // then
        assertThat(allPlaces).isEqualTo(placeDtoList);
    }
    
}
