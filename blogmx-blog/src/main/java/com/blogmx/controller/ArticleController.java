package com.blogmx.controller;

import com.blogmx.pojo.MoreBlog;
import com.blogmx.pojo.SearchBlog;
import com.blogmx.service.ArticleService;
import com.blogmx.service.BlogService;
import com.blogmx.service.LableService;
import com.blogmx.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private LableService lableService;
    @Autowired
    private BlogService blogService;


    @GetMapping("{page}")
    public ResponseEntity<List<MoreBlog>> getArticle(@PathVariable("page")int page){
        List<MoreBlog> article = articleService.getArticle(page);
        if(article == null){
            return ResponseEntity.ok(new ArrayList<>());
        }
        return ResponseEntity.ok(article);
    }

    @GetMapping("")
    public String toArticle(Model model){
        Map<String, Object> map = new HashMap<>();
        List<String> lablesName = lableService.getLablesName();
        map.put("lables", lablesName);
        map.put("hot", blogService.getHot());
        map.put("top",blogService.getTop());
        model.addAllAttributes(map);
        return "article";
    }

    @GetMapping("/search")
    public String toSearch(Model model, @RequestParam("key") String key){
        Map<String, Object> map = new HashMap<>();
        List<String> lablesName = lableService.getLablesName();
        map.put("lables", lablesName);
        map.put("hot", blogService.getHot());
        map.put("top",blogService.getTop());
        map.put("key", key);
        model.addAllAttributes(map);
        return "search";
    }

    @Autowired
    private SearchService searchService;

    @GetMapping("/search/{key}/{page}")
    public ResponseEntity<List<SearchBlog>> toSearchBlog(@PathVariable("key")String key, @PathVariable("page")int page){
        List<SearchBlog> blogsByKey = searchService.getBlogsByKey(key, page);
        return ResponseEntity.ok(blogsByKey);
    }

}
