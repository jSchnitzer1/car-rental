package com.infor.car.api.repository;

import com.infor.car.api.model.Booking;
import com.infor.car.api.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, String> {
    @Transactional
    @Query("SELECT c FROM Car c WHERE c.plateNum=:plateNum")
    Optional<Car> findByPlateNum(@Param("plateNum") String plateNum);

    @Transactional
    @Query("SELECT c FROM Car c WHERE c.model=:model")
    Optional<List<Car>> findByModel(@Param("model") String model);

    @Transactional
    @Query("SELECT c FROM Car c WHERE c.price<=:price")
    Optional<List<Car>> findByPrice(@Param("price") BigDecimal price);

    //Search for available cars to rent from date/time, to date/time, maximum rental price per hour
    @Transactional
    @Query("SELECT c FROM Car c WHERE c.price<=:price AND c.plateNum NOT IN (SELECT b.car.plateNum FROM Booking b WHERE (:startDateTime<=b.toDateTime AND :endDateTime>=b.fromDateTime))")
    Optional<List<Car>> findAvailableCars(@Param("price") BigDecimal price,
                                        @Param("startDateTime") LocalDateTime startDateTime,
                                        @Param("endDateTime") LocalDateTime endDateTime);
}
