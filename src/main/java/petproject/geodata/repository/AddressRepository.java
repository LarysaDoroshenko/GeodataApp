package petproject.geodata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import petproject.geodata.entity.AddressEntity;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    List<AddressEntity> findByCountry(String country);

}
