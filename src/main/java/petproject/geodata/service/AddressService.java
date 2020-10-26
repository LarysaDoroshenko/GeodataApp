package petproject.geodata.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import petproject.geodata.domain.AddressEntity;
import petproject.geodata.dto.AddressDto;
import petproject.geodata.mapper.AddressMapper;
import petproject.geodata.repository.AddressRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public List<AddressDto> findByCountry(String country) {
        List<AddressEntity> addressEntityList = addressRepository.findByCountry(country);
        
        return addressEntityList.stream()
                .map(addressMapper::map)
                .collect(Collectors.toList());
    }

}
