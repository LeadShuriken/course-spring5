package org.iproduct.spring.webfluxintro.init;

import lombok.extern.slf4j.Slf4j;
import org.iproduct.spring.webfluxintro.dao.ArticleRepository;
import org.iproduct.spring.webfluxintro.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ArticleRepository repo;

    @Override
    public void run(String... args) throws Exception {
        log.info("Started initialization ...");
        repo.deleteAll().thenMany(
                Flux.just("Article 1", "Article 2", "Article 3", "Article 3")
        ).flatMap(title -> repo.insert(
                Article.builder().title(title).content(title + " content").build()
        )).log().subscribe(null, null, () -> log.info("Initialization done..."));
    }
}
