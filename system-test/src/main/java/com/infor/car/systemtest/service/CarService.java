package com.infor.car.systemtest.service;

import com.infor.car.systemtest.dto.CarDto;
import com.infor.car.systemtest.model.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CarService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarService.class);

    @Autowired
    private WebClient webClient;

    public Mono<Car> create(CarDto carDto) {
        return webClient.post()
                .uri("/cars")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(carDto), CarDto.class)
                .retrieve()
                .bodyToMono(Car.class);
    }

    public Flux<Car> findAll() {
        return webClient.get()
                .uri("/cars")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Car.class);
    }

    public Flux<Car> findCarsByModel(String model) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                .path("/cars/model/{model}")
                .build(model))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Car.class);
    }

    public Flux<Car> findAvailableCars(String price, String startDateTime, String endDateTime) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/cars/available")
                        .queryParam("price", price)
                        .queryParam("startDateTime", startDateTime)
                        .queryParam("endDateTime", endDateTime)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Car.class);
    }
}
