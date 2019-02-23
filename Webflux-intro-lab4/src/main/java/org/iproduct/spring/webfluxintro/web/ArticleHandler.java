package org.iproduct.spring.webfluxintro.web;

import org.iproduct.spring.webfluxintro.model.Article;
import org.iproduct.spring.webfluxintro.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;

@Component
public class ArticleHandler {

    @Autowired
    private ArticleService service;

    private Function<Article, Mono<ServerResponse>> myFirstLambda =
            article -> ServerResponse.ok().body(Mono.just(article),
                    Article.class);

    public Mono<ServerResponse> all(ServerRequest req) {
        return ServerResponse.ok()
                .body(service.getAllArticles(), Article.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest req) {
        return req.bodyToMono(Article.class)
                .flatMap(article -> service.create(article))
                .flatMap(article -> ServerResponse.created(URI.create(article.getId())
                ).build());
    }

    public Mono<ServerResponse> get(ServerRequest req) {
        return service.getArticleById(req.pathVariable("id"))
                .flatMap(myFirstLambda)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> remove(ServerRequest req) {
        return service.delete(req.pathVariable("id"))
                .flatMap(myFirstLambda)
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}