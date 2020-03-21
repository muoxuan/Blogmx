package com.blogmx.controller;

import com.blogmx.pojo.MoreBlog;
import com.blogmx.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;
    @GetMapping("")
    public String getIndex(Model model){
        List<MoreBlog> hot = blogService.getHot();
        model.addAttribute("hotBlogs", hot);
        return "index";
    }
}
