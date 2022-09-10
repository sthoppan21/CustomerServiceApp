package com.customerservice.controller;

import com.customerservice.dto.CustomerResponse;
import com.customerservice.model.Customer;
import com.customerservice.repository.CustomerRepository;
import com.customerservice.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

@Tag("unit-test")
@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @InjectMocks
    CustomerController customerController;
    @Mock
    CustomerService customerService;
    @Mock
    CustomerRepository customerRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setMockOutput() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void getCustomerById() throws Exception {
        Customer customer = mockCustomer();
        Mockito.when(customerRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(customer));

        mockMvc.perform(get("http://localhost:8080/v1/api/customer/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        }

    @Test
    void getCustomerById_NotFound() throws Exception {
        Customer customer = mockCustomer();
        Mockito.when(customerRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(customer));

        mockMvc.perform(get("http://localhost:8080/v1/api/customer/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    private CustomerResponse mockResponseDto() {
        CustomerResponse response = new CustomerResponse();
        response.setCustomerId(1234L);
        return response;
    }

    private Customer mockCustomer() {
        Customer customer = new Customer();
        customer.setName("test");
        customer.setEmail("abc@test.com");
        customer.setBirthDate("01/01/1980");
        customer.setSsn("12345678");
        customer.setPhone("1234567890");
        customer.setId(1);
        return customer;
    }

    private Customer mockCustomerRequest() {
        Customer customer = new Customer();
        customer.setName("test");
        customer.setEmail("abc@test.com");
        customer.setBirthDate("01/01/1980");
        customer.setSsn("12345678");
        customer.setPhone("1234567890");
        return customer;
    }

    @Test
    void createCustomer() throws Exception {

        Customer customer = mockCustomer();
        Mockito.when(customerService.createCustomer(Mockito.any(Customer.class))).thenReturn(customer);

        Customer request = mockCustomerRequest();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("http://localhost:8080/v1/api/customer").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId").value("1"));

    }
}