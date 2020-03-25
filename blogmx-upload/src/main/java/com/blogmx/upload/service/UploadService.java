package com.blogmx.upload.service;

import com.blogmx.pojo.Blog;
import com.blogmx.pojo.SearchBlog;
import com.blogmx.repository.BlogRepository;
import com.blogmx.upload.mapper.UpBlogMapper;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class UploadService {

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private UpBlogMapper blogMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;




    /**
     * 将数据保存进数据库
     * @param file
     * @param titleName
     * @param subTitle
     * @param isTop
     * @param isHot
     * @param index
     * @param watchNum
     * @param image
     */
    public int saveBlog(MultipartFile file,
                         String titleName,
                         String subTitle,
                         Boolean isTop,
                         Boolean isHot,
                         String index,
                         Long watchNum,
                         String image)
    {

        Blog blog = new Blog();
        blog.setTitleName(titleName);
        blog.setSubTitle(subTitle);
        blog.setIsHot(isHot);
        blog.setIsTop(isTop);
        blog.setIndex(index);
        blog.setWatchNum(watchNum);
        blog.setImage(image);
        blog.setCreateTime(new Date());
        String url = upload(file);
        blog.setFile(url);
        if(Strings.isBlank(url)){
            return 0;
        }
        int i = blogMapper.insert(blog);
        saveElasticsearch(blog);
        return i;


    }

    public String upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        try {

            // 保存到服务器
            String ext = StringUtils.substringAfterLast(originalFilename, ".");
            StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);

            // 生成url地址，返回
            return "http://47.99.81.136//" + storePath.getFullPath();

        } catch (IOException e) {
            LOGGER.info("服务器内部错误：{}", originalFilename);
            e.printStackTrace();
        }
        return null;
    }

    public void saveElasticsearch(Blog blog){
        SearchBlog temp = new SearchBlog();
        temp.setWatchNum(blog.getWatchNum());
        Long id = blog.getId();
        temp.setUrl("http://www.blogmx.cn/blog/" + blog.getId() + ".html");
        temp.setDay(new SimpleDateFormat("dd").format(blog.getCreateTime()));
        temp.setMonth(new SimpleDateFormat("MM").format(blog.getCreateTime()) + "月");
        temp.setYear(new SimpleDateFormat("yyyy").format(blog.getCreateTime()));
        temp.setTitleName(blog.getTitleName());
        temp.setSubTitle(blog.getSubTitle());
        temp.setIsTop(blog.getIsTop());
        temp.setId(id);
        temp.setIsHot(blog.getIsHot());
        temp.setImage(blog.getImage());
        blogRepository.save(temp);
    }


}
