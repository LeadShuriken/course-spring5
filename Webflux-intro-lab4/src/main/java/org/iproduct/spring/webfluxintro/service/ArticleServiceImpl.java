package org.iproduct.spring.webfluxintro.service;

import org.iproduct.spring.webfluxintro.dao.ArticleRepository;
import org.iproduct.spring.webfluxintro.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleRepository repo;

    public Flux<Article> getAllArticles() {
        return repo.findAll();
    }

    public Mono<Article> create(Article art) {
        return repo.insert(art);
    }

    public Mono<Article> getArticleById(String id) {
        return repo.findById(id);
    }

    public Mono<Article> delete(String id) {
        return repo.findById(id).flatMap(article ->
                repo.deleteById(article.getId()).thenReturn(article)
        );
    }
}
