package com.blogmx.controller;

import com.auth0.jwt.JWT;
import com.blogmx.annotation.UserLoginToken;
import com.blogmx.service.MessageService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

@Controller
public class MessageController {
    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST = "127.0.0.1";
    private static final String SEPARATOR = ",";
    @Autowired
    private MessageService messageService;


    @UserLoginToken
    @GetMapping("message")
    public String getMessage(Model model){

        Map<String, Object> allMessage = messageService.getAllMessage();
        model.addAllAttributes(allMessage);
        return "message";
    }


    @GetMapping("nologin")
    public String GetMessage(){
        return "noLogin";
    }

    @GetMapping("getmessage")
    public ResponseEntity<Void> getMessage(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @RequestParam("message") String message,
                                           @RequestParam("fatherId") Long fatherId){

        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (LOCALHOST.equals(ipAddress)) {
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            // "***.***.***.***".length()
            if (ipAddress != null && ipAddress.length() > 15) {
                if (ipAddress.indexOf(SEPARATOR) > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        Cookie[] cookies = request.getCookies();
        String token = null; //从 Cookie中取出 token
        if(cookies == null){
            try {
                response.sendRedirect("/nologin");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok().build();
        }
        for(Cookie c : cookies){
            if(c.getName().equals("MX_SID")){
                token = c.getValue();
            }
        }
        if(token == null){
            try {
                response.sendRedirect("/nologin");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok().build();
        }
        String name = JWT.decode(token).getAudience().get(0);
        messageService.sendMessage(name, message, ipAddress, fatherId, 0L);
        try {
            response.sendRedirect("/message");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }


}
