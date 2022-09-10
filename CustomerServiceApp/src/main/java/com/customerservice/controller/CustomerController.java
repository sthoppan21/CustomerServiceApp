package com.customerservice.controller;

import java.util.*;

import com.customerservice.dto.CustomerResponse;
import com.customerservice.exception.MissingFieldsException;
import com.customerservice.model.Customer;
import com.customerservice.repository.CustomerRepository;
import com.customerservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/v1/api")
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@Autowired
	CustomerRepository customerRepository;

	@GetMapping("/customer/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable("id") long id) {
		Optional<Customer> customerData = customerRepository.findById(id);

		if (customerData.isPresent()) {
			return new ResponseEntity<>(customerData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/customer")
	public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody Customer customer) throws MissingFieldsException, Exception {
			Customer savedCustomer = customerService.createCustomer(customer);
			CustomerResponse customerResponse = new CustomerResponse();
			customerResponse.setCustomerId(savedCustomer.getId());
			return new ResponseEntity<CustomerResponse>(customerResponse, HttpStatus.CREATED);

	}
}
