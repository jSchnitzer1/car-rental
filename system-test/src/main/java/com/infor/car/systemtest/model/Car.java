package com.infor.car.systemtest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.math.BigDecimal;

@ToString
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Car {

    @JsonProperty("plateNum")
    private String plateNum;

    @JsonProperty("model")
    private String model;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonCreator
    public Car() {
    }

    @JsonCreator
    public Car(@JsonProperty("plateNum")String plateNum, @JsonProperty("model") String model, @JsonProperty("year") Integer year,  @JsonProperty("price") BigDecimal price) {
        this.plateNum = plateNum;
        this.model = model;
        this.year = year;
        this.price = price;
    }
}
