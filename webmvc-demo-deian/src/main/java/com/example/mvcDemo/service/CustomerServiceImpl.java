package com.example.mvcDemo.service;

import com.example.mvcDemo.dao.CustomerRepository;
import com.example.mvcDemo.model.Article;
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
    public List<Article> fetch() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Article> get(String id) {
        return customerRepository.findById(id);
    }

    @Override
    public Article update(Article article) {
        article.setModified(LocalDateTime.now());
        customerRepository.save(article);
        return null;
    }

    @Override
    public Article add(Article article) {
        return customerRepository.insert(article);
    }

    @Override
    public Optional<Article> delete(String id) {
        Optional<Article> toBeDeleted = customerRepository.findById(id);
        if(toBeDeleted.isPresent()) {
            customerRepository.deleteById(id);
        }
        return toBeDeleted;
    }
}
