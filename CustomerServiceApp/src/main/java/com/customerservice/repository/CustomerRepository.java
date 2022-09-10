package com.customerservice.repository;

import java.util.Optional;

import com.customerservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
  Optional<Customer> findById(Long id);

  public default Customer saveCustomer(Customer customer)
  {
    return save(customer);
  }
}
