package com.infor.car.systemtest.service;

import com.infor.car.systemtest.dto.BookingDto;
import com.infor.car.systemtest.dto.CarDto;
import com.infor.car.systemtest.model.Booking;
import com.infor.car.systemtest.model.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class BookingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    private WebClient webClient;

    public Mono<Booking> create(BookingDto bookingDto) {
        return webClient.post()
                .uri("/bookings")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(bookingDto), CarDto.class)
                .retrieve()
                .bodyToMono(Booking.class);
    }

    public Flux<BookingDto> getAllBookings(String startDateTime, String endDateTime) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/bookings/all")
                        .queryParam("startDateTime", startDateTime)
                        .queryParam("endDateTime", endDateTime)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(BookingDto.class);
    }

    public Mono<String> findAllBookingsBetweenDateTimePerHour(String startDateTime, String endDateTime) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/bookings/allperhour")
                        .queryParam("startDateTime", startDateTime)
                        .queryParam("endDateTime", endDateTime)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> findAllBookingsTotalPayment(String startDateTime, String endDateTime) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/bookings/totalpayment")
                        .queryParam("startDateTime", startDateTime)
                        .queryParam("endDateTime", endDateTime)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
    }
}
