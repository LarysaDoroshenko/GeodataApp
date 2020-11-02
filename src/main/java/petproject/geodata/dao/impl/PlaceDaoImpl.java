package petproject.geodata.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import petproject.geodata.dao.PlaceDao;
import petproject.geodata.domain.AddressEntity;
import petproject.geodata.domain.PlaceEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlaceDaoImpl implements PlaceDao {
    
    private final JdbcTemplate jdbcTemplate;
    private final PlaceMapper placeMapper;
    
    private String queryTemplate = "select * from place p left outer join address a on p.address_entity_id = a.id where p.latitude %s 0";

    @Override
    public List<PlaceEntity> getPlacesOfNorthernHemisphere() {
        String placesOfNorthernHemisphere = String.format(queryTemplate, ">");
        return jdbcTemplate.query(placesOfNorthernHemisphere, placeMapper);
    }
    
    @Override
    public List<PlaceEntity> getPlacesOfSouthernHemisphere() {
        String placesOfSouthernHemisphere = String.format(queryTemplate, "<");
        return jdbcTemplate.query(placesOfSouthernHemisphere, placeMapper);
    }

    @Component
    private static class PlaceMapper implements RowMapper<PlaceEntity> {

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

}
