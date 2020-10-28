package petproject.geodata.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import petproject.geodata.dto.PlaceDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PlaceApiServiceTest {

    private static final String URL_TEMPLATE = "https://nominatim.openstreetmap.org/reverse?format=geojson&lat=%s&lon=%s";
    private static final double LONGITUDE = 30.5234;
    private static final double LATITUDE = 50.4501;

    private String url;

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private JsonNode jsonNode;
    @Mock
    private JsonNode errorJsonNode;
    @Mock
    private JsonNode featuresNode;
    @Mock
    private JsonNode firstFeature;
    @Mock
    private JsonNode firstFeatureProperties;

    @InjectMocks
    private PlaceApiServiceImpl placeApiServiceImpl;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        url = String.format(URL_TEMPLATE, LATITUDE, LONGITUDE);
    }

    @Test
    public void shouldReturnOptionalEmptyIfNoResponse() throws JsonProcessingException {
        // given
        given(restTemplate.getForObject(url, String.class)).willReturn(null);

        // when
        Optional<PlaceDto> place = placeApiServiceImpl.findPlace(LATITUDE, LONGITUDE);

        // then
        assertThat(place).isEmpty();
    }

    @Test
    public void shouldReturnOptionalEmptyWhenJsonNodeHasErrorAttribute() throws JsonProcessingException {
        // given
        String response = "{response}";
        given(restTemplate.getForObject(url, String.class)).willReturn(response);
        given(objectMapper.readTree(response)).willReturn(jsonNode);
        given(jsonNode.get("error")).willReturn(errorJsonNode);

        // when
        Optional<PlaceDto> place = placeApiServiceImpl.findPlace(LATITUDE, LONGITUDE);

        // then
        assertThat(place).isEmpty();
    }

    @Test
    public void shouldReturnPlaceDtoWhenJsonNodeIsValid() throws JsonProcessingException {
        // given
        String response = "{response}";
        given(restTemplate.getForObject(url, String.class)).willReturn(response);
        given(objectMapper.readTree(response)).willReturn(jsonNode);
        given(jsonNode.get("features")).willReturn(featuresNode);
        given(featuresNode.get(0)).willReturn(firstFeature);
        given(firstFeature.get("properties")).willReturn(firstFeatureProperties);
        String details = "details";
        given(firstFeatureProperties.toString()).willReturn(details);
        PlaceDto placeDto = new PlaceDto();
        given(objectMapper.readValue(details, PlaceDto.class)).willReturn(placeDto);

        // when
        Optional<PlaceDto> place = placeApiServiceImpl.findPlace(LATITUDE, LONGITUDE);

        // then
        assertThat(place).contains(placeDto);
    }

    @Test(expected = JsonProcessingException.class)
    public void shouldThrowOriginalExceptionWhenObjectMapperThrowsException() throws JsonProcessingException {
        // given
        String response = "{response}";
        given(restTemplate.getForObject(url, String.class)).willReturn(response);
        given(objectMapper.readTree(response)).willThrow(JsonProcessingException.class);

        // when 
        placeApiServiceImpl.findPlace(LATITUDE, LONGITUDE);
    }
    
}
