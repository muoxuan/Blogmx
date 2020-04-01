package com.blogmx.service;

import com.blogmx.mapper.BlogMapper;
import com.blogmx.mapper.LableMapper;
import com.blogmx.pojo.Blog;
import com.blogmx.pojo.Lable;
import com.blogmx.pojo.MoreBlog;
import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private LableMapper lableMapper;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate<String, Blog> blogRedisTemplate;

    @Autowired
    private RedisTemplate<String, Date> redisTemplate;

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
        List<Lable> lables = null;
        Blog blog = null;
        blog = (Blog)blogRedisTemplate.opsForHash().get("blogs", id.toString());
        if(blog == null){
            blog = blogMapper.selectByPrimaryKey(id);
            if(blog == null){
                return null;
            }
            blogRedisTemplate.opsForHash().put("blogs", blog.getId().toString(), blog);
        }
        if(blog == null){
            return null;
        }
        lables = lableMapper.selectAll();
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
        String[] strs = blog.getIndex().split(",");
        List<String> indexs = new LinkedList<>();
        for(int i = 0; i < strs.length; i++){
            String name = isBelongLables(Long.parseLong(strs[i]), lables);
            if(!name.equals("")){
                indexs.add(name);
            }
        }
        map.put("lables", indexs);
        return map;
    }

    public String isBelongLables(Long id, List<Lable> lables){
        for(Lable lable : lables){
            if(id == lable.getId()){
                return lable.getName();
            }
        }
        return "";
    }

    public List<MoreBlog> getHot(){
        Blog blog = new Blog();
        blog.setIsHot(true);
        List<Blog> select = blogMapper.select(blog);
        List<MoreBlog> list = new ArrayList<>();
        for(Blog b : select){
            Date createTime = b.getCreateTime();
            MoreBlog temp = new MoreBlog();
            temp.setB(b);
            temp.setYear(new SimpleDateFormat("yyyy年MM月dd日").format(createTime));
            temp.setUrl("http://www.blogmx.cn/blog/" + b.getId() + ".html");
            list.add(temp);
        }
        return list;
    }

    public List<MoreBlog> getTop(){
        Blog blog = new Blog();
        blog.setIsTop(true);
        List<Blog> select = blogMapper.select(blog);
        List<MoreBlog> list = new ArrayList<>();
        for(Blog b : select){
            MoreBlog temp = new MoreBlog();
            temp.setB(b);
            temp.setUrl("http://www.blogmx.cn/blog/" + b.getId() + ".html");
            list.add(temp);
        }
        return list;
    }

    public void createHtml(Long id){
        //初始化运行上下文
        Context context = new Context();
        //设置数据模型
        context.setVariables(loadBlog(id));
        PrintWriter printWriter = null;
        try {
            //静态文件生成到nginx本地
            File file = new File("G:\\nginx\\nginx-1.14.0\\nginx-1.14.0\\html\\blog\\" + id + ".html");
            printWriter = new PrintWriter(file);

            templateEngine.process("read", context, printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(printWriter != null){
                printWriter.close();
            }
        }
    }

    public void addWatchNum(Long id){
        Blog blog = (Blog)blogRedisTemplate.opsForHash().get("blogs", id.toString());
        blogRedisTemplate.opsForHash().delete("blogs", id.toString());
        blog.setWatchNum(blog.getWatchNum() + 1);
        blogRedisTemplate.opsForHash().put("blogs", blog.getId().toString(), blog);
        Date time = redisTemplate.opsForValue().get("catchDate");
        if(time == null){
            redisTemplate.opsForValue().set("catchDate", new Date(), 2, TimeUnit.HOURS);
            List<Object> blogs = blogRedisTemplate.opsForHash().values("blogs");
            for(Object obj : blogs){
                Blog b = (Blog) obj;
                blogMapper.updateByPrimaryKey(b);
            }
            //System.out.println(time.toString());
        }

    }



}
