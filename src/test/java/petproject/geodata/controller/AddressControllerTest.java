package petproject.geodata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import petproject.geodata.dto.AddressDto;
import petproject.geodata.service.AddressService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(AddressController.class)
public class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    private JacksonTester<List<AddressDto>> jacksonTester;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void returnListOfPlacesByCountry() throws Exception {
        AddressDto addressDto = new AddressDto();
        addressDto.setTown("New York");
        addressDto.setPostcode("1001");
        addressDto.setCountry("USA");
        List<AddressDto> addressDtoList = Collections.singletonList(addressDto);

        // given
        given(addressService.findByCountry("USA")).willReturn(addressDtoList);

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/address/USA").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        String contentAsString = response.getContentAsString();
        assertThat(contentAsString).isEqualTo(jacksonTester.write(addressDtoList).getJson());
    }

}
