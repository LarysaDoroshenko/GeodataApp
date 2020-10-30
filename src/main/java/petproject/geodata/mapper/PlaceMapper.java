package petproject.geodata.mapper;

import org.springframework.jdbc.core.RowMapper;
import petproject.geodata.domain.AddressEntity;
import petproject.geodata.domain.PlaceEntity;

import java.sql.ResultSet;
import java.sql.SQLException;


public class PlaceMapper implements RowMapper<PlaceEntity> {
    
    @Override
    public PlaceEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(resultSet.getLong("id"));
        addressEntity.setCity(resultSet.getString("city"));
        addressEntity.setPostcode(resultSet.getString("postcode"));
        addressEntity.setCountry(resultSet.getString("country"));
        
        PlaceEntity placeEntity = new PlaceEntity();
        placeEntity.setId(resultSet.getLong("id"));
        placeEntity.setLongitude(resultSet.getDouble("longitude"));
        placeEntity.setLatitude(resultSet.getDouble("latitude"));
        placeEntity.setPlaceId(resultSet.getString("place_id"));
        placeEntity.setDisplayName(resultSet.getString("display_name"));
        placeEntity.setElementType(resultSet.getString("element_type"));
        placeEntity.setOsmId(resultSet.getString("osm_id"));
        placeEntity.setOsmType(resultSet.getString("osm_type"));
        placeEntity.setAddressEntity(addressEntity);
        
        return placeEntity;
    }
}
