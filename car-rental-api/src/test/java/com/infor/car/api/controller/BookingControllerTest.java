package com.infor.car.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.infor.car.api.App;
import com.infor.car.api.AppConstants;
import com.infor.car.api.dto.BookingDto;
import com.infor.car.api.exception.ErrorMessage;
import com.infor.car.api.model.Booking;
import com.infor.car.api.model.Car;
import com.infor.car.api.model.Customer;
import com.infor.car.api.service.BookingService;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.Matchers.hasEntry;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingControllerTest.class);

    @Value("${api.url.template}")
    private String apiUrlBase;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingServiceMock;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private ObjectWriter objectWriter;

    private List<BookingDto> initBookingsDto;


    @Before
    public void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

        initBookingsDto = List.of(
                new BookingDto(2L, "MUV171", LocalDateTime.parse("2020-08-07 08:00:00", AppConstants.getFormatter()), LocalDateTime.parse("2020-08-11 08:00:00", AppConstants.getFormatter())),
                new BookingDto(1L, "TQS121", LocalDateTime.parse("2020-08-01 15:00:00", AppConstants.getFormatter()), LocalDateTime.parse("2020-08-10 11:00:00", AppConstants.getFormatter()))
        );
    }

    @Test
    public void findAll() throws Exception {
        when(bookingServiceMock.findAll()).thenReturn(initBookingsDto);

        var resultActions = mockMvc.perform(get(apiUrlBase + "/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.[0].customerId").value(2))
                .andExpect(jsonPath("$.[0].plateNum").value("MUV171"))
                .andExpect(jsonPath("$.[0].fromDateTime").value("2020-08-07T08:00:00"))
                .andExpect(jsonPath("$.[0].toDateTime").value("2020-08-11T08:00:00"));
    }

    @Test
    public void findById() throws Exception {
        when(bookingServiceMock.findById(any())).thenReturn(initBookingsDto.get(1));

        var resultActions = mockMvc.perform(get(apiUrlBase + "/bookings/id/2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.plateNum").value("TQS121"))
                .andExpect(jsonPath("$.fromDateTime").value("2020-08-01T15:00:00"))
                .andExpect(jsonPath("$.toDateTime").value("2020-08-10T11:00:00"));
    }

    @Test
    public void findAllBookingsByCustomerId() throws Exception {
        when(bookingServiceMock.findAllBookingsByCustomerId(any())).thenReturn(initBookingsDto);

        var resultActions = mockMvc.perform(get(apiUrlBase + "/bookings/customer/2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.[0].customerId").value(2))
                .andExpect(jsonPath("$.[0].plateNum").value("MUV171"))
                .andExpect(jsonPath("$.[0].fromDateTime").value("2020-08-07T08:00:00"))
                .andExpect(jsonPath("$.[0].toDateTime").value("2020-08-11T08:00:00"));
    }

    @Test
    public void findAllBookingsByPlatNum() throws Exception {
        when(bookingServiceMock.findAllBookingsByPlatNum(any())).thenReturn(initBookingsDto);

        var resultActions = mockMvc.perform(get(apiUrlBase + "/bookings/plate/TQS121")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.[1].customerId").value(1))
                .andExpect(jsonPath("$.[1].plateNum").value("TQS121"))
                .andExpect(jsonPath("$.[1].fromDateTime").value("2020-08-01T15:00:00"))
                .andExpect(jsonPath("$.[1].toDateTime").value("2020-08-10T11:00:00"));
    }

    @Test
    public void findAllBookingsBetweenDateTime() throws Exception {
        when(bookingServiceMock.findAllBookingsBetweenDateTime(any(), any())).thenReturn(initBookingsDto);

        var resultActions = mockMvc.perform(get(apiUrlBase + "/bookings/all/?startDateTime=20200729160000&endDateTime=20200807070000")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.[1].customerId").value(1))
                .andExpect(jsonPath("$.[1].plateNum").value("TQS121"))
                .andExpect(jsonPath("$.[1].fromDateTime").value("2020-08-01T15:00:00"))
                .andExpect(jsonPath("$.[1].toDateTime").value("2020-08-10T11:00:00"));
    }

    @Test
    public void findAllBookingsBetweenDateTime_INVALID_ARGUMENT() throws Exception {
        when(bookingServiceMock.findAllBookingsBetweenDateTime(any(), any())).thenReturn(initBookingsDto);

        var result = mockMvc.perform(get(apiUrlBase + "/bookings/all/?startDateTime=20200729160000&endDateTime=2020080070000")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        ErrorMessage errorMessage = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ErrorMessage.class);
        assertTrue(errorMessage.getMessage().contains("Failed to convert value of type 'java.lang.String' to required type 'java.time.LocalDateTime'"));
    }

    @Test
    public void findAllBookingsBetweenDateTime_RUNTIME_ERROR_Required_Param() throws Exception {
        when(bookingServiceMock.findAllBookingsBetweenDateTime(any(), any())).thenReturn(initBookingsDto);

        var result = mockMvc.perform(get(apiUrlBase + "/bookings/all/?startDateTime=20200729160000")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        ErrorMessage errorMessage = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ErrorMessage.class);
        assertTrue(errorMessage.getMessage().contains("Required LocalDateTime parameter 'endDateTime' is not present"));
    }

    @Test
    public void findAllBookingsTotalPayment() throws Exception {
        Map<String, String> totalPayment = Stream.of(
                new AbstractMap.SimpleEntry<>("MUV171", "6700 SEK"),
                new AbstractMap.SimpleEntry<>("TQS121", "17000 SEK")
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        when(bookingServiceMock.findAllBookingsTotalPayment(any(), any())).thenReturn(totalPayment);

        var result = mockMvc.perform(get(apiUrlBase + "/bookings/totalpayment/?startDateTime=20200729160000&endDateTime=20200810070000")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        try {
            Map<String, String> payments = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), Map.class);
            assertEquals(2, payments.size());
            assertThat(payments, hasEntry("MUV171", "6700 SEK"));
            assertThat(payments, hasEntry("TQS121", "17000 SEK"));
        } catch (Exception ex) {
            LOGGER.error("Test Error: ", ex.getLocalizedMessage());
            fail("Error in JSON to Map conversion" + ex.getLocalizedMessage());
        }
    }

    @Test
    public void create() throws Exception {
        BookingDto bookingDto = new BookingDto(1L, "HQP191",
                LocalDateTime.parse("2020-08-20 08:00:00", AppConstants.getFormatter()),
                LocalDateTime.parse("2020-08-24 17:00:00", AppConstants.getFormatter()));
        Car car = new Car("HQP191", "VOLVO", 2020, new BigDecimal("120"));
        Customer customer = new Customer(1L, "Jane", "Doe", 198101021100L);
        Booking booking = new Booking(3L, customer, car,
                LocalDateTime.parse("2020-08-20 08:00:00", AppConstants.getFormatter()),
                LocalDateTime.parse("2020-08-24 17:00:00", AppConstants.getFormatter()));

        when(bookingServiceMock.create(any())).thenReturn(booking);

        String requestJson = objectWriter.writeValueAsString(bookingDto);

        var result = mockMvc.perform(post(apiUrlBase + "/bookings").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isCreated());

        Booking bCreated = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), Booking.class);

        assertNotNull(bCreated);
        assertEquals("HQP191", bCreated.getCar().getPlateNum());
        assertThat(bCreated.getCustomer().getId(), is(1L));
        assertThat(bCreated.getFromDateTime().format(AppConstants.getFormatter()), is("2020-08-20 08:00:00"));
        assertThat(bCreated.getToDateTime().format(AppConstants.getFormatter()), is("2020-08-24 17:00:00"));
    }
}
