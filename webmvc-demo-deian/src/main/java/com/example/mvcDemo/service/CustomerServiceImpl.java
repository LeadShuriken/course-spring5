package com.example.mvcDemo.service;

import com.example.mvcDemo.dao.CustomerRepository;
import com.example.mvcDemo.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Override
    public List<Customer> fetch() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> get(String id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer update(Customer customer) {
        customer.setModified(LocalDateTime.now());
        customerRepository.save(customer);
        return null;
    }

    @Override
    public Customer add(Customer customer) {
        return customerRepository.insert(customer);
    }

    @Override
    public Optional<Customer> delete(String id) {
        Optional<Customer> toBeDeleted = customerRepository.findById(id);
        if(toBeDeleted.isPresent()) {
            customerRepository.deleteById(id);
        }
        return toBeDeleted;
    }
}
