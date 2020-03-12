package com.blogmx.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class BlogService {

    public String getBlogById(Long id){
        File file = new File("C:\\Users\\14699\\Desktop\\new\\" + id.toString() + ".md");
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
                sbf.append("\n");
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    public void download(){
        StringBuffer sb = new StringBuffer();  //存放下载的文件序列
        String line = null;
        BufferedReader buffer = null;

        // 创建一个URL对象
        String urlString = "http://file.mxblog.cn/group1/M00/00/00/rBAohl5qMOOATg5MAAAJnQijsOk1745.md";
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
