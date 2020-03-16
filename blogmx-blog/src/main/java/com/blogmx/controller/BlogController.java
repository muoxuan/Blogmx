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

import java.util.Map;

@Controller
@RequestMapping("blog")
public class BlogController {

    @Autowired
    private BlogService blogService;



    @GetMapping("{id}.html")
    public String toBlogPage(Model model, @PathVariable("id") Long id){
        if(id == 0){
            return "uploadTest";
        }
        Map<String, Object> map = blogService.loadBlog(id);
        model.addAllAttributes(map);

        return "read";
    }
    @GetMapping("getBlog")
    public ResponseEntity<Void> getBlog(@RequestParam("blogId") Long id){

        //blogService.download();
        return ResponseEntity.ok().build();
    }





}
