package com.infor.car.api.service;

import com.infor.car.api.AppConstants;
import com.infor.car.api.dto.BookingDto;
import com.infor.car.api.exception.EntityExistsException;
import com.infor.car.api.exception.EntityNotFoundException;
import com.infor.car.api.exception.ErrorCode;
import com.infor.car.api.exception.LongPeriodException;
import com.infor.car.api.model.Booking;
import com.infor.car.api.model.Car;
import com.infor.car.api.model.Customer;
import com.infor.car.api.repository.BookingRepository;
import com.infor.car.api.repository.CarRepository;
import com.infor.car.api.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class BookingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CustomerRepository customerRepository;


    @Transactional
    public List<BookingDto> findAll() {
        List<Booking> bookings = bookingRepository.findAll();
        if (bookings == null || bookings.isEmpty()) {
            LOGGER.error("There are no bookings in the database");
            throw new EntityNotFoundException("There are no bookings in the database", ErrorCode.RESOURCE_NOT_FOUND);
        }
        return BookingDto.mapToBookingDto(bookings);
    }

    @Transactional
    public BookingDto findById(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (!booking.isPresent()) {
            LOGGER.error("Booking with the given id [{}] doesn't exists", id);
            throw new EntityNotFoundException("Booking with the given id [" + id + "] doesn't exists", ErrorCode.RESOURCE_NOT_FOUND);
        }
        return BookingDto.mapToBookingDto(booking.get());
    }

    @Transactional
    public List<BookingDto> findAllBookingsByCustomerId(Long customerId) {
        Optional<List<Booking>> bookings = bookingRepository.findAllByCustomerId(customerId);
        if (!bookings.isPresent()) {
            LOGGER.error("Bookings with the given customer-id [{}] doesn't exists", customerId);
            throw new EntityNotFoundException("Customer [" + customerId + "] doesn't have bookings", ErrorCode.RESOURCE_NOT_FOUND);
        }
        return BookingDto.mapToBookingDto(bookings.get());
    }

    @Transactional
    public List<BookingDto> findAllBookingsByPlatNum(String plateNum) {
        Optional<List<Booking>> bookings = bookingRepository.findAllByPlateNum(plateNum);
        if (!bookings.isPresent()) {
            LOGGER.error("Bookings with the given plate number [{}] doesn't exists", plateNum);
            throw new EntityNotFoundException("Car [" + plateNum + "] doesn't have bookings", ErrorCode.RESOURCE_NOT_FOUND);
        }
        return BookingDto.mapToBookingDto(bookings.get());
    }

    @Transactional
    public List<BookingDto> findAllBookingsBetweenDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Optional<List<Booking>> bookings = findAllBetweenDateTime(startDateTime, endDateTime);
        return BookingDto.mapToBookingDto(bookings.get());
    }

    @Transactional
    public Map<String, Integer> findAllBookingsBetweenDateTimePerHour(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if(ChronoUnit.DAYS.between(startDateTime, endDateTime) > AppConstants.getMaxDaySearch()) {
            LOGGER.error("Number of days should not exceed {} days between start date time [{}] and end date time[{}]", AppConstants.getMaxDaySearch(), startDateTime.format(AppConstants.getFormatter()), endDateTime.format(AppConstants.getFormatter()));
            throw new LongPeriodException("Number of days should not exceed 15 days between start date time [" + startDateTime.format(AppConstants.getFormatter()) + "] and end date time [" + endDateTime.format(AppConstants.getFormatter()) + "]", ErrorCode.LONG_PERIOD_SEARCH);
        }

        Optional<List<Booking>> opBookings = findAllBetweenDateTime(startDateTime, endDateTime);
        List<Booking> bookings = opBookings.get();
        LocalDateTime tmpDateTime = startDateTime;
        Map<String, Integer> bookingsPerHour = new LinkedHashMap<>();
        do {
            int count = 0;
            for (Booking b : bookings)
                if (b.getFromDateTime().isBefore(tmpDateTime) && b.getToDateTime().isAfter(tmpDateTime)) count++;
            bookingsPerHour.put(tmpDateTime.format(AppConstants.getFormatter()), count);
            tmpDateTime = (tmpDateTime.plusHours(1));
        } while (tmpDateTime.isBefore(endDateTime));

        return bookingsPerHour;
    }

    private Optional<List<Booking>> findAllBetweenDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Optional<List<Booking>> opBookings = bookingRepository.findAllBetweenDateTime(startDateTime, endDateTime);
        if (!opBookings.isPresent()) {
            LOGGER.error("There are no available bookings between [{}] and [{}]", startDateTime.format(AppConstants.getFormatter()), endDateTime.format(AppConstants.getFormatter()));
            throw new EntityNotFoundException("There are no available bookings between [" + startDateTime.format(AppConstants.getFormatter()) + "] and [" + endDateTime.format(AppConstants.getFormatter()) + "]", ErrorCode.RESOURCE_NOT_FOUND);
        }
        return opBookings;
    }

    @Transactional
    public Map<String, String> findAllBookingsTotalPayment(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Optional<List<Booking>> opBookings = findAllBetweenDateTime(startDateTime, endDateTime);
        List<Booking> bookings = opBookings.get();

        Map<String, String> carPayment = new LinkedHashMap<>();
        bookings.forEach(b -> {
            var hours = ChronoUnit.HOURS.between(b.getFromDateTime(), b.getToDateTime());
            var totalPrice = b.getCar().getPrice().multiply(new BigDecimal(hours)).round(new MathContext(2));
            carPayment.put(b.getCar().getPlateNum(), totalPrice.toPlainString() + " SEK");
        });
        return carPayment;
    }

    @Transactional
    @Modifying
    public Booking create(BookingDto bookingDto) {
        Optional<Car> opCar = carRepository.findByPlateNum(bookingDto.getPlateNum());
        Optional<Customer> opCustomer = customerRepository.findById(bookingDto.getCustomerId());

        if(!opCar.isPresent()) {
            LOGGER.error("Car with the given plat number [{}] doesn't exists", bookingDto.getPlateNum());
            throw new EntityNotFoundException("Car with the given plate number [" + bookingDto.getCustomerId() + "] doesn't exists", ErrorCode.RESOURCE_NOT_FOUND);
        }
        if(!opCustomer.isPresent()) {
            LOGGER.error("Customer with the given id [{}] doesn't exists", bookingDto.getCustomerId());
            throw new EntityNotFoundException("Customer with the given id [" + bookingDto.getCustomerId() + "] doesn't exists", ErrorCode.RESOURCE_NOT_FOUND);
        }

        if(bookingDto.getFromDateTime() == null || bookingDto.getToDateTime() == null) {
            LOGGER.error("From and To dates cannot be null or empty");
            throw new NullPointerException("From and To dates cannot be null or empty");
        }

        Optional<List<Booking>> existingBookings = bookingRepository.findExistingBooking(bookingDto.getPlateNum(), bookingDto.getFromDateTime(), bookingDto.getToDateTime());
        if (existingBookings.isPresent() && existingBookings.get().size() > 0) {
            Booking b = existingBookings.get().get(0);
            LOGGER.error("This car [{}] with is booked between {} and {}", b.getCar().getPlateNum(), b.getFromDateTime(), b.getToDateTime());
            throw new EntityExistsException("This car ["+b.getCar().getPlateNum()+"] with is booked between "+b.getFromDateTime()+" and " + b.getToDateTime(), ErrorCode.RESOURCE_ALREADY_EXISTS);
        }

        Booking booking = new Booking();
        booking.setCar(opCar.get());
        booking.setCustomer(opCustomer.get());
        booking.setFromDateTime(bookingDto.getFromDateTime());
        booking.setToDateTime(bookingDto.getToDateTime());

        return bookingRepository.save(booking);
    }

    @Transactional
    @Modifying
    public void delete(Long bookingId){
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new EntityNotFoundException("Booking with the given id [" + bookingId + "] doesn't exists", ErrorCode.RESOURCE_NOT_FOUND));
        bookingRepository.delete(booking);
    }
}
