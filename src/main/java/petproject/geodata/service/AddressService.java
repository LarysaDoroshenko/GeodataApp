package petproject.geodata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petproject.geodata.domain.AddressEntity;
import petproject.geodata.dto.AddressDto;
import petproject.geodata.mapper.AddressMapper;
import petproject.geodata.repository.AddressRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressMapper addressMapper;

    public List<AddressDto> findByCountry(String country) {
        List<AddressEntity> addressEntityList = addressRepository.findByCountry(country);
        
        return addressEntityList.stream()
                .map(address -> addressMapper.map(address))
                .collect(Collectors.toList());
    }

}
