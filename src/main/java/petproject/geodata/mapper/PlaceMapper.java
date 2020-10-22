package petproject.geodata.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petproject.geodata.domain.Address;
import petproject.geodata.domain.Place;
import petproject.geodata.dto.AddressDto;
import petproject.geodata.dto.PlaceDto;

@Service
public class PlaceMapper {

    @Autowired
    private AddressMapper addressMapper;

    public Place map(PlaceDto placeDto) {
        Place place = new Place();
        place.setLatitude(placeDto.getLatitude());
        place.setLongitude(placeDto.getLongitude());
        place.setPlaceId(placeDto.getPlaceId());
        place.setDisplayName(placeDto.getDisplayName());
        place.setElementType(placeDto.getElementType());
        place.setOsmId(placeDto.getOsmId());
        place.setOsmType(placeDto.getOsmType());

        Address address = addressMapper.map(placeDto.getAddressDto());
        place.setAddress(address);

        return place;
    }

    public PlaceDto map(Place place) {
        PlaceDto placeDto = new PlaceDto();
        placeDto.setLatitude(place.getLatitude());
        placeDto.setLongitude(place.getLongitude());
        placeDto.setPlaceId(place.getPlaceId());
        placeDto.setDisplayName(place.getDisplayName());
        placeDto.setElementType(place.getElementType());
        placeDto.setOsmId(place.getOsmId());
        placeDto.setOsmType(place.getOsmType());

        AddressDto addressDto = addressMapper.map(place.getAddress());
        placeDto.setAddressDto(addressDto);

        return placeDto;
    }

}
