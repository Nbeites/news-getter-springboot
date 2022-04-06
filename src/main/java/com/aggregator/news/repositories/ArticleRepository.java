package com.aggregator.news.repositories;

import com.aggregator.news.dao.Article;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ArticleRepository extends JpaRepository<Article, Long> {

    Article findById(long id);

}