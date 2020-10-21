package petproject.geodata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import petproject.geodata.domain.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
