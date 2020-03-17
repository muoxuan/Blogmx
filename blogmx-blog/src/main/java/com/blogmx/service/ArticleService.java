package com.blogmx.service;


import com.blogmx.mapper.BlogMapper;
import com.blogmx.pojo.Blog;
import com.blogmx.pojo.MoreBlog;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ArticleService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private BlogService blogService;


    public Map<String, Object> getArticle(int page){

        List<Blog> blogs = blogMapper.selectAll();
        List<MoreBlog> list1 = new LinkedList<>();
        List<MoreBlog> list2 = new LinkedList<>();
        for(Blog blog : blogs){
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
        Map<String, Object> map = new HashMap<>();
        map.put("blogs", list1.subList(page - 1, page + 3));
        return map;
    }
}
