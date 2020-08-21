package com.infor.car.systemtest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("socialSecurityNumber")
    private Long socialSecurityNumber;

    @JsonCreator
    public Customer() {
    }

    @JsonCreator
    public Customer(@JsonProperty("id") Long id, @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName, @JsonProperty("socialSecurityNumber") Long socialSecurityNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.socialSecurityNumber = socialSecurityNumber;
    }
}
