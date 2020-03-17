package com.blogmx.service;

import com.blogmx.mapper.BlogMapper;
import com.blogmx.pojo.Blog;
import com.blogmx.pojo.MoreBlog;
import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BlogService {
    @Autowired
    private BlogMapper blogMapper;

    /**
     *
     * 根据地址获取md文件
     */
    public String download(String urlString){
        StringBuffer sb = new StringBuffer();  //存放下载的文件序列
        String line = null;
        BufferedReader buffer = null;
        // 创建一个URL对象
        //String urlString = "http://file.mxblog.cn/group1/M00/00/00/rBAohl5qMOOATg5MAAAJnQijsOk1745.md";
        try {
            URL url = new URL(urlString);
            // 创建一个Http连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 使用IO流读取数据
            buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            boolean flag = false;
            while ((line = buffer.readLine()) != null) {
                if(flag){
                    sb.append(line);
                    sb.append("\n");
                }
                else{
                    flag = true;
                }
            }
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String mdToHtml(String markdown){
        MutableDataSet options = new MutableDataSet();
        options.setFrom(ParserEmulationProfile.MARKDOWN);
        options.set(Parser.EXTENSIONS, Arrays.asList(new Extension[] { TablesExtension.create()}));
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(markdown);
        String html = renderer.render(document);
        return html;
    }


    public Map<String, Object> loadBlog(Long id){
        Blog blog = blogMapper.selectByPrimaryKey(id);
        if(blog == null){
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("watchNum", blog.getWatchNum());
        Date createTime = blog.getCreateTime();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("year", new SimpleDateFormat("yyyy").format(createTime));
        map.put("month", new SimpleDateFormat("MM").format(createTime) + "月");
        map.put("day", new SimpleDateFormat("dd").format(createTime));
        map.put("CreateTime", sdf.format(createTime));
        map.put("TitleName", blog.getTitleName());
        map.put("blog", mdToHtml(download(blog.getFile())));
        return map;
    }




}
