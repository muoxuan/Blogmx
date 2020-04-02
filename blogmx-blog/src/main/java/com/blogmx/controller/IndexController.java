package com.blogmx.controller;

import com.blogmx.pojo.MoreBlog;
import com.blogmx.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @GetMapping("")
    public String getIndex(Model model, HttpServletRequest httpServletRequest){
        Cookie[] cookies = httpServletRequest.getCookies();
        if(cookies != null) {
            Cookie cookie = null;
            for (Cookie c : cookies) {
                if (c.getName().equals("test")) {
                    cookie = c;
                }
            }
            if (cookie != null) {
                System.out.println(cookie.getValue());
            } else {
                System.out.println("Null");
            }
        }
        else{
            System.out.println("NO Cookie");
        }
        List<MoreBlog> hot = blogService.getHot();
        model.addAttribute("hotBlogs", hot);
        return "index";
    }


}
