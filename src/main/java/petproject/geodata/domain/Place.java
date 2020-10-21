package petproject.geodata.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "place")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double longitude;
    private Double latitude;

    @JsonAlias(value = "place_id")
    private String placeId;
    @JsonAlias(value = "display_name")
    private String displayName;
    @JsonAlias(value = "type")
    private String elementType;
    @JsonAlias(value = "osm_id")
    private String osmId;
    @JsonAlias(value = "osm_type")
    private String osmType;

    @OneToOne
    private Address address;

}
