package com.blogmx.service;

import com.blogmx.mapper.BlogMapper;
import com.blogmx.pojo.Blog;
import com.blogmx.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class DeleteService {

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private RedisTemplate<String, Blog> blogRedisTemplate;
    @Autowired
    private BlogRepository blogRepository;

    public Boolean deleteAll(){
        int delete = 0;
        Boolean blogs = false;
        try {
            delete= blogMapper.delete(new Blog());
            blogs = blogRedisTemplate.delete("blogs");
            blogRepository.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return delete > 0 && blogs;
    }

    public Boolean deleteById(Long id){
        int delete = 0;
        Long blogs = 0L;
        try {
            Blog blog = new Blog();
            blog.setId(id);
            delete= blogMapper.delete(blog);
            Object blog1 = blogRedisTemplate.opsForHash().get("blog", id.toString());
            blog = (Blog) blog1;
            blogs = blogRedisTemplate.opsForHash().delete("blogs", id.toString());
            blogRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return delete > 0 && blogs > 0;
    }



}
