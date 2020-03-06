package com.blogmx.service;

import org.springframework.stereotype.Service;

import java.io.*;

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


}
