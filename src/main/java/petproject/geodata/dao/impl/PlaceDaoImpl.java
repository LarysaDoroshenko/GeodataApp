package petproject.geodata.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import petproject.geodata.dao.PlaceDao;
import petproject.geodata.domain.PlaceEntity;
import petproject.geodata.mapper.PlaceMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PlaceDaoImpl implements PlaceDao {
    
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<PlaceEntity> getPlacesOfNorthernHemisphere() {
        String placesOfNorthernHemisphere = "select * from place p left outer join address a on p.address_entity_id = a.id where p.latitude >= 0";
        return jdbcTemplate.query(placesOfNorthernHemisphere, new PlaceMapper());
    }
    
    @Override
    public List<PlaceEntity> getPlacesOfSouthernHemisphere() {
        String placesOfSouthernHemisphere = "select * from place p left outer join address a on p.address_entity_id = a.id where p.latitude <= 0";
        return jdbcTemplate.query(placesOfSouthernHemisphere, new PlaceMapper());
    }

}
