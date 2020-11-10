package petproject.geodata.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import petproject.geodata.dto.AddressDto;
import petproject.geodata.service.AddressService;

import java.util.List;

@RestController
@RequestMapping(value = "/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/{country}")
    public List<AddressDto> getPlacesByCountry(@PathVariable String country) {
        return addressService.findByCountry(country);
    }

    @GetMapping("/top2")
    public List<String> getTop2OfWesternHemisphere() {
        return addressService.getTop2OfWesternHemisphere();
    }

}
