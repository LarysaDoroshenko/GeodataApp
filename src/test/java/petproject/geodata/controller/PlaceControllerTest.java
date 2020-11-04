package petproject.geodata.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import petproject.geodata.vo.ErrorResponseVo;

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
    private static final double ERROR_LONGITUDE = 100.5234;
    private static final double ERROR_LATITUDE = 100.4501;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaceService placeService;

    private JacksonTester<List<PlaceDto>> listJacksonTester;
    private JacksonTester<PlaceDto> placeDtoJacksonTester;
    private JacksonTester<ErrorResponseVo> errorResponseVoJacksonTester;

    private PlaceDto placeDto;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        placeDto = new PlaceDto();
    }

    @Test
    public void returnListOfAllPlaces() throws Exception {
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
    public void returnPlaceAndSaveIt() throws Exception {
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

    @Test
    public void shouldHandleExceptionIfParamsAreOutOfLimits() throws Exception {
        // given
        ErrorResponseVo errorResponseVo = new ErrorResponseVo("There is mistake in coordinates. findPlaceAndSave.longitude: must be less than or equal to 90");
        
        // when
        String urlTemplate = String.format("/place?lon=%.4f&lat=%.4f", ERROR_LONGITUDE, ERROR_LATITUDE);
        MockHttpServletResponse response = mockMvc.perform(get(urlTemplate).accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        assertThat(response.getContentAsString()).isEqualTo(errorResponseVoJacksonTester.write(errorResponseVo).getJson());
    }

    @Test
    public void shouldHandleOriginalJsonProcessingExceptionWhenPlaceServiceThrowsJsonProcessingException() throws Exception {
        // given
        ErrorResponseVo errorResponseVo = new ErrorResponseVo("JsonProcessingException: N/A");
        given(placeService.findPlaceOrFindAndSaveIfNotYetSaved(LATITUDE, LONGITUDE)).willThrow(JsonProcessingException.class);

        // when
        String urlTemplate = String.format("/place?lon=%.4f&lat=%.4f", LONGITUDE, LATITUDE);
        MockHttpServletResponse response = mockMvc.perform(get(urlTemplate).accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isEqualTo(errorResponseVoJacksonTester.write(errorResponseVo).getJson());
    }

    @Test
    public void shouldHandleOriginalExceptionWhenPlaceServiceThrowsException() throws Exception {
        // given
        RuntimeException runtimeException = new RuntimeException("RuntimeException");
        ErrorResponseVo errorResponseVo = new ErrorResponseVo("Exception occurred: RuntimeException");
        given(placeService.findPlaceOrFindAndSaveIfNotYetSaved(LATITUDE, LONGITUDE)).willThrow(runtimeException);

        // when
        String urlTemplate = String.format("/place?lon=%.4f&lat=%.4f", LONGITUDE, LATITUDE);
        MockHttpServletResponse response = mockMvc.perform(get(urlTemplate).accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo(errorResponseVoJacksonTester.write(errorResponseVo).getJson());
    }

}
