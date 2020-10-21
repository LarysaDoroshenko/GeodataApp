package petproject.geodata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petproject.geodata.domain.Address;
import petproject.geodata.repository.AddressRepository;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public List<Address> findByCountry(String country) {
        return addressRepository.findByCountry(country);
    }

}
