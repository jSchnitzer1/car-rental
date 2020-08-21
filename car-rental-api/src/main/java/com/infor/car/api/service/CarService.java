package com.infor.car.api.service;

import com.infor.car.api.controller.CarController;
import com.infor.car.api.AppConstants;
import com.infor.car.api.exception.EntityExistsException;
import com.infor.car.api.exception.EntityNotFoundException;
import com.infor.car.api.exception.ErrorCode;
import com.infor.car.api.model.Car;
import com.infor.car.api.repository.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarController.class);

    @Autowired
    private CarRepository repository;

    @Transactional
    public List<Car> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Car findByPlateNum(String plateNum) {
        Optional<Car> car = repository.findByPlateNum(plateNum);
        if (!car.isPresent()) {
            LOGGER.error("Car with the given plat number [{}] doesn't exists", plateNum);
            throw new EntityNotFoundException("Car with the given plate number [" + plateNum + "] doesn't exists", ErrorCode.RESOURCE_NOT_FOUND);
        }
        return car.get();
    }

    @Transactional
    public List<Car> findByModel(String model) {
        Optional<List<Car>> cars = repository.findByModel(model);
        if (!cars.isPresent()) {
            LOGGER.error("Car(s) with the given model [{}] don't exists", model);
            throw new EntityNotFoundException("Car with the given model [" + model + "] doesn't exists", ErrorCode.RESOURCE_NOT_FOUND);
        }
        return cars.get();
    }

    @Transactional
    public List<Car> findByPrice(BigDecimal price) {
        Optional<List<Car>> cars = repository.findByPrice(price);
        if (!cars.isPresent()) {
            LOGGER.error("Car(s) with the given [{}] don't exists", price);
            throw new EntityNotFoundException("Car(s) with the given price [" + price + "] doesn't exists", ErrorCode.RESOURCE_NOT_FOUND);
        }
        return cars.get();
    }

    @Transactional
    public List<Car> findAvailableCars(BigDecimal price, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Optional<List<Car>> cars = repository.findAvailableCars(price, startDateTime, endDateTime);
        if (!cars.isPresent()) {
            LOGGER.error("There are no available car(s) with the given price [{}] and start date [{}] and end date [{}]", price, startDateTime.format(AppConstants.getFormatter()), endDateTime.format(AppConstants.getFormatter()));
            throw new EntityNotFoundException("There are no available car(s) with the given price [" + price + "] and start date [" + startDateTime.format(AppConstants.getFormatter()) + "] and end date [" + endDateTime.format(AppConstants.getFormatter()) + "]", ErrorCode.RESOURCE_NOT_FOUND);
        }
        return cars.get();
    }

    @Transactional
    @Modifying
    public Car create(Car car) {
        Optional<Car> existingCar = repository.findByPlateNum(car.getPlateNum());
        if (existingCar.isPresent()) {
            Car c = existingCar.get();
            LOGGER.error("Car with the plate number already exists: {}", c.getPlateNum());
            throw new EntityExistsException("Car with the given plate number already exists:" + car.getPlateNum(), ErrorCode.RESOURCE_ALREADY_EXISTS);
        }
        return repository.save(car);
    }

    @Transactional
    @Modifying
    public void delete(String plateNum) {
        Car car = repository.findByPlateNum(plateNum).orElseThrow(() ->
                new EntityNotFoundException("Car with the given plate number [" + plateNum + "] doesn't exists", ErrorCode.RESOURCE_NOT_FOUND));
        repository.delete(car);
    }
}
