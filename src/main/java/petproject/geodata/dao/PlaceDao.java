package petproject.geodata.dao;

import petproject.geodata.entity.PlaceEntity;

import java.util.List;

public interface PlaceDao {
    
    List<PlaceEntity> getPlacesOfNorthernHemisphere();
    
    List<PlaceEntity> getPlacesOfSouthernHemisphere();
    
}
