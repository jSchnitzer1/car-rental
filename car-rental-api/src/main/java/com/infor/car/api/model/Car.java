package com.infor.car.api.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@ToString
@EqualsAndHashCode
@Entity
@Table(name = "Car")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @Column(name = "PLATE_NUM", nullable = false)
    private String plateNum;

    @Column(name = "MODEL", length = 15)
    @Size(min=2, max=15)
    @NonNull
    private String model;

    @Column(name = "YEAR")
    @NonNull
    private Integer year;

    @Column(name = "PRICE")
    @NonNull
    private BigDecimal price;
}
