package com.infor.car.api.controller;

import com.infor.car.api.AppConstants;
import com.infor.car.api.dto.CarDto;
import com.infor.car.api.model.Car;
import com.infor.car.api.service.CarService;
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
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
@Validated
public class CarController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarController.class);

    @Autowired
    private CarService carService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<Car>> findAllCars() {
        LOGGER.info("Getting all cars");
        return new ResponseEntity<>(carService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/plate/{plateNum}")
    public ResponseEntity<Car> findCarByPlateNum(@PathVariable @Valid @NotBlank String plateNum) {
        LOGGER.info("Getting a car with plate number {}", plateNum);
        return new ResponseEntity<>(carService.findByPlateNum(plateNum), HttpStatus.OK);
    }

    @GetMapping("/model/{model}")
    public ResponseEntity<List<Car>> findCarsByModel(@PathVariable @Valid @NotBlank @Size(max = 15) String model) {
        LOGGER.info("Finding cars by their model {}", model.toUpperCase());
        return new ResponseEntity<>(carService.findByModel(model.toUpperCase()), HttpStatus.OK);
    }

    @GetMapping("/price/{price}")
    public ResponseEntity<List<Car>> findCarsByPrice(@PathVariable @Valid @Min(value = 1) BigDecimal price) {
        LOGGER.info("Finding cars with price less than {}", price);
        return new ResponseEntity<>(carService.findByPrice(price), HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Car>> findAvailableCars(@RequestParam("price") @Valid @Min(value = 1) BigDecimal price,
                                                       @RequestParam("startDateTime") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyyMMddHHmmss") LocalDateTime startDateTime,
                                                       @RequestParam("endDateTime") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyyMMddHHmmss")  LocalDateTime endDateTime) {
        LOGGER.info("Finding available cars between [" + startDateTime.format(AppConstants.getFormatter()) + "] and [" + endDateTime.format(AppConstants.getFormatter()) + "]");
        return new ResponseEntity<>(carService.findAvailableCars(price, startDateTime, endDateTime), HttpStatus.OK);
    }

    @DeleteMapping("/{plateNum}")
    public ResponseEntity deleteCar(@PathVariable @Valid @NotBlank String plateNum) {
        LOGGER.info("Deleting the car with plate number {}", plateNum);
        carService.delete(plateNum);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Car> createCustomer(@RequestBody @Valid @NotNull CarDto car) {
        LOGGER.info("Creating customer {}", car.getPlateNum());
        car.setModel(car.getModel().toUpperCase());
        return new ResponseEntity<>(carService.create(modelMapper.map(car, Car.class)),
                HttpStatus.CREATED);
    }
}
