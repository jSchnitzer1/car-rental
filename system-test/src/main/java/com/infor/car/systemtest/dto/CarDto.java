package com.infor.car.systemtest.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

public class CarDto {

    @NotEmpty(message = "Plate Number can't be empty.")
    @Getter
    @Setter
    private String plateNum;

    @NotEmpty(message = "Car model can't be empty.")
    @Getter
    @Setter
    private String model;

    @Min(value = 1950)
    @Getter
    @Setter
    private Integer year;

    @Min(value = 1)
    @Getter
    @Setter
    private BigDecimal price;

    @Override
    public String toString() {
        return "CarDto{"+ "plateNum='"+ plateNum + '\''
                + ", model='" + model + '\''
                + ", year=" + year + '\''
                + ", price=" + price + '}';
    }

    public CarDto(@NotEmpty(message = "Plate Number can't be empty.") String plateNum, @NotEmpty(message = "Car model can't be empty.") String model, @Min(value = 1950) Integer year, @Min(value = 1) BigDecimal price) {
        this.plateNum = plateNum;
        this.model = model;
        this.year = year;
        this.price = price;
    }
}
