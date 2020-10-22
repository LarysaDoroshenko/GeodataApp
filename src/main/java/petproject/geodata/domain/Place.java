package petproject.geodata.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "place", uniqueConstraints = {@UniqueConstraint(columnNames = {"longitude", "latitude"})})
@Data
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double longitude;
    private Double latitude;

    private String placeId;
    private String displayName;
    private String elementType;
    private String osmId;
    private String osmType;

    @OneToOne
    private Address address;

}
