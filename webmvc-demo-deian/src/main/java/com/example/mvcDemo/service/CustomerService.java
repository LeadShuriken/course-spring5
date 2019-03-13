package com.example.mvcDemo.service;

import com.example.mvcDemo.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> fetch();
    Optional<Customer> get(String id);
    Customer update(Customer customer);
    Customer add(Customer customer);
    Optional<Customer> delete(String id);
}
