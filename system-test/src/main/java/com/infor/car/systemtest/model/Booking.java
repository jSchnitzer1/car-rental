package com.infor.car.systemtest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Booking {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("customer")
    private Customer customer;

    @JsonProperty("car")
    private Car car;

    @JsonProperty("fromDateTime")
    private LocalDateTime fromDateTime;

    @JsonProperty("toDateTime")
    private LocalDateTime toDateTime;

    @JsonCreator
    public Booking() {
    }

    @JsonCreator
    public Booking(@JsonProperty("id") Long id, @JsonProperty("customer") Customer customer, @JsonProperty("car") Car car, @JsonProperty("fromDateTime") LocalDateTime fromDateTime, @JsonProperty("toDateTime") LocalDateTime toDateTime) {
        this.id = id;
        this.customer = customer;
        this.car = car;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
    }
}
