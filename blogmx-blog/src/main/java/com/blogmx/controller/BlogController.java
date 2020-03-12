package com.blogmx.controller;

import com.blogmx.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping("")
    public ResponseEntity<String> getBlogs(@RequestParam("blogId") Long id){
        System.out.println("11111111111111111111111111111111111111111111111111");
        String blogById = blogService.getBlogById(id);
        return ResponseEntity.ok(blogById);
    }

    @GetMapping("{id}.html")
    public String toBlogPage(Model model, @PathVariable("id") Long id){
        System.out.println("2222222");
        if(id == 0){
            return "uploadTest";
        }
        return "read";
    }
    @GetMapping("getBlog")
    public ResponseEntity<Void> getBlog(@RequestParam("blogId") Long id){
        blogService.download();
        return ResponseEntity.ok().build();
    }



}
