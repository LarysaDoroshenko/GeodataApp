package petproject.geodata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import petproject.geodata.entity.AddressEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    List<AddressEntity> findByCountry(String country);

    @Query(value = "select distinct a.country from address a join place p on a.id = p.address_entity_id and p.longitude < 0 and a.country in " +
            "(select a.country from address a group by a.country order by count(a.country) desc)", 
    nativeQuery = true)
    List<String> findCountriesOfWesternHemisphereOrderByCount();

    default List<String> findTop2MostFrequentCountries() {
        return findCountriesOfWesternHemisphereOrderByCount()
                .stream()
                .limit(2)
                .collect(Collectors.toList()
                );
    }

}
