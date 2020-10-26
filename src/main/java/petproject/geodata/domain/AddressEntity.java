package petproject.geodata.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "address")
@Data
public class AddressEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String postcode;
    private String country;

}
