package com.blogmx.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class BlogService {


    /**
     *
     * 根据地址获取md文件
     */
    public void download(String urlString){
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
            while ((line = buffer.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(sb.toString());

    }


}
