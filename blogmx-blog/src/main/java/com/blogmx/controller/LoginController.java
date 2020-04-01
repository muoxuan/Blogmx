package com.blogmx.controller;


import com.blogmx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@RequestParam("name") String name,
                                         @RequestParam("email") String email,
                                         @RequestParam("password") String password){
        Boolean register = userService.register(name, email, password);
        if(register) {
            return ResponseEntity.ok(true);
        }
        else{
            return ResponseEntity.ok(false);
        }


    }

    @PostMapping("/log")
    public ResponseEntity<Boolean> login(@RequestParam("name") String name,
                                         @RequestParam("password") String password,
                                         HttpServletResponse httpServletResponse){
        String token = userService.login(name, password);
        if(token != null) {
            Cookie cookie = new Cookie("MX_SID", token);
            httpServletResponse.addCookie(cookie);
            return ResponseEntity.ok(true);
        }
        else{
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/check/{name}")
    public ResponseEntity<Boolean> checkName(@PathVariable("name") String name){
        return ResponseEntity.ok(userService.chickUserName(name));
    }
}
