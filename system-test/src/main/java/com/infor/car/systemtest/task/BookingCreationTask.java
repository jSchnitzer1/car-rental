package com.infor.car.systemtest.task;

import com.infor.car.systemtest.dto.BookingDto;
import com.infor.car.systemtest.model.Car;
import com.infor.car.systemtest.model.Customer;
import com.infor.car.systemtest.service.AppConstants;
import com.infor.car.systemtest.service.BookingService;
import com.infor.car.systemtest.service.CarService;
import com.infor.car.systemtest.service.CustomerService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Random;

@AllArgsConstructor
public class BookingCreationTask implements Runnable {
    private BookingService bookingService;
    private CustomerService customerService;
    private CarService carService;
    private int taskId;

    @Override
    public void run() {
        Random rand = new Random();
        List<Customer> customers = customerService.findAll().collectList().block();
        List<Car> cars = carService.findAll().collectList().block();
        AppConstants.FromToDateTime fromToDateTime = AppConstants.getFromToDateTimes().get(taskId);
        BookingDto bookingDto = new BookingDto(customers.get(rand.nextInt(customers.size())).getId(),
                cars.get(rand.nextInt(cars.size())).getPlateNum(),
                fromToDateTime.getFromDateTime(),
                fromToDateTime.getToDateTime());

        bookingService.create(bookingDto)
                .doOnError(e -> AppConstants.logError(e, "creating booking"))
                .subscribe(b -> {
                    AppConstants.displayObject("Created", b);
                    AppConstants.simulateDelayedProcessing(1000);
                });
    }
}
