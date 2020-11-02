package petproject.geodata.dao.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import petproject.geodata.domain.AddressEntity;
import petproject.geodata.domain.PlaceEntity;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PlaceDaoImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private PlaceDaoImpl.PlaceMapper placeMapper;

    @InjectMocks
    private PlaceDaoImpl placeDaoImpl;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void returnListOfPlacesOfNorthernHemisphere() {
        String query = "select * from place p left outer join address a on p.address_entity_id = a.id where p.latitude > 0";

        AddressEntity addressEntity = new AddressEntity();
        PlaceEntity placeEntity = new PlaceEntity();
        placeEntity.setAddressEntity(addressEntity);

        List<PlaceEntity> expectedList = Collections.singletonList(placeEntity);

        // given
        given(jdbcTemplate.query(query, placeMapper)).willReturn(expectedList);

        // when
        List<PlaceEntity> placesOfNorthernHemisphere = placeDaoImpl.getPlacesOfNorthernHemisphere();

        // then
        assertThat(placesOfNorthernHemisphere).isEqualTo(expectedList);
    }

    @Test
    public void returnListOfPlacesOfSouthernHemisphere() {
        String query = "select * from place p left outer join address a on p.address_entity_id = a.id where p.latitude < 0";

        AddressEntity addressEntity = new AddressEntity();
        PlaceEntity placeEntity = new PlaceEntity();
        placeEntity.setAddressEntity(addressEntity);

        List<PlaceEntity> expectedList = Collections.singletonList(placeEntity);

        // given
        given(jdbcTemplate.query(query, placeMapper)).willReturn(expectedList);

        // when
        List<PlaceEntity> placesOfSouthernHemisphere = placeDaoImpl.getPlacesOfSouthernHemisphere();

        // then
        assertThat(placesOfSouthernHemisphere).isEqualTo(expectedList);
    }

}
