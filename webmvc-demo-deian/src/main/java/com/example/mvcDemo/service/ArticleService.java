package com.example.mvcDemo.service;

import com.example.mvcDemo.model.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    List<Article> fetch();
    Optional<Article> get(String id);
    Article update(Article article);
    Article add(Article article);
    Optional<Article> delete(String id);
}
