package org.iproduct.spring.webfluxintro.service;

import org.iproduct.spring.webfluxintro.model.Article;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ArticleService {
    public Flux<Article> getAllArticles();

    public Mono<Article> create(Article art);

    public Mono<Article> getArticleById(String id);

    public Mono<Article> delete(String id);
}
