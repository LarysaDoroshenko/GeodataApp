package petproject.geodata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import petproject.geodata.entity.PlaceEntity;

import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<PlaceEntity, Long> {
    
    Optional<PlaceEntity> findByLatitudeAndLongitude(Double latitude, Double longitude);

}
