package petproject.geodata.dao;

import petproject.geodata.domain.PlaceEntity;

import java.util.List;

public interface PlaceDao {
    
    List<PlaceEntity> getPlacesOfNorthernHemisphere();
    
    List<PlaceEntity> getPlacesOfSouthernHemisphere();
    
}
