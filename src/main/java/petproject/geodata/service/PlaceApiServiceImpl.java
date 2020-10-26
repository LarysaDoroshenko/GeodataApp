package petproject.geodata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import petproject.geodata.dto.PlaceDto;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceApiServiceImpl implements PlaceApiService {

    private static final String URL_TEMPLATE = "https://nominatim.openstreetmap.org/reverse?format=geojson&lat=%s&lon=%s";

    private final RestTemplate restTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Optional<PlaceDto> findPlace(Double latitude, Double longitude) throws JsonProcessingException {
        String url = String.format(URL_TEMPLATE, latitude, longitude);
        String response = restTemplate.getForObject(url, String.class);

        if (response == null) {
            return Optional.empty();
        }

        JsonNode jsonNode = objectMapper.readTree(response);

        if (jsonNode.get("error") != null) {
            return Optional.empty();
        }

        JsonNode featuresNode = jsonNode.get("features");
        JsonNode firstFeature = featuresNode.get(0);
        JsonNode firstFeatureProperties = firstFeature.get("properties");
        String details = firstFeatureProperties.toString();

        PlaceDto place = objectMapper.readValue(details, PlaceDto.class);

        return Optional.of(place);
    }

}
