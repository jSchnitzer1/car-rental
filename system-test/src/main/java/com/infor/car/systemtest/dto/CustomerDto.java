package com.infor.car.systemtest.dto;

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

    public CustomerDto(@NotEmpty(message = "First name can't be empty.") String firstName, @NotEmpty(message = "Last name can't be empty.") String lastName, @Digits(integer = 12, fraction = 0) Long socialSecurityNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.socialSecurityNumber = socialSecurityNumber;
    }

    @Override
    public String toString() {
        return "CustomerDto{"+ "firstName='"+ firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", socialSecurityNumber=" + socialSecurityNumber + '}';
    }
}

