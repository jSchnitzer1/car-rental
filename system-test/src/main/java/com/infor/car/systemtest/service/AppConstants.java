package com.infor.car.systemtest.service;

import com.infor.car.systemtest.SystemTestApplication;
import com.infor.car.systemtest.dto.CarDto;
import com.infor.car.systemtest.dto.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppConstants {
    private static Logger LOGGER = LoggerFactory.getLogger(SystemTestApplication.class);

    @Getter
    private static final Integer maxDaySearch = 15;

    @Getter
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Getter
    private static final List<CustomerDto> customersToBeCreated = List.of(
            new CustomerDto("Tim", "Hashim", 199001021144L),
            new CustomerDto("Kamel", "Jams", 199101021144L),
            new CustomerDto("Ahmed", "Samih", 199201021144L),
            new CustomerDto("Sven", "Svennson", 199401021144L),
            new CustomerDto("Tom", "Tommson", 199501021144L)
    );

    @Getter
    private static final List<CarDto> carsToBeRegistered = List.of(
            new CarDto("NOQ156", "MAZDA", 2015, new BigDecimal("60")),
            new CarDto("NXR194", "TOYOTA", 2020, new BigDecimal("100")),
            new CarDto("JUQ985", "NISSAN", 2010, new BigDecimal("45")),
            new CarDto("HDR519", "BMW", 2016, new BigDecimal("65"))
    );

    @Getter
    private static final List<FromToDateTime> fromToDateTimes = List.of(

            new FromToDateTime(LocalDateTime.parse("2020-07-29 16:00:00", formatter), LocalDateTime.parse("2020-08-10 07:00:00", formatter)),
            new FromToDateTime(LocalDateTime.parse("2020-08-29 16:00:00", formatter), LocalDateTime.parse("2020-09-01 07:00:00", formatter)),
            new FromToDateTime(LocalDateTime.parse("2020-10-02 08:00:00", formatter), LocalDateTime.parse("2020-10-10 07:00:00", formatter)),
            new FromToDateTime(LocalDateTime.parse("2020-08-30 07:00:00", formatter), LocalDateTime.parse("2020-09-04 12:00:00", formatter)),
            new FromToDateTime(LocalDateTime.parse("2020-11-01 17:00:00", formatter), LocalDateTime.parse("2020-11-10 09:00:00", formatter)),
            new FromToDateTime(LocalDateTime.parse("2020-11-05 16:00:00", formatter), LocalDateTime.parse("2020-11-15 10:00:00", formatter)),
            new FromToDateTime(LocalDateTime.parse("2020-11-04 20:00:00", formatter), LocalDateTime.parse("2020-11-14 07:00:00", formatter)),
            new FromToDateTime(LocalDateTime.parse("2020-11-15 21:00:00", formatter), LocalDateTime.parse("2020-11-17 09:00:00", formatter)),
            new FromToDateTime(LocalDateTime.parse("2020-11-11 16:00:00", formatter), LocalDateTime.parse("2020-11-12 07:00:00", formatter))
    );

    public static final void logError(Throwable e, String errorType) {
        String error = e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getMessage();
        LOGGER.error("Error in " + errorType + " -> " + error);
    }

    public static final void displayObject(String op, Object o) {
        LOGGER.info(op + " " + o.getClass().getTypeName() + " -> " + o + " Performed by [Thread] " + Thread.currentThread().getName());
    }

    public static final void simulateDelayedProcessing(int mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            LOGGER.error("Error is thread sleep -> " + e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getMessage());
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class FromToDateTime {
        private LocalDateTime fromDateTime;
        private LocalDateTime toDateTime;
    }

}

