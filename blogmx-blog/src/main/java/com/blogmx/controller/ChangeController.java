package com.blogmx.controller;

import com.blogmx.annotation.UserLoginToken;
import com.blogmx.pojo.Blog;
import com.blogmx.service.DeleteService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
@RequestMapping("change")
public class ChangeController {
    @Autowired
    private DeleteService deleteService;

    @UserLoginToken
    @GetMapping("deleteAll")
    public ResponseEntity<Boolean> deleteAll(){
        return ResponseEntity.ok(deleteService.deleteAll());
    }
    @UserLoginToken
    @GetMapping("delete")
    public ResponseEntity<Boolean> delete(@RequestParam("id") Long id){
        return ResponseEntity.ok(deleteService.deleteById(id));
    }
    @UserLoginToken
    @GetMapping("")
    public String upload(){
        testRedis();
        return "uploadTest";
    }
    @Autowired
    private RedisTemplate<String, Blog> blogRedisTemplate;
    @Test
    public void testRedis(){
        Set<Object> blogs = blogRedisTemplate.opsForHash().keys("blogs");
        for(Object obj : blogs){
            System.out.println(obj);
        }
    }


}
