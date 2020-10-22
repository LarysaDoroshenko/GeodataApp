package petproject.geodata.mapper;

import org.springframework.stereotype.Service;
import petproject.geodata.domain.Address;
import petproject.geodata.dto.AddressDto;

@Service
public class AddressMapper {

    public Address map(AddressDto addressDto) {
        Address address = new Address();
        address.setCity(addressDto.getCity());
        address.setCountry(addressDto.getCountry());
        address.setPostcode(addressDto.getPostcode());
        return address;
    }

    public AddressDto map(Address address) {
        AddressDto addressDto = new AddressDto();
        addressDto.setCity(address.getCity());
        addressDto.setCountry(address.getCountry());
        addressDto.setPostcode(address.getPostcode());
        return addressDto;
    }

}
