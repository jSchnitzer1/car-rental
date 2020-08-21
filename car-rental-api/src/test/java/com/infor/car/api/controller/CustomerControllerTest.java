package com.infor.car.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.infor.car.api.dto.CustomerDto;
import com.infor.car.api.model.Customer;
import com.infor.car.api.service.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.CoreMatchers.is;

import java.nio.charset.Charset;
import java.util.List;
import static org.junit.Assert.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Value("${api.url.template}")
    private String apiUrlBase;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerServiceMock;

    @MockBean
    private ModelMapper modelMapper;

    private List<Customer> initCustomers;

    @Before
    public void setup() {
        initCustomers = List.of(
                new Customer(1L, "Jane", "Doe", 198101021100L),
                new Customer(2L, "John", "Doe", 199105072211L)
        );
    }

    @Test
    public void findAll() throws Exception {
        when(customerServiceMock.findAll()).thenReturn(initCustomers);

        var resultActions = mockMvc.perform(get(apiUrlBase + "/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].firstName").value("Jane"))
                .andExpect(jsonPath("$.[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.[0].socialSecurityNumber").value("198101021100"));
    }

    @Test
    public void findCustomerById() throws Exception {
        when(customerServiceMock.findById(any())).thenReturn(initCustomers.get(1));

        var resultActions = mockMvc.perform(get(apiUrlBase + "/customers/2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.socialSecurityNumber").value("199105072211"));
    }

    @Test
    public void findCustomerById_RUNTIME_ERROR() throws Exception {
        when(customerServiceMock.findById(any())).thenReturn(initCustomers.get(1));

        var result = mockMvc.perform(get(apiUrlBase + "/customers/invalid")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", is("INVALID_ARGUMENT")));

                Boolean isCorrectErrorMessage = result.andReturn().getResponse().getContentAsString()
                        .contains("\"message\":\"Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'");
                assertTrue(isCorrectErrorMessage);
    }

    @Test
    public void create() throws Exception {
        when(customerServiceMock.create(any())).thenReturn(new Customer(11L, "Tom", "Bob", 198001051177L));

        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirstName("Tom");
        customerDto.setLastName("Bob");
        customerDto.setSocialSecurityNumber(198001051177L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(customerDto);

        var resultActions = mockMvc.perform(post(apiUrlBase + "/customers").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isCreated());

        resultActions.andExpect(jsonPath("$.firstName").value("Tom"))
                .andExpect(jsonPath("$.lastName").value("Bob"))
                .andExpect(jsonPath("$.socialSecurityNumber").value("198001051177"));
    }
}
