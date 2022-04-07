package com.aggregator.news.web.controllers;

import com.aggregator.news.bsl.services.ArticleService;
import com.aggregator.news.dao.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class ArticleController {

    @Autowired // Here should be a service between Controller and Repository
    ArticleService articleService;


    @RequestMapping(value = "/articles", method = RequestMethod.GET)
    public List<Article> articleList(){
        return articleService.findAll();
    }


    @RequestMapping(value = "/article/{id}", method = RequestMethod.GET)
    public Article singleArticleList(@PathVariable(value="id") long id){
        return articleService.findById(id);
    }


    @RequestMapping(value = "/article", method = RequestMethod.POST)
    public Article saveArticle(@RequestBody @Valid Article article) {
        return articleService.save(article);
    }


    @RequestMapping(value = "/article", method = RequestMethod.DELETE)
    public void deleteArticle(@RequestBody @Valid Article article) {
        articleService.delete(article);
    }


    @RequestMapping(value = "/article/{id}", method = RequestMethod.PUT)
    public Article  updateArticle (@RequestBody @Valid Article article) {
        return articleService.update(article);
    }

}
