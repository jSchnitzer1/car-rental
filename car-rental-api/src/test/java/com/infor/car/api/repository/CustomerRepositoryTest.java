package com.infor.car.api.repository;

import com.infor.car.api.model.Customer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository repository;

    @Test
    public void findAll() {
        final List<Customer> customers = repository.findAll();
        assertNotNull(customers);
        assertEquals(2, customers.size());

        Customer customer_1 = customers.get(0);
        assertEquals("Jane", customer_1.getFirstName());
        assertEquals("Doe", customer_1.getLastName());

        Customer customer_2 = customers.get(1);
        assertEquals("John", customer_2.getFirstName());
        assertEquals("Doe", customer_2.getLastName());
    }
}
