package com.blogmx.controller;

import com.blogmx.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("{page}")
    public String getArticle(Model model, @PathVariable("page")int page){
        Map<String, Object> article = articleService.getArticle(page);
        model.addAllAttributes(article);
        return "article";
    }

    @GetMapping("")
    public String getIndexArticle(Model model){
        Map<String, Object> article = articleService.getArticle(1);
        model.addAllAttributes(article);
        return "article";
    }
}
