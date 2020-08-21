package com.infor.car.api.repository;

import com.infor.car.api.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {


    @Override
    @Transactional
    @Query("SELECT b FROM Booking b LEFT JOIN FETCH b.customer LEFT JOIN FETCH b.car")
    List<Booking> findAll();

    @Transactional
    @Query("SELECT b FROM Booking b WHERE b.id=:id")
    Optional<Booking> findById(@Param("id") Long id);

    @Transactional
    @Query("SELECT b FROM Booking b INNER JOIN b.customer c WHERE c.id=:customerId")
    Optional<List<Booking>> findAllByCustomerId(@Param("customerId") Long customerId);

    @Transactional
    @Query("SELECT b FROM Booking b INNER JOIN b.car c WHERE c.plateNum=:plateNum")
    Optional<List<Booking>> findAllByPlateNum(@Param("plateNum") String plateNum);

    // Report of booked cars from date/time, to date/time
    @Transactional
    @Query("SELECT b FROM Booking b WHERE :startDateTime<=b.toDateTime AND :endDateTime>=b.fromDateTime")
    Optional<List<Booking>> findAllBetweenDateTime(@Param("startDateTime") LocalDateTime startDateTime,
                                                   @Param("endDateTime") LocalDateTime endDateTime);

    // Report exiting booking
    @Transactional
    @Query("SELECT b FROM Booking b WHERE :startDateTime<=b.toDateTime AND :endDateTime>=b.fromDateTime AND b.car.plateNum=:plateNum")
    Optional<List<Booking>> findExistingBooking(@Param("plateNum") String plateNum,
                                                @Param("startDateTime") LocalDateTime startDateTime,
                                                @Param("endDateTime") LocalDateTime endDateTime);
}
