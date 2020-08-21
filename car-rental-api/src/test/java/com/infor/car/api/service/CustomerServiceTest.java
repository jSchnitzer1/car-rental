package com.infor.car.api.service;

import com.infor.car.api.dto.CustomerDto;
import com.infor.car.api.exception.EntityExistsException;
import com.infor.car.api.exception.EntityNotFoundException;
import com.infor.car.api.exception.ErrorCode;
import com.infor.car.api.model.Customer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void findAll() {
        List<Customer> customers = customerService.findAll();
        assertThat(customers.size(), greaterThan(0));

        Customer customer_1 = customers.get(0);
        assertEquals("Jane", customer_1.getFirstName());
        assertEquals("Doe", customer_1.getLastName());
        assertThat(customer_1.getSocialSecurityNumber(), is(198101021100L));

        Customer customer_2 = customers.get(1);
        assertEquals("John", customer_2.getFirstName());
        assertEquals("Doe", customer_2.getLastName());
        assertThat(customer_2.getSocialSecurityNumber(), is(199105072211L));
    }

    @Test
    public void findById() {
        Customer customer = customerService.findById(1L);
        assertNotNull(customer);

        assertEquals("Jane", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertThat(customer.getSocialSecurityNumber(), is(198101021100L));
    }

    @Test
    public void customerEntityNotFoundException() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                customerService.findById(7L));
        assertTrue(exception.getMessage().equals("Customer with the given id [7] doesn't exists"));
        assertThat(exception.getErrorCode(), is(ErrorCode.RESOURCE_NOT_FOUND));
    }

    @Test
    public void createCustomer() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirstName("Bob");
        customerDto.setLastName("Johnson");
        customerDto.setSocialSecurityNumber(199009081155L);
        Customer customer = modelMapper.map(customerDto, Customer.class);

        assertThat(customerService.create(customer), is(customer));

        List<Customer> customers = customerService.findAll();
        assertNotNull(customers);
        assertThat(customers.size(), is(3));
    }

    @Test
    public void createEntityExistsException() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirstName("Jane");
        customerDto.setLastName("Doe");
        customerDto.setSocialSecurityNumber(198101021100L);
        Customer customer = modelMapper.map(customerDto, Customer.class);

        EntityExistsException exception = assertThrows(EntityExistsException.class, () ->
                customerService.create(customer));
        assertTrue(exception.getMessage().equals("Customer with the given social security number already exists:198101021100"));
        assertThat(exception.getErrorCode(), is(ErrorCode.RESOURCE_ALREADY_EXISTS));
    }
}
