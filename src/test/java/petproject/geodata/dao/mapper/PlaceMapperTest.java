package petproject.geodata.dao.mapper;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import petproject.geodata.dao.impl.PlaceDaoImpl;
import petproject.geodata.entity.AddressEntity;
import petproject.geodata.entity.PlaceEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class PlaceMapperTest {

    public static final Long ADDRESS_ENTITY_ID = 1234L;
    public static final String CITY = "Cape Town";
    public static final String POSTCODE = "8001";
    public static final String COUNTRY = "South Africa";
    public static final Long PLACE_ENTITY_ID = 1234L;
    public static final Double LONGITUDE = 18.303;
    public static final Double LATITUDE = -33.889;
    public static final String PLACE_ID = "207323";
    public static final String DISPLAY_NAME = "Cape Town, City of Cape Town, Western Cape, 8001, South Africa";
    public static final String ELEMENT_TYPE = "city";
    public static final String OSM_ID = "32675806";
    public static final String OSM_TYPE = "node";

    private PlaceDaoImpl.PlaceMapper placeMapper = new PlaceDaoImpl.PlaceMapper();

    @Mock
    private ResultSet resultSet;

    @Test
    public void returnPlaceEntityWithMappedFieldsWhenUsedPlaceMapper() throws SQLException {
        // given
        when(resultSet.getLong("address_entity_id")).thenReturn(ADDRESS_ENTITY_ID);
        when(resultSet.getString("city")).thenReturn(CITY);
        when(resultSet.getString("postcode")).thenReturn(POSTCODE);
        when(resultSet.getString("country")).thenReturn(COUNTRY);

        when(resultSet.getLong("id")).thenReturn(PLACE_ENTITY_ID);
        when(resultSet.getDouble("longitude")).thenReturn(LONGITUDE);
        when(resultSet.getDouble("latitude")).thenReturn(LATITUDE);
        when(resultSet.getString("place_id")).thenReturn(PLACE_ID);
        when(resultSet.getString("display_name")).thenReturn(DISPLAY_NAME);
        when(resultSet.getString("element_type")).thenReturn(ELEMENT_TYPE);
        when(resultSet.getString("osm_id")).thenReturn(OSM_ID);
        when(resultSet.getString("osm_type")).thenReturn(OSM_TYPE);

        // when
        PlaceEntity placeEntity = placeMapper.mapRow(resultSet, 0);
        AddressEntity addressEntity = placeEntity.getAddressEntity();

        // then
        assertEquals(ADDRESS_ENTITY_ID, addressEntity.getId());
        assertEquals(CITY, addressEntity.getCity());
        assertEquals(POSTCODE, addressEntity.getPostcode());
        assertEquals(COUNTRY, addressEntity.getCountry());

        assertEquals(PLACE_ENTITY_ID, placeEntity.getId());
        assertEquals(LONGITUDE, placeEntity.getLongitude());
        assertEquals(LATITUDE, placeEntity.getLatitude());
        assertEquals(PLACE_ID, placeEntity.getPlaceId());
        assertEquals(DISPLAY_NAME, placeEntity.getDisplayName());
        assertEquals(ELEMENT_TYPE, placeEntity.getElementType());
        assertEquals(OSM_ID, placeEntity.getOsmId());
        assertEquals(OSM_TYPE, placeEntity.getOsmType());
    }

}
