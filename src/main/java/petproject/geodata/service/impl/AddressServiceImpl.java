package petproject.geodata.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import petproject.geodata.domain.AddressEntity;
import petproject.geodata.dto.AddressDto;
import petproject.geodata.repository.AddressRepository;
import petproject.geodata.service.AddressService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<AddressDto> findByCountry(String country) {
        List<AddressEntity> addressEntityList = addressRepository.findByCountry(country);

        return addressEntityList.stream()
                .map(addressEntity -> modelMapper.map(addressEntity, AddressDto.class))
                .collect(Collectors.toList());
    }

}
