package com.infor.car.api.controller;

import com.infor.car.api.dto.CustomerDto;
import com.infor.car.api.model.Customer;
import com.infor.car.api.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Validated
public class CustomerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<Customer>> findAllCustomers() {
        LOGGER.info("Getting all customers");
        return new ResponseEntity<>(customerService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> findCustomerById(@PathVariable @Valid @Min(value = 1) Long id) {
        LOGGER.info("Getting a customer with id {}", id);
        return new ResponseEntity<>(customerService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCustomer(@PathVariable @Valid @Min(value = 1) Long id) {
        LOGGER.info("Deleting the customer with id {}", id);
        customerService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody @Valid @NotNull CustomerDto customer) {
        LOGGER.info("Creating customer {} {}", customer.getFirstName(), customer.getLastName());
        return new ResponseEntity<>(customerService.create(modelMapper.map(customer, Customer.class)),
                HttpStatus.CREATED);
    }
}
