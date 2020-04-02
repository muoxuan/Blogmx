package com.blogmx.controller;


import com.blogmx.service.UserService;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Enumeration;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String getLogin(Model model, HttpServletResponse httpServletResponse){
        return "login";
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@RequestParam("name") String name,
                                         @RequestParam("email") String email,
                                         @RequestParam("password") String password){
        Boolean register = userService.register(name, email, password);
        if(register) {
            return ResponseEntity.ok(true);
        }
        else{
            return ResponseEntity.ok(false);
        }


    }

    @PostMapping("/log")
    public ResponseEntity<Boolean> login(@RequestParam("name") String name,
                                         @RequestParam("password") String password,
                                         HttpServletResponse httpServletResponse){
        String token = userService.login(name, password);
        if(token != null) {
            Cookie cookie = new Cookie("MX_SID", token);
            httpServletResponse.addCookie(cookie);
            return ResponseEntity.ok(true);
        }
        else{
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/check/{name}")
    public ResponseEntity<Boolean> checkName(@PathVariable("name") String name){
        return ResponseEntity.ok(userService.chickUserName(name));
    }

    @RequestMapping("/qqLogin")
    public void qqLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        try {
            response.sendRedirect(
                    new Oauth().getAuthorizeURL(request));//将页面重定向到qq第三方的登录页面
        } catch (QQConnectException e) {
            e.printStackTrace();
        }
    }


    //获取登录者的基础信息
    @RequestMapping("/login/qqcallback")
    public void QQAfterlogin(String code , HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("----"+code);
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();

        Enumeration<String> parameterNames = request.getParameterNames();
        while(parameterNames.hasMoreElements()){
            String parameterName = parameterNames.nextElement();//code
            System.out.println(parameterName+":"+request.getParameter(parameterName));//state
        }
        try {
            // 获取AccessToken(AccessToken用于获取OppendID)
            AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);
            System.out.println("accessTokenObj:"+accessTokenObj);
            // 用于接收AccessToken
            String accessToken   = null,
                    openID        = null;
            long tokenExpireIn = 0L; // AccessToken有效时长
            if (!accessTokenObj.getAccessToken().equals("")) {
                accessToken = accessTokenObj.getAccessToken();  // 获取AccessToken
                tokenExpireIn = accessTokenObj.getExpireIn();

                // 利用获取到的accessToken 去获取当前用的openid -------- start
                OpenID openIDObj =  new OpenID(accessToken);
                // 通过对象获取[OpendId]（OpendID用于获取QQ登录用户的信息）
                openID = openIDObj.getUserOpenID();

                out.println("欢迎你，代号为 " + openID + " 的用户!");
                // 利用获取到的accessToken 去获取当前用户的openid --------- end
                UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
                UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
                String token = userService.qqLogin(userInfoBean.getNickname(), openID);
                if(token != null) {
                    Cookie cookie = new Cookie("MX_SID", token);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    response.sendRedirect("/article");
                }
            }
        } catch (QQConnectException e) {
        }

    }

}
