package com.infor.car.api.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

public class CustomerDto {

    @NotEmpty(message = "First name can't be empty.")
    @Getter
    @Setter
    private String firstName;

    @NotEmpty(message = "Last name can't be empty.")
    @Getter
    @Setter
    private String lastName;

    @Digits(integer=12, fraction=0)
    @Getter
    @Setter
    private Long socialSecurityNumber;

    @Override
    public String toString() {
        return "CustomerDto{"+ "firstName='"+ firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", socialSecurityNumber=" + socialSecurityNumber + '}';
    }
}

