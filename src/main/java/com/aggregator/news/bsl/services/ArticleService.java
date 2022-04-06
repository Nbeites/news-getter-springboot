package com.aggregator.news.bsl.services;

import com.aggregator.news.dao.Article;
import com.aggregator.news.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    ArticleRepository articleRepository;

    public List<Article> findAll(){
        return articleRepository.findAll();
    }

    public Article findById(long id){
        return articleRepository.findById(id);
    }

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public void delete(@RequestBody @Valid Article article) {
        articleRepository.delete(article);
    }

    public Article update (Article article){

        articleRepository.save(article);

        return article;
    };
}