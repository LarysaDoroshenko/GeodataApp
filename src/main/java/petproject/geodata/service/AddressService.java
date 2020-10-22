package petproject.geodata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petproject.geodata.domain.Address;
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
        List<Address> addressList = addressRepository.findByCountry(country);
        
        return addressList.stream()
                .map(address -> addressMapper.map(address))
                .collect(Collectors.toList());
    }

}
