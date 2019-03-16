package com.example.mvcDemo.service;

import com.example.mvcDemo.dao.CustomerRepository;
import com.example.mvcDemo.model.Article;
import com.example.mvcDemo.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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

    @Override
    public void removeArticle(String id) {
        List<Customer> customers = this.fetch();
        for(Customer customer: customers) {
            boolean removed = false;
            List<Article> articles = customer.getArticles();
            Iterator<Article> i = articles.iterator();
            while(i.hasNext()) {
                Article article = i.next();
                if(article.getId().equals(id)) {
                    i.remove();
                    removed = true;
                }
            }

            if(removed) {
                customer.setArticles(articles);
                this.update(customer);
            }
        }
    }

    @Override
    public void updateArticle(Article article) {
        List<Customer> customers = this.fetch();
        String id = article.getId();
        for(Customer customer: customers) {
            boolean updated = false;
            List<Article> articles = customer.getArticles();
            ListIterator<Article> i = articles.listIterator();
            while(i.hasNext()) {
                Article tempArticle = i.next();
                if(tempArticle.getId().equals(id)) {
                    i.set(article);
                    updated = true;
                }
            }

            if(updated) {
                customer.setArticles(articles);
                this.update(customer);
            }
        }
    }
}
