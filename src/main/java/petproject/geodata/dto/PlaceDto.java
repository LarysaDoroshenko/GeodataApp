package petproject.geodata.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PlaceDto {
    
    private Double longitude;
    private Double latitude;

    @JsonAlias(value = "display_name")
    private String name;
    @JsonAlias(value = "type")
    private String elementType;
    @JsonAlias(value = "osm_id")
    private String osmId;
    @JsonAlias(value = "osm_type")
    private String osmType;
    @JsonAlias(value = "address")
    private AddressDto addressDto;

}
