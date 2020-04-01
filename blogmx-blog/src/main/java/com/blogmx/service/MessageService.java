package com.blogmx.service;


import com.blogmx.mapper.MessageMapper;
import com.blogmx.pojo.Message;
import com.blogmx.pojo.MoreMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    public void sendMessage(String name, String message, String location, Long fatherId, Long titleId){
        Message m = new Message();
        m.setFatherId(fatherId);
        m.setMessage(message);
        m.setTitleId(titleId);
        m.setWriterLocation(location);
        m.setWriterName(name);
        m.setWriterDate(new Date());
        messageMapper.insert(m);
    }

    public Map<String, Object> getAllMessage(){
        List<Message> messages = messageMapper.selectAll();
        List<MoreMessage> more = new LinkedList<>();
        for(Message m : messages){
            MoreMessage mm = new MoreMessage();
            mm.setM(m);
            mm.setDate(new SimpleDateFormat("yyyy-MM-dd").format(m.getWriterDate()));
            more.add(mm);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("messages", more);
        return map;
    }
}
