package com.infor.car.api.repository;

import com.infor.car.api.AppConstants;
import com.infor.car.api.dto.BookingDto;
import com.infor.car.api.model.Booking;
import com.infor.car.api.model.Car;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    @Test
    public void findAll() {
        final List<Booking> bookings = bookingRepository.findAll();

        assertNotNull(bookings);
        assertEquals(2, bookings.size());

        List<BookingDto> bookingDtos = BookingDto.mapToBookingDto(bookings);
        BookingDto bookingDto_1 = bookingDtos.get(0);
        assertEquals("MUV171", bookingDto_1.getPlateNum());
        assertThat(bookingDto_1.getCustomerId(), is(2L));
        assertThat(bookingDto_1.getFromDateTime().format(AppConstants.getFormatter()), is("2020-08-07 08:00:00"));
        assertThat(bookingDto_1.getToDateTime().format(AppConstants.getFormatter()), is("2020-08-11 08:00:00"));
    }

    @Test
    public void findAllBetweenDateTime() {
        final Optional<List<Booking>> opBookings = bookingRepository.findAllBetweenDateTime(LocalDateTime.parse("2020-07-29 16:00:00", AppConstants.getFormatter()),
                LocalDateTime.parse("2020-08-07 07:00:00", AppConstants.getFormatter()));

        assertTrue(opBookings.isPresent());

        List<Booking> bookings = opBookings.get();
        assertThat(bookings.size(), greaterThan(0));

        List<BookingDto> bookingDtos = BookingDto.mapToBookingDto(bookings);
        BookingDto bookingDto_1 = bookingDtos.get(0);
        assertEquals("TQS121", bookingDto_1.getPlateNum());
        assertThat(bookingDto_1.getCustomerId(), is(1L));
        assertThat(bookingDto_1.getFromDateTime().format(AppConstants.getFormatter()), is("2020-08-01 15:00:00"));
        assertThat(bookingDto_1.getToDateTime().format(AppConstants.getFormatter()), is("2020-08-10 11:00:00"));
    }
}
