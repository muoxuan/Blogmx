package com.blogmx.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.blogmx.annotation.PassToken;
import com.blogmx.annotation.UserLoginToken;
import com.blogmx.pojo.User;
import com.blogmx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        Cookie[] cookies = httpServletRequest.getCookies();
        String token = null; //从 Cookie中取出 token
        if(cookies == null){
            httpServletResponse.sendRedirect("/nologin");
            //throw new RuntimeException("无token，请重新登录");
            return false;
        }
        for(Cookie c : cookies){
            if(c.getName().equals("MX_SID")){
                token = c.getValue();
            }
        }
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            System.out.println(1);
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    System.out.println("noToken");
                    httpServletResponse.sendRedirect("/nologin");

                    return false;
                }
                // 获取 token 中的 user id
                Long id;
                try {
                    id = Long.parseLong(JWT.decode(token).getAudience().get(0));
                } catch (JWTDecodeException j) {
                    httpServletResponse.sendRedirect("/nologin");
                    return false;
                }
                User user = userService.findUserById(id);
                if (user == null) {

                    httpServletResponse.sendRedirect("/nologin");
                    return false;
                }
                // 验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    System.out.println("worng cookie");
                    System.out.println(user.getPassword());
                    httpServletResponse.sendRedirect("/nologin");
                    return false;
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }
}
