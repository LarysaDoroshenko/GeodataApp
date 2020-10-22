package petproject.geodata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import petproject.geodata.dto.AddressDto;
import petproject.geodata.service.AddressService;

import java.util.List;

@RestController
@RequestMapping(value = "/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/{country}")
    List<AddressDto> getPlacesByCountry(@PathVariable String country) {
        return addressService.findByCountry(country);
    }

}
