package com.example.mvcDemo.service;

import com.example.mvcDemo.dao.ArticleRepository;
import com.example.mvcDemo.dao.CustomerRepository;
import com.example.mvcDemo.model.Article;
import com.example.mvcDemo.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CustomerServiceImpl customerService;

    @Override
    public List<Article> fetch() {
        return articleRepository.findAll();
    }

    @Override
    public Optional<Article> get(String id) {
        return articleRepository.findById(id);
    }

    @Override
    public Article update(Article article) {
        article.setModified(LocalDateTime.now());
        customerService.updateArticle(article);
        articleRepository.save(article);
        return null;
    }

    @Override
    public Article add(Article article) {
        return articleRepository.insert(article);
    }

    @Override
    public Optional<Article> delete(String id) {
        Optional<Article> toBeDeleted = articleRepository.findById(id);
        if(toBeDeleted.isPresent()) {
            customerService.removeArticle(id);
            articleRepository.deleteById(id);
        }
        return toBeDeleted;
    }
}
