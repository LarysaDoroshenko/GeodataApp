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

    @Query("select distinct a.country from AddressEntity a join PlaceEntity p on a.id = p.addressEntity.id and p.longitude < 0 and a.country in (:countries)")
    List<String> findCountriesOfWesternHemisphere(List<String> countries);

    @Query("select a.country from AddressEntity a group by a.country order by count(a.country) desc")
    List<String> findCountriesOrderByCount();

    default List<String> findTop2MostFrequentCountries() {
        return findCountriesOfWesternHemisphere(findCountriesOrderByCount())
                .stream()
                .limit(2)
                .collect(Collectors.toList()
                );
    }

}
