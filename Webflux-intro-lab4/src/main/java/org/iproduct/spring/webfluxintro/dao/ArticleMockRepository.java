package org.iproduct.spring.webfluxintro.dao;

import org.iproduct.spring.webfluxintro.model.Article;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class ArticleMockRepository {
    public List<Article> articles = new CopyOnWriteArrayList<>(
//            Arrays.asList(
//                    new Article("1", "title_1", "content_1", LocalDateTime.now()),
//                    new Article("2", "title_2", "content_2", LocalDateTime.now()),
//                    new Article("3", "title_3", "content_3", LocalDateTime.now()),
//                    new Article("4", "title_4", "content_4", LocalDateTime.now())
//            )
    );

    public Flux<Article> find() {
        return Flux.fromIterable(articles);
    }

    public Mono<Article> save(Article art) {
        art.setId(LocalDateTime.now().toString());
        articles.add(art);
        return Mono.just(art);
    }
}
