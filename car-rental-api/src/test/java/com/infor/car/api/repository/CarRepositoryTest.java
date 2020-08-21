package com.infor.car.api.repository;

import com.infor.car.api.AppConstants;
import com.infor.car.api.model.Car;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CarRepositoryTest {
    @Autowired
    private CarRepository repository;

    @Test
    public void findAll() {
        final List<Car> cars = repository.findAll();
        assertNotNull(cars);
        assertEquals(4, cars.size());

        Car car_1 = cars.get(0);
        assertEquals("NPS151", car_1.getPlateNum());
        assertEquals("BMW", car_1.getModel());
        assertThat(car_1.getYear(), is(2018));
        assertEquals(new BigDecimal("90.00"), car_1.getPrice());

        Car car_2 = cars.get(1);
        assertEquals("TQS121", car_2.getPlateNum());
        assertEquals("AUDI", car_2.getModel());
        assertThat(car_2.getYear(), is(2017));
        assertEquals(new BigDecimal("80.00"), car_2.getPrice());
    }

    @Test
    public void findByPrice() {
        final Optional<List<Car>> opCars = repository.findByPrice(new BigDecimal("75"));
        assertTrue(opCars.isPresent());

        List<Car> cars = opCars.get();
        assertThat(cars.size(), greaterThan(0));

        Car car = cars.get(0);
        assertEquals("MUV171", car.getPlateNum());
        assertEquals("MAZDA", car.getModel());
        assertThat(car.getYear(), is(2016));
        assertEquals(new BigDecimal("70.00"), car.getPrice());
    }

    @Test
    public void findAvailableCars() {
        final Optional<List<Car>> opCars = repository.findAvailableCars(new BigDecimal("150"),
                LocalDateTime.parse("2020-07-29 16:00:00", AppConstants.getFormatter()),
                LocalDateTime.parse("2020-08-10 07:00:00", AppConstants.getFormatter()));

        assertTrue(opCars.isPresent());

        List<Car> cars = opCars.get();
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
