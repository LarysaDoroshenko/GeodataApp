package petproject.geodata.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AddressDto {

    @JsonAlias(value = "city")
    private String town;
    @JsonAlias(value = "postcode")
    private String postcode;
    @JsonAlias(value = "country")
    private String country;

}
