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
import petproject.geodata.dto.PlaceDto;
import petproject.geodata.service.PlaceService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(PlaceController.class)
public class PlaceControllerTest {

    private static final double LONGITUDE = 30.5234;
    private static final double LATITUDE = 50.4501;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaceService placeService;

    private JacksonTester<List<PlaceDto>> listJacksonTester;
    private JacksonTester<PlaceDto> placeDtoJacksonTester;

    private PlaceDto placeDto;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        placeDto = new PlaceDto();
    }

    @Test
    public void getAllPlacesTest() throws Exception {
        List<PlaceDto> placeDtoList = Collections.singletonList(placeDto);

        // given
        given(placeService.getAllPlaces()).willReturn(placeDtoList);

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/place/list").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        String contentAsString = response.getContentAsString();
        assertThat(contentAsString).isEqualTo(listJacksonTester.write(placeDtoList).getJson());
    }

    @Test
    public void findPlaceAndSaveTest() throws Exception {
        // given
        given(placeService.findPlaceOrFindAndSaveIfNotYetSaved(LATITUDE, LONGITUDE)).willReturn(placeDto);

        // when
        String urlTemplate = String.format("/place?lon=%.4f&lat=%.4f", LONGITUDE, LATITUDE);
        MockHttpServletResponse response = mockMvc.perform(get(urlTemplate).accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        String contentAsString = response.getContentAsString();
        assertThat(contentAsString).isEqualTo(placeDtoJacksonTester.write(placeDto).getJson());
    }

    @Test
    public void returnListOfPlacesOfNorthernHemisphere() throws Exception {
        List<PlaceDto> placeDtoList = Collections.singletonList(placeDto);

        // given
        given(placeService.getPlacesOfNorthernHemisphere()).willReturn(placeDtoList);

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/place/list/north").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        String contentAsString = response.getContentAsString();
        assertThat(contentAsString).isEqualTo(listJacksonTester.write(placeDtoList).getJson());
    }

    @Test
    public void returnListOfPlacesOfSouthernHemisphere() throws Exception {
        List<PlaceDto> placeDtoList = Collections.singletonList(placeDto);

        // given
        given(placeService.getPlacesOfSouthernHemisphere()).willReturn(placeDtoList);

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/place/list/south").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        String contentAsString = response.getContentAsString();
        assertThat(contentAsString).isEqualTo(listJacksonTester.write(placeDtoList).getJson());
    }
    
}
