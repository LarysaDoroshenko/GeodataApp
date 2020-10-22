package petproject.geodata.mapper;

import org.springframework.stereotype.Service;
import petproject.geodata.domain.AddressEntity;
import petproject.geodata.dto.AddressDto;

@Service
public class AddressMapper {

    public AddressEntity map(AddressDto addressDto) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setCity(addressDto.getCity());
        addressEntity.setCountry(addressDto.getCountry());
        addressEntity.setPostcode(addressDto.getPostcode());
        return addressEntity;
    }

    public AddressDto map(AddressEntity addressEntity) {
        AddressDto addressDto = new AddressDto();
        addressDto.setCity(addressEntity.getCity());
        addressDto.setCountry(addressEntity.getCountry());
        addressDto.setPostcode(addressEntity.getPostcode());
        return addressDto;
    }

}
