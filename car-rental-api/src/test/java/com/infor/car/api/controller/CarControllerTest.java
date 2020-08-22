package com.infor.car.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.infor.car.api.dto.CarDto;
import com.infor.car.api.exception.ErrorMessage;
import com.infor.car.api.model.Car;
import com.infor.car.api.service.CarService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(CarController.class)
public class CarControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Value("${api.url.template}")
    private String apiUrlBase;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carServiceMock;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private ObjectWriter objectWriter;

    private List<Car> initCars;

    @Before
    public void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

        initCars = List.of(
                new Car("NPS151", "BMW", 2018, new BigDecimal("90")),
                new Car("TQS121", "AUDI", 2017, new BigDecimal("80")),
                new Car("MUV171", "MAZDA", 2016, new BigDecimal("70")),
                new Car("HQP191", "VOLVO", 2020, new BigDecimal("120"))
        );
    }

    @Test
    public void findAll() throws Exception {
        when(carServiceMock.findAll()).thenReturn(initCars);

        var resultActions = mockMvc.perform(get(apiUrlBase + "/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.[0].plateNum").value("NPS151"))
                .andExpect(jsonPath("$.[0].model").value("BMW"))
                .andExpect(jsonPath("$.[0].year").value("2018"))
                .andExpect(jsonPath("$.[0].price").value("90"));
    }

    @Test
    public void findCustomerByPlateNum() throws Exception {
        when(carServiceMock.findByPlateNum(any())).thenReturn(initCars.get(1));

        var resultActions = mockMvc.perform(get(apiUrlBase + "/cars/plate/TQS121")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.model").value("AUDI"))
                .andExpect(jsonPath("$.year").value("2017"))
                .andExpect(jsonPath("$.price").value("80"));
    }

    @Test
    public void findCustomerByModel() throws Exception {
        when(carServiceMock.findByModel(any())).thenReturn(initCars);

        var resultActions = mockMvc.perform(get(apiUrlBase + "/cars/model/MAZDA")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.[2].plateNum").value("MUV171"))
                .andExpect(jsonPath("$.[2].year").value("2016"))
                .andExpect(jsonPath("$.[2].price").value("70"));
    }

    @Test
    public void findCustomerByPrice() throws Exception {
        when(carServiceMock.findByPrice(any())).thenReturn(initCars);

        var resultActions = mockMvc.perform(get(apiUrlBase + "/cars/price/90")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.[1].plateNum").value("TQS121"))
                .andExpect(jsonPath("$.[1].model").value("AUDI"))
                .andExpect(jsonPath("$.[1].year").value("2017"));
    }

    @Test
    public void findAvailableCars() throws Exception {
        when(carServiceMock.findAvailableCars(any(), any(), any())).thenReturn(initCars);

        var resultActions = mockMvc.perform(get(apiUrlBase + "/cars/available/?price=90&startDateTime=20200729160000&endDateTime=20200820070000")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.[0].plateNum").value("NPS151"))
                .andExpect(jsonPath("$.[0].model").value("BMW"))
                .andExpect(jsonPath("$.[0].year").value("2018"))
                .andExpect(jsonPath("$.[0].price").value("90"));
    }

    @Test
    public void create() throws Exception {
        Car car = new Car("GVD157", "KIA", 2020, new BigDecimal("100"));
        when(carServiceMock.create(any())).thenReturn(car);

        CarDto carDto = new CarDto();
        carDto.setPlateNum("GVD157");
        carDto.setModel("KIA");
        carDto.setYear(2020);
        carDto.setPrice(new BigDecimal("100"));

        String requestJson= objectWriter.writeValueAsString(carDto);

        var resultActions = mockMvc.perform(post(apiUrlBase + "/cars").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isCreated());

        resultActions.andExpect(jsonPath("$.plateNum").value("GVD157"))
                .andExpect(jsonPath("$.model").value("KIA"))
                .andExpect(jsonPath("$.year").value("2020"))
                .andExpect(jsonPath("$.price").value("100"));
    }

    @Test
    public void findCarByPrice_INVALID_ARGUMENT() throws Exception {
        when(carServiceMock.findByPrice(any())).thenReturn(initCars);

        var result = mockMvc.perform(get(apiUrlBase + "/cars/price/invalid")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", is("INVALID_ARGUMENT")));

        ErrorMessage errorMessage = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ErrorMessage.class);
        assertTrue(errorMessage.getMessage().contains("Failed to convert value of type 'java.lang.String' to required type 'java.math.BigDecimal'"));
    }

    @Test
    public void create_RUNTIME_ERROR() throws Exception {
        Car car = new Car("HBP105", "NISSAN", 2019, new BigDecimal("90"));
        when(carServiceMock.create(any())).thenReturn(car);

        var result = mockMvc.perform(post(apiUrlBase + "/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is("RUNTIME_ERROR")));

        ErrorMessage errorMessage = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ErrorMessage.class);
        assertTrue(errorMessage.getMessage().contains("Required request body is missing"));
    }
}
