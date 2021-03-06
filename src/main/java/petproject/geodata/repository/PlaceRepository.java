package petproject.geodata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import petproject.geodata.entity.PlaceEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<PlaceEntity, Long> {
    
    Optional<PlaceEntity> findByLatitudeAndLongitude(Double latitude, Double longitude);

    @Query("select p from PlaceEntity p where p.longitude > 0 and p.latitude > 66.5622 and p.displayName <> 'Unknown place'")
    List<PlaceEntity> findPlacesOfEasternHemisphereBeyondTheArcticCircle();

    @Query(value = "select p from PlaceEntity p order by p.latitude")
    List<PlaceEntity> findAllOrderByLatitude();

    default Optional<PlaceEntity> findTheMostEasternPlace() {
        return findAllOrderByLatitude()
                .stream()
                .findFirst();
    }

    @Query(value = "select * from place p order by p.latitude desc offset ((select count(*) from place) - 1)", nativeQuery = true)
    Optional<PlaceEntity> findFirstOrderByLatitude();

}
