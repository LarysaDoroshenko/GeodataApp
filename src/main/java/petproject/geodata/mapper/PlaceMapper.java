package petproject.geodata.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petproject.geodata.domain.AddressEntity;
import petproject.geodata.domain.PlaceEntity;
import petproject.geodata.dto.AddressDto;
import petproject.geodata.dto.PlaceDto;

@Service
public class PlaceMapper {

    @Autowired
    private AddressMapper addressMapper;

    public PlaceEntity map(PlaceDto placeDto) {
        PlaceEntity placeEntity = new PlaceEntity();
        placeEntity.setLatitude(placeDto.getLatitude());
        placeEntity.setLongitude(placeDto.getLongitude());
        placeEntity.setPlaceId(placeDto.getPlaceId());
        placeEntity.setDisplayName(placeDto.getDisplayName());
        placeEntity.setElementType(placeDto.getElementType());
        placeEntity.setOsmId(placeDto.getOsmId());
        placeEntity.setOsmType(placeDto.getOsmType());

        AddressEntity addressEntity = addressMapper.map(placeDto.getAddressDto());
        placeEntity.setAddressEntity(addressEntity);

        return placeEntity;
    }

    public PlaceDto map(PlaceEntity placeEntity) {
        PlaceDto placeDto = new PlaceDto();
        placeDto.setLatitude(placeEntity.getLatitude());
        placeDto.setLongitude(placeEntity.getLongitude());
        placeDto.setPlaceId(placeEntity.getPlaceId());
        placeDto.setDisplayName(placeEntity.getDisplayName());
        placeDto.setElementType(placeEntity.getElementType());
        placeDto.setOsmId(placeEntity.getOsmId());
        placeDto.setOsmType(placeEntity.getOsmType());

        AddressDto addressDto = addressMapper.map(placeEntity.getAddressEntity());
        placeDto.setAddressDto(addressDto);

        return placeDto;
    }

}
