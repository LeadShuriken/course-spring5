package com.example.mvcDemo.dao;

import com.example.mvcDemo.model.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends MongoRepository<Article, String> {
    @Override
    List<Article> findAll();
    @Override
    Optional<Article> findById(String s);
}