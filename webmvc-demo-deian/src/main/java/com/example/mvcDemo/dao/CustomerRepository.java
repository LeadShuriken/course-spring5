package com.example.mvcDemo.dao;

import com.example.mvcDemo.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    @Override
    List<Customer> findAll();
    @Override
    Optional<Customer> findById(String s);
}
