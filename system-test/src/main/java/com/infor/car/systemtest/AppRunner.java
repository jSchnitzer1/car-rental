package com.infor.car.systemtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infor.car.systemtest.service.AppConstants;
import com.infor.car.systemtest.service.BookingService;
import com.infor.car.systemtest.service.CarService;
import com.infor.car.systemtest.service.CustomerService;
import com.infor.car.systemtest.task.BookingCreationTask;
import com.infor.car.systemtest.task.CarCreationTask;
import com.infor.car.systemtest.task.CustomerCreationTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Component
public class AppRunner implements CommandLineRunner {
    private static Logger LOGGER = LoggerFactory.getLogger(SystemTestApplication.class);

    @Autowired
    private CarService carService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BookingService bookingService;

    @Override
    public void run(String... args) throws Exception {
        final ExecutorService customerCreationExecutor = Executors.newFixedThreadPool(2);
        final ExecutorService carCreationExecutor = Executors.newFixedThreadPool(2);
        final ExecutorService bookingCreationExecutor = Executors.newFixedThreadPool(5);
        final ExecutorService displayAllBookingsExecutor = Executors.newSingleThreadExecutor();
        final ExecutorService displayBookingsPerHourExecutor = Executors.newSingleThreadExecutor();
        final ExecutorService displayBookingsTotalPayment = Executors.newSingleThreadExecutor();

        // adding more customers
        IntStream.range(0, AppConstants.getCustomersToBeCreated().size()).forEach(i ->
                customerCreationExecutor.submit(new CustomerCreationTask(customerService, i)));

        // displaying all bookings between two dates
        displayBookings(displayAllBookingsExecutor);

        // registering more cars
        IntStream.range(0, AppConstants.getCarsToBeRegistered().size()).forEach(i ->
                carCreationExecutor.submit(new CarCreationTask(carService, i)));

        // displaying bookings per hour between two dates
        displayBookingsPerHour(displayBookingsPerHourExecutor);

        // display total payments
        displayTotalPayments(displayBookingsTotalPayment);

        // creating more bookings
        IntStream.range(0, AppConstants.getFromToDateTimes().size()).forEach(i ->
                bookingCreationExecutor.submit(new BookingCreationTask(bookingService, customerService, carService, i)));

        //display current customers, cars, and bookings
        //displayInfo();
    }

    private void displayBookings(ExecutorService displayAllBookingsExecutor) {
        IntStream.range(0, 5).forEach(i -> displayAllBookingsExecutor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("\n\n");
                bookingService.getAllBookings("20200729160000", "20201231070000")
                        .doOnError(e -> AppConstants.logError(e, "displaying booking"))
                        .subscribe(b -> {
                            AppConstants.displayObject("Display", b);
                        });
                AppConstants.simulateDelayedProcessing(8000);
            }
        }));
    }

    private void displayBookingsPerHour(ExecutorService executorService) {
        IntStream.range(0, 5).forEach(i -> executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("\n\n");
                bookingService.findAllBookingsBetweenDateTimePerHour("20200806160000", "20200807110000")
                        .doOnError(e -> AppConstants.logError(e, "displaying booking"))
                        .subscribe(m -> {
                            LOGGER.info("Display total hours.....");
                            ObjectMapper mapper = new ObjectMapper();
                            try {
                                Map<String, Integer> hours = mapper.readValue(m, Map.class);
                                hours.entrySet().forEach(e -> LOGGER.info("Hour: " + e.getKey() + " No. of reservations: " + e.getValue() + " Performed by [Thread] " + Thread.currentThread().getName()));
                            } catch (IOException e) {
                                LOGGER.error("Error in mapping hours {}", e.getMessage());
                            }

                        });
                AppConstants.simulateDelayedProcessing(8000);
            }
        }));
    }

    private void displayTotalPayments(ExecutorService executorService) {
        IntStream.range(0, 5).forEach(i -> executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("\n\n");
                bookingService.findAllBookingsTotalPayment("20200806160000", "20200807110000")
                        .doOnError(e -> AppConstants.logError(e, "displaying booking"))
                        .subscribe(m -> {
                            LOGGER.info("Display total payments.....");
                            ObjectMapper mapper = new ObjectMapper();
                            try {
                                Map<String, String> hours = mapper.readValue(m, Map.class);
                                hours.entrySet().forEach(e -> LOGGER.info("Payment for Car: " + e.getKey() + " Total payment is: " + e.getValue() + " Performed by [Thread] " + Thread.currentThread().getName()));
                            } catch (IOException e) {
                                LOGGER.error("Error in total payments {}", e.getMessage());
                            }

                        });
                AppConstants.simulateDelayedProcessing(8000);
            }
        }));
    }

    @Deprecated
    private void displayInfo() {
        AppConstants.simulateDelayedProcessing(2000);

        System.out.println("\n\n");
        customerService.findAll().subscribe(System.out::println);
        carService.findAll().subscribe(System.out::println);

        AppConstants.simulateDelayedProcessing(1000);
        System.out.println("\n\nfind BMW car in cars......");
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                carService.findCarsByModel("BMW").subscribe(System.out::println);
            }
        });

        AppConstants.simulateDelayedProcessing(1000);
        System.out.println("\n\nfind available cars for price 80 SEK and between 2020-07-29 16:00:00 and 2020-08-10 07:00:00......");
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                carService.findAvailableCars("80", "20200729160000", "20200810070000").subscribe(System.out::println);
            }
        });
    }
}

