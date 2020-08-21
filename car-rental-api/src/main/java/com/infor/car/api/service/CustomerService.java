package com.infor.car.api.service;

import com.infor.car.api.exception.EntityExistsException;
import com.infor.car.api.exception.EntityNotFoundException;
import com.infor.car.api.exception.ErrorCode;
import com.infor.car.api.model.Customer;
import com.infor.car.api.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository repository;

    @Transactional
    public List<Customer> findAll(){
        return repository.findAll();
    }

    @Transactional
    public Customer findById(Long id) {
        Optional<Customer> customer = repository.findById(id);
        if (!customer.isPresent()){
            LOGGER.error("Customer with the given id [{}] doesn't exists", id);
            throw new EntityNotFoundException("Customer with the given id [" + id + "] doesn't exists", ErrorCode.RESOURCE_NOT_FOUND);
        }
        return customer.get();
    }

    @Transactional
    @Modifying
    public Customer create(Customer customer) {
        Optional<Customer> existingCustomer = repository.findBySocialSecurityNumber(customer.getSocialSecurityNumber());
        if (existingCustomer.isPresent()) {
            Customer c = existingCustomer.get();
            LOGGER.error("Customer with the social security number already exists: {} {}", c.getFirstName(), c.getLastName());
            throw new EntityExistsException("Customer with the given social security number already exists:" + customer.getSocialSecurityNumber(), ErrorCode.RESOURCE_ALREADY_EXISTS);
        }

        return repository.save(customer);
    }

    @Transactional
    @Modifying
    public void delete(Long customerId){
        Customer customer = repository.findById(customerId).orElseThrow(() ->
                new EntityNotFoundException("Customer with the given id [" + customerId + "] doesn't exists", ErrorCode.RESOURCE_NOT_FOUND));
        repository.delete(customer);
    }
}
