package com.example.mvcDemo.service;

import com.example.mvcDemo.dao.ArticleRepository;
import com.example.mvcDemo.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleRepository articleRepository;

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
        articleRepository.save(article);
        return null;
    }

    @Override
    public Article add(Article article) {
        return articleRepository.insert(article);
    }

    @Override
    public Optional<Article> delete(String id) {
        articleRepository.deleteById(id);
        return null;
    }
}
