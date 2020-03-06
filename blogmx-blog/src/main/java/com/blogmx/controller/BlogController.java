package com.blogmx.controller;

import com.blogmx.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/blogs")
    public ResponseEntity<String> getBlogs(@RequestParam("blogId") Long id){
        System.out.println("11111111111111111111111111111111111111111111111111");
        String blogById = blogService.getBlogById(id);
        return ResponseEntity.ok(blogById);


    }


}
