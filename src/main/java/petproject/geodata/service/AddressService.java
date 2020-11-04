package petproject.geodata.service;

import petproject.geodata.dto.AddressDto;

import java.util.List;

public interface AddressService {

    List<AddressDto> findByCountry(String country);

}
