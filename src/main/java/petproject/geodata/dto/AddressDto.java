package petproject.geodata.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AddressDto {

    @JsonAlias(value = "city")
    private String town;
    @JsonAlias(value = "postcode")
    private String postcode;
    @JsonAlias(value = "country")
    private String country;

}
