package com.infor.car.systemtest.service;

import com.infor.car.systemtest.dto.CustomerDto;
import com.infor.car.systemtest.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private WebClient webClient;

    public Mono<Customer> create(CustomerDto customerDto) {
        return webClient.post()
                .uri("/customers")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerDto), CustomerDto.class)
                .retrieve()
                .bodyToMono(Customer.class);
    }

    public Flux<Customer> findAll() {
        return webClient.get()
                .uri("/customers")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Customer.class);
    }
}
