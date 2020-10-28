package petproject.geodata.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import petproject.geodata.domain.AddressEntity;
import petproject.geodata.domain.PlaceEntity;
import petproject.geodata.dto.AddressDto;
import petproject.geodata.dto.PlaceDto;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlaceMapper {

    private final ModelMapper modelMapper;
    private final AddressMapper addressMapper;

    public PlaceEntity toEntity(PlaceDto placeDto) {
        AddressDto addressDto = placeDto.getAddressDto();

        AddressEntity addressEntity = Objects.isNull(addressDto) ? null : addressMapper.toEntity(addressDto);
        PlaceEntity placeEntity = modelMapper.map(placeDto, PlaceEntity.class);
        placeEntity.setAddressEntity(addressEntity);

        return placeEntity;
    }

    public PlaceDto toDto(PlaceEntity placeEntity) {
        AddressEntity addressEntity = placeEntity.getAddressEntity();

        AddressDto addressDto = Objects.isNull(addressEntity) ? null : addressMapper.toDto(addressEntity);
        PlaceDto placeDto = modelMapper.map(placeEntity, PlaceDto.class);
        placeDto.setAddressDto(addressDto);

        return placeDto;
    }

}
