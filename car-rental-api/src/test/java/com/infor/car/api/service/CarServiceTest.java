package com.infor.car.api.service;

import com.infor.car.api.AppConstants;
import com.infor.car.api.exception.EntityNotFoundException;
import com.infor.car.api.exception.ErrorCode;
import com.infor.car.api.model.Car;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class CarServiceTest {

    @Autowired
    private CarService carService;

    @Test
    public void findAll() {
        List<Car> cars = carService.findAll();
        assertNotNull(cars);
        assertThat(cars.size(), greaterThan(0));

        Car car_1 = cars.get(0);
        assertEquals("NPS151", car_1.getPlateNum());
        assertEquals("BMW", car_1.getModel());
        assertThat(car_1.getYear(), is(2018));
        assertEquals(new BigDecimal("90.00"), car_1.getPrice());
    }

    @Test
    public void findByPlateNum() {
        Car car = carService.findByPlateNum("TQS121");

        assertEquals("TQS121", car.getPlateNum());
        assertEquals("AUDI", car.getModel());
        assertThat(car.getYear(), is(2017));
        assertEquals(new BigDecimal("80.00"), car.getPrice());
    }

    @Test
    public void findByPlateNumEntityNotFoundException() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                carService.findByPlateNum("IncorrectPlateNumber"));
        assertTrue(exception.getMessage().equals("Car with the given plate number [IncorrectPlateNumber] doesn't exists"));
        assertThat(exception.getErrorCode(), is(ErrorCode.RESOURCE_NOT_FOUND));
    }

    @Test
    public void findByPrice() {
        List<Car> cars = carService.findByPrice(new BigDecimal(75));

        assertThat(cars.size(), greaterThan(0));

        Car car = cars.get(0);
        assertEquals("MUV171", car.getPlateNum());
        assertEquals("MAZDA", car.getModel());
        assertThat(car.getYear(), is(2016));
        assertEquals(new BigDecimal("70.00"), car.getPrice());
    }

    @Test
    public void findByPriceEntityNotFoundException() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                carService.findByPrice(new BigDecimal("25")));
        assertTrue(exception.getMessage().equals("Car(s) with the given price [25] doesn't exists"));
        assertThat(exception.getErrorCode(), is(ErrorCode.RESOURCE_NOT_FOUND));
    }

    @Test
    public void findAvailableCars() {
        List<Car> cars = carService.findAvailableCars(new BigDecimal("150"),
                LocalDateTime.parse("2020-07-29 16:00:00", AppConstants.getFormatter()),
                LocalDateTime.parse("2020-08-10 07:00:00", AppConstants.getFormatter()));

        assertThat(cars.size(), greaterThan(0));

        Car car_1 = cars.get(0);
        assertEquals("NPS151", car_1.getPlateNum());
        assertEquals("BMW", car_1.getModel());
        assertThat(car_1.getYear(), is(2018));
        assertEquals(new BigDecimal("90.00"), car_1.getPrice());

        Car car_2 = cars.get(1);
        assertEquals("HQP191", car_2.getPlateNum());
        assertEquals("VOLVO", car_2.getModel());
        assertThat(car_2.getYear(), is(2020));
        assertEquals(new BigDecimal("120.00"), car_2.getPrice());
    }
}
