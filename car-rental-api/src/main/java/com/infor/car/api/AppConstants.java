package com.infor.car.api;

import com.infor.car.api.dto.BookingDto;
import com.infor.car.api.model.Booking;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class AppConstants {
    @Getter
    private static final Integer maxDaySearch = 15;

    @Getter
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

}
