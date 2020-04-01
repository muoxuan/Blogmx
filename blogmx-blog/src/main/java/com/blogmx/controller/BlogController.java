package com.blogmx.controller;

import com.blogmx.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    /**
     * 获取博客详情页
     * @param model
     * @param id
     * @return
     */
    @GetMapping("{id}.html")
    public String toBlogPage(Model model, @PathVariable("id") Long id){
        Map<String, Object> map = blogService.loadBlog(id);
        if(map == null){
            return "badblog";
        }
        model.addAllAttributes(map);
        blogService.createHtml(id);
        return "read";
    }

    @GetMapping("/add/{id}")
    public ResponseEntity<Void> toBlogPageFirst(@PathVariable("id") Long id){
        blogService.addWatchNum(id);
        return ResponseEntity.ok().build();
    }

}
