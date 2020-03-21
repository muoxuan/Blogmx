package com.blogmx.service;

import com.blogmx.mapper.LableMapper;
import com.blogmx.pojo.Lable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class LableService {
    @Autowired
    private LableMapper lableMapper;

    public List<String> getLablesName(){
        List<Lable> lables = lableMapper.selectAll();
        List<String> list = new LinkedList<>();
        for(Lable lable : lables){
            list.add(lable.getName());
        }
        return list;
    }


}
