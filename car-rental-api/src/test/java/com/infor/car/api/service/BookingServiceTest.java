package com.infor.car.api.service;

import com.infor.car.api.AppConstants;
import com.infor.car.api.dto.BookingDto;
import com.infor.car.api.exception.EntityNotFoundException;
import com.infor.car.api.exception.ErrorCode;
import com.infor.car.api.exception.LongPeriodException;
import com.infor.car.api.model.Booking;
import com.infor.car.api.repository.CarRepository;
import com.infor.car.api.repository.CustomerRepository;
import org.hamcrest.collection.IsMapContaining;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void findAll() {
        List<BookingDto> bookingDtos = bookingService.findAll();

        assertNotNull(bookingDtos);
        assertEquals(2, bookingDtos.size());

        BookingDto bookingDto_1 = bookingDtos.get(0);
        assertEquals("MUV171", bookingDto_1.getPlateNum());
        assertThat(bookingDto_1.getCustomerId(), is(2L));
        assertThat(bookingDto_1.getFromDateTime().format(AppConstants.getFormatter()), is("2020-08-07 08:00:00"));
        assertThat(bookingDto_1.getToDateTime().format(AppConstants.getFormatter()), is("2020-08-11 08:00:00"));
    }

    @Test
    public void findAllBookingsByPlatNum() {
        List<BookingDto> bookingDtos = bookingService.findAllBookingsByPlatNum("MUV171");

        assertNotNull(bookingDtos);
        assertEquals(1, bookingDtos.size());

        BookingDto bookingDto = bookingDtos.get(0);
        assertEquals("MUV171", bookingDto.getPlateNum());
        assertThat(bookingDto.getCustomerId(), is(2L));
        assertThat(bookingDto.getFromDateTime().format(AppConstants.getFormatter()), is("2020-08-07 08:00:00"));
        assertThat(bookingDto.getToDateTime().format(AppConstants.getFormatter()), is("2020-08-11 08:00:00"));
    }

    @Test
    public void findAllBookingsBetweenDateTime() {
        List<BookingDto> bookingDtos = bookingService.findAllBookingsBetweenDateTime(LocalDateTime.parse("2020-07-29 16:00:00", AppConstants.getFormatter()),
                LocalDateTime.parse("2020-08-07 07:00:00", AppConstants.getFormatter()));


        BookingDto bookingDto_1 = bookingDtos.get(0);
        assertEquals("TQS121", bookingDto_1.getPlateNum());
        assertThat(bookingDto_1.getCustomerId(), is(1L));
        assertThat(bookingDto_1.getFromDateTime().format(AppConstants.getFormatter()), is("2020-08-01 15:00:00"));
        assertThat(bookingDto_1.getToDateTime().format(AppConstants.getFormatter()), is("2020-08-10 11:00:00"));
    }

    @Test
    public void findAllBookingsBetweenDateTime_EntityNotFoundException() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                bookingService.findAllBookingsBetweenDateTime(LocalDateTime.parse("2020-06-01 16:00:00", AppConstants.getFormatter()),
                        LocalDateTime.parse("2020-07-01 07:00:00", AppConstants.getFormatter())));
        assertTrue(exception.getMessage().equals("There are no available bookings between [2020-06-01 16:00:00] and [2020-07-01 07:00:00]"));
        assertThat(exception.getErrorCode(), is(ErrorCode.RESOURCE_NOT_FOUND));
    }

    @Test
    public void findAllBookingsBetweenDateTimePerHour() {
        Map<String, Integer> mHours = bookingService.findAllBookingsBetweenDateTimePerHour(LocalDateTime.parse("2020-07-29 16:00:00", AppConstants.getFormatter()),
                LocalDateTime.parse("2020-08-10 07:00:00", AppConstants.getFormatter()));

        assertNotNull(mHours);
        assertThat(mHours.size(), is(279));
        assertTrue(mHours.containsKey("2020-08-07 12:00:00"));
        assertTrue(IsMapContaining.hasEntry("2020-08-07 12:00:00", 2).matches(mHours));
    }

    @Test
    public void findAllBookingsBetweenDateTimePerHourException() {
        LongPeriodException exception = assertThrows(LongPeriodException.class, () ->
                bookingService.findAllBookingsBetweenDateTimePerHour(LocalDateTime.parse("2020-07-29 16:00:00", AppConstants.getFormatter()),
                        LocalDateTime.parse("2020-09-10 07:00:00", AppConstants.getFormatter())));
        assertTrue(exception.getMessage().equals("Number of days should not exceed 15 days between start date time [2020-07-29 16:00:00] and end date time [2020-09-10 07:00:00]"));
        assertThat(exception.getErrorCode(), is(ErrorCode.LONG_PERIOD_SEARCH));
    }

    @Test
    public void findAllBookingsTotalPayment() {
        Map<String, String> totalPayments = bookingService.findAllBookingsTotalPayment(LocalDateTime.parse("2020-07-29 16:00:00", AppConstants.getFormatter()),
                LocalDateTime.parse("2020-08-10 07:00:00", AppConstants.getFormatter()));

        assertNotNull(totalPayments);
        assertThat(totalPayments.size(), is(2));
        assertTrue(totalPayments.containsKey("TQS121"));
        assertTrue(IsMapContaining.hasEntry("TQS121", "17000 SEK").matches(totalPayments));
    }
}
