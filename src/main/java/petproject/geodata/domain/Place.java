package petproject.geodata.domain;

import javax.persistence.*;

@Entity
@Table(name = "place")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double longitude;
    private Double latitude;

    private String country;
    private String displayName;
    private String elementType;
    private String osmId;
    private String osmType;

}
