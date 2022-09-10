package com.customerservice.service;

import com.customerservice.exception.MissingFieldsException;
import com.customerservice.model.Customer;
import com.customerservice.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class CustomerService {

    private static Logger log = LoggerFactory.getLogger(CustomerService.class);

    @Value("${host.url}")
    private String hostUrl;

    @Autowired
    private Validator validator;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    public Customer createCustomer(Customer customer) throws InterruptedException, ExecutionException, MissingFieldsException
    {
        validateRequest(customer);
        log.info("Email Validation Thread starts...");
        CompletableFuture<Boolean> isValidEmailAddress = emailValidator();
        log.info("Email Validation Thread completed...");

        log.info("Phone Validation Thread starts...");
        CompletableFuture<Boolean> isValidPhone = phoneValidator();
        log.info("Phone Validation Thread completed...");

        log.info("SSN Validation Thread starts...");
        CompletableFuture<Boolean> isValidSSN = ssnValidator();
        log.info("SSN Validation Thread completed...");

        CompletableFuture.allOf(isValidEmailAddress, isValidPhone, isValidSSN).join();

        Customer savedCustomer = customerRepository.saveCustomer(customer);

        return savedCustomer;
    }

    public void validateRequest(Customer customer) throws MissingFieldsException
    {
        if(customer.getSsn().isEmpty() && customer.getBirthDate().isEmpty())
            throw new MissingFieldsException("Please enter both SSN and Birthdate fields");
    }

    @Async("asyncExecutor")
    public CompletableFuture<Boolean> emailValidator() throws InterruptedException
    {
        long startTime = System.currentTimeMillis();
        log.info("Email Validation start time...,{}",startTime);

        boolean isEmailValid = restTemplate.getForObject(hostUrl+"/v1/api/emailvalidate", boolean.class);

        log.info("isEmailValid, {}", isEmailValid);
        Thread.sleep(1000L);  //Wait for sometime

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        log.info("Email Validation completion time...,{}", totalTime);

        Boolean isValidEmail = Boolean.valueOf(isEmailValid);
        return CompletableFuture.completedFuture(isValidEmail);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Boolean> phoneValidator() throws InterruptedException
    {
        long startTime = System.currentTimeMillis();
        log.info("Phone Validation start time...,{}",startTime);

        boolean isPhoneValid = restTemplate.getForObject(hostUrl+"/v1/api/phonevalidate", boolean.class);

        log.info("isPhoneValid, {}", isPhoneValid);
        Thread.sleep(1500L);  //Wait for sometime

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        log.info("Phone Validation completion time...,{}", totalTime);

        Boolean isValidPhone = Boolean.valueOf(isPhoneValid);
        return CompletableFuture.completedFuture(isValidPhone);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Boolean> ssnValidator() throws InterruptedException
    {
        long startTime = System.currentTimeMillis();
        log.info("SSN Validation start time...,{}",startTime);

        boolean isSSNValid = restTemplate.getForObject(hostUrl+"/v1/api/ssnvalidate", boolean.class);

        log.info("isSSNValid, {}", isSSNValid);
        Thread.sleep(2000L);  //Wait for sometime

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        log.info("Email Validation completion time...,{}", totalTime);

        Boolean isValidSSN = Boolean.valueOf(isSSNValid);
        return CompletableFuture.completedFuture(isValidSSN);
    }

}
