package com.infor.car.api.controller;

import com.infor.car.api.AppConstants;
import com.infor.car.api.dto.BookingDto;
import com.infor.car.api.model.Booking;
import com.infor.car.api.service.BookingService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@Validated
public class BookingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<BookingDto>> findAllBookings() {
        LOGGER.info("Getting all bookings");
        return new ResponseEntity<>(bookingService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<BookingDto> findById(@PathVariable @Valid @Min(value = 1) Long id) {
        LOGGER.info("Getting a booking by ID {}", id);
        return new ResponseEntity<>(bookingService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<List<BookingDto>> findAllBookingsByCustomerId(@PathVariable("id") @Valid @Min(value = 1) Long customerId) {
        LOGGER.info("Getting a booking by customer ID {}", customerId);
        return new ResponseEntity<>(bookingService.findAllBookingsByCustomerId(customerId), HttpStatus.OK);
    }

    @GetMapping("/plate/{plate}")
    public ResponseEntity<List<BookingDto>> findAllBookingsByPlatNum(@PathVariable("plate") @Valid @NotBlank String plateNum) {
        LOGGER.info("Getting a booking by plate number {}", plateNum);
        return new ResponseEntity<>(bookingService.findAllBookingsByPlatNum(plateNum), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookingDto>> findAllBookingsBetweenDateTime(@RequestParam("startDateTime") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyyMMddHHmmss") LocalDateTime startDateTime,
                                                                        @RequestParam("endDateTime") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyyMMddHHmmss")  LocalDateTime endDateTime) {
        LOGGER.info("Finding available bookings between [" + startDateTime.format(AppConstants.getFormatter()) + "] and [" + endDateTime.format(AppConstants.getFormatter()) + "]");
        return new ResponseEntity<>(bookingService.findAllBookingsBetweenDateTime(startDateTime, endDateTime), HttpStatus.OK);
    }

    @GetMapping("/allperhour")
    public ResponseEntity<Map<String, Integer>> findAllBookingsBetweenDateTimePerHour(@RequestParam("startDateTime") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyyMMddHHmmss") LocalDateTime startDateTime,
                                                                                      @RequestParam("endDateTime") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyyMMddHHmmss")  LocalDateTime endDateTime) {
        LOGGER.info("Finding available bookings per hour between [" + startDateTime.format(AppConstants.getFormatter()) + "] and [" + endDateTime.format(AppConstants.getFormatter()) + "]");
        return new ResponseEntity<>(bookingService.findAllBookingsBetweenDateTimePerHour(startDateTime, endDateTime), HttpStatus.OK);
    }

    @GetMapping("/totalpayment")
    public ResponseEntity<Map<String, String>> findAllBookingsTotalPayment(@RequestParam("startDateTime") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyyMMddHHmmss") LocalDateTime startDateTime,
                                                                               @RequestParam("endDateTime") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyyMMddHHmmss")  LocalDateTime endDateTime) {
        LOGGER.info("Finding total payments [" + startDateTime.format(AppConstants.getFormatter()) + "] and [" + endDateTime.format(AppConstants.getFormatter()) + "]");
        return new ResponseEntity<>(bookingService.findAllBookingsTotalPayment(startDateTime, endDateTime), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBooking(@PathVariable @Valid @Min(value = 1) Long id) {
        LOGGER.info("Deleting the booking with id {}", id);
        bookingService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody @Valid @NotNull BookingDto booking) {
        LOGGER.info("Creating Booking for [{}] between %s and {}",booking.getPlateNum(), booking.getFromDateTime(), booking.getToDateTime());
        return new ResponseEntity<>(bookingService.create(booking),
                HttpStatus.CREATED);
    }
}
