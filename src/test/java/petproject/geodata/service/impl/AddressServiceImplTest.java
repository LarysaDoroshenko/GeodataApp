package petproject.geodata.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import petproject.geodata.domain.AddressEntity;
import petproject.geodata.dto.AddressDto;
import petproject.geodata.repository.AddressRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AddressServiceImpl addressServiceImpl;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByCountryTest() {
        // given
        AddressEntity addressEntity1 = new AddressEntity();
        AddressEntity addressEntity2 = new AddressEntity();
        List<AddressEntity> addressEntityList = Arrays.asList(addressEntity1, addressEntity2);

        given(addressRepository.findByCountry("Country")).willReturn(addressEntityList);

        AddressDto addressDto1 = new AddressDto();
        AddressDto addressDto2 = new AddressDto();
        List<AddressDto> addressDtoList = Arrays.asList(addressDto1, addressDto2);

        given(modelMapper.map(addressEntity1, AddressDto.class)).willReturn(addressDto1);
        given(modelMapper.map(addressEntity2, AddressDto.class)).willReturn(addressDto2);

        // when
        List<AddressDto> addressesByCountry = addressServiceImpl.findByCountry("Country");

        // then
        assertThat(addressesByCountry).isEqualTo(addressDtoList);
    }
}
