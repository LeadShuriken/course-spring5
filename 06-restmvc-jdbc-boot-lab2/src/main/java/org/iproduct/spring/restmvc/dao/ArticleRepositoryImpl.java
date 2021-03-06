package org.iproduct.spring.restmvc.dao;

import lombok.extern.slf4j.Slf4j;
import org.iproduct.spring.restmvc.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class ArticleRepositoryImpl implements ArticleRepository {
    public static final String INSERT_SQL =
        "INSERT INTO articles(id, title, content, picture_url, created, updated) VALUES (DEFAULT, ?, ?, ?, ?, ?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ArticleMapper articleMapper;

   @Override
    public Collection<Article> findAll() {
        List<Article> articles = jdbcTemplate.query("select * from articles", articleMapper);
        log.info("Articles loaded: {}", articles);
        return articles;
    }

    @Override
    public Optional<Article> findById(long id) {
        Article article = jdbcTemplate.queryForObject(
                "select * from articles where id = ?",
                new Object[] {id}, articleMapper);
        log.info("Article created: {}", article);
        return Optional.ofNullable(article);
    }

    @Override
    public Article insert(Article article) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int created = jdbcTemplate.update( new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(INSERT_SQL, new String[] {"id"});
                ps.setString(1, article.getTitle());
                ps.setString(2, article.getContent());
                ps.setString(3, article.getPictureUrl());
                ps.setTimestamp(4, new Timestamp(
                        article.getCreated().toInstant(ZoneOffset.UTC).toEpochMilli()));
                ps.setTimestamp(5, new Timestamp(
                        article.getUpdated().toInstant(ZoneOffset.UTC).toEpochMilli()));
                return ps;
            }
        }, keyHolder);
        article.setId(keyHolder.getKey().longValue());
        return created > 0 ? article: null;
    }

    @Override
    public Article save(Article article) {
        int count = this.jdbcTemplate.update(
                "update articles set (title, content, picture_url, created, updated) = (?, ?, ?, ?, ?) where id = ?",
                article.getTitle(),
                article.getContent(),
//                article.getAuthorId(),
                article.getPictureUrl(),
                article.getCreated(),
                article.getUpdated(),
                article.getId());
        log.info("Article updated: {}", article);
        return article;
    }

    @Override
    public boolean deleteById(long articleId) {
        int count = this.jdbcTemplate.update(
                "delete from articles where id = ?",
                Long.valueOf(articleId));
        return count > 0;
    }

    @Override
    public long count() {
        PreparedStatementCreator p;
        return jdbcTemplate.queryForObject("select count(*) from articles", Long.class);
    }

    @PostConstruct
    public void initDb() {
        log.info("Start data initialization  ...");
//        jdbcTemplate.execute("DROP TABLE IF EXISTS articles");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS articles(" +
                "id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, " +
                "title VARCHAR(80), " +
                "content VARCHAR(512), " +
                "picture_url VARCHAR(128), " +
                "created TIMESTAMP, " +
                "updated TIMESTAMP" +
                ")");

        int articlesCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM articles", Integer.class);
        log.info("Articles count: {}", articlesCount);

        if (articlesCount == 0) {
            List<Article> articles = Arrays.asList(
                    new Article("Welcome to Spring 5", "Spring 5 is great beacuse ..."),
                    new Article("Dependency Injection", "Should I use DI or lookup ..."),
                    new Article("Spring Beans and Wireing", "There are several ways to configure Spring beans.")
            );

            // Use a Java 8 stream to print out each tuple of the list
            articles.forEach(article -> {
                log.info(String.format("Inserting article record for %s %s",
                        article.getTitle(), article.getContent()));
                jdbcTemplate.update("INSERT INTO articles(id, title, content, picture_url, created, updated) VALUES (DEFAULT, ?, ?, ?, ?, ?)",
                        article.getTitle(), article.getContent(), article.getPictureUrl(),
                        article.getCreated(),
                        article.getUpdated()
                );
            });
        }

        log.info("Querying for article records where title contains = 'Spring':");
        jdbcTemplate.query(
                "SELECT id, title, content, picture_url, created, updated FROM articles WHERE title LIKE ?", new Object[]{"%Spring%"},
                articleMapper
        ).forEach(article -> log.info(article.toString()));
    }

}
