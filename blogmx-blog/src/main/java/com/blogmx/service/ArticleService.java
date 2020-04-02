package com.blogmx.service;

import com.blogmx.mapper.BlogMapper;
import com.blogmx.pojo.Blog;
import com.blogmx.pojo.MoreBlog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class ArticleService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private BlogService blogService;

    @Autowired
    private RedisTemplate<String, Blog> blogRedisTemplate;

    public List<MoreBlog> getArticle(int page){

        return getArticle(page, 5);
    }

    public List<MoreBlog> getArticle(int page, int rows){



        //List<Object> blogs = null;
        List<MoreBlog> list1;
        //blogs =  blogRedisTemplate.opsForHash().values("blogs");
            blogRedisTemplate.delete("blogs");
            List<Blog> blogs1 = blogMapper.selectAll();
            list1 = getMoreBlogByBlogs(blogs1);
            for(Blog b : blogs1){
                blogRedisTemplate.opsForHash().put("blogs", b.getId().toString(), b);
            }
            System.out.println("数据同步...");



        if(rows == -1){
            return list1;
        }

        page = (page - 1) * rows;

        if(page >= list1.size()){
            return null;
        }
        return list1.subList(page, Math.min(list1.size(), page + rows));
    }

    public List<MoreBlog> getMoreBlogByObjects(List<Object> blogs){
        List<MoreBlog> list1 = new LinkedList<>();
        List<MoreBlog> list2 = new LinkedList<>();
        for(Object b : blogs){
            Blog blog = (Blog) b;
            MoreBlog moreBlog = new MoreBlog();
            moreBlog.setB(blog);
            moreBlog.setDay(new SimpleDateFormat("dd").format(blog.getCreateTime()));
            moreBlog.setMonth(new SimpleDateFormat("MM").format(blog.getCreateTime()) + "月");
            moreBlog.setYear(new SimpleDateFormat("yyyy").format(blog.getCreateTime()));
            moreBlog.setUrl("http://www.blogmx.cn/blog/" + blog.getId() + ".html");
            moreBlog.setSubtitle(blogService.mdToHtml(blogService.download(blog.getFile())));
            if(blog.getIsTop()){
                list1.add(moreBlog);
            }
            else{
                list2.add(moreBlog);
            }
        }
        list1.addAll(list2);
        return list1;
    }
    public List<MoreBlog> getMoreBlogByBlogs(List<Blog> blogs){
        List<MoreBlog> list1 = new LinkedList<>();
        List<MoreBlog> list2 = new LinkedList<>();
        for(Blog blog : blogs){
            MoreBlog moreBlog = new MoreBlog();
            moreBlog.setB(blog);
            moreBlog.setDay(new SimpleDateFormat("dd").format(blog.getCreateTime()));
            moreBlog.setMonth(new SimpleDateFormat("MM").format(blog.getCreateTime()) + "月");
            moreBlog.setYear(new SimpleDateFormat("yyyy").format(blog.getCreateTime()));
            moreBlog.setUrl("http://www.blogmx.cn/blog/" + blog.getId() + ".html");
            //moreBlog.setSubtitle(blogService.mdToHtml(blogService.download(blog.getFile())));
            if(blog.getIsTop()){
                list1.add(moreBlog);
            }
            else{
                list2.add(moreBlog);
            }
        }
        list1.addAll(list2);
        return list1;
    }

    public Boolean saveBlog(Blog blog){
        blogRedisTemplate.opsForHash().put("blogs", blog.getId().toString(), blog);
        return true;
    }


}
