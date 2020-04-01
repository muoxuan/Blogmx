package com.blogmx.service;


import com.blogmx.mapper.UserMapper;
import com.blogmx.pojo.User;
import com.blogmx.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;



    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    public Boolean register(String name, String email, String password){
        int max=100,min=1;
        Long randomNum = System.currentTimeMillis();
        String salt = randomNum.toString();
        password += salt;
        password = stringToMD5(password);
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        user.setSalt(salt);
        int insert = userMapper.insert(user);
        return insert > 0;
    }

    public String login(String name, String password){
        User user = new User();
        user.setName(name);
        List<User> select = userMapper.select(user);
        if(select == null || select.size() == 0){
            return null;
        }
        user = select.get(0);
        String salt = user.getSalt();
        password += salt;
        password = stringToMD5(password);
        if(!password.equals(user.getPassword())){
            return null;
        }

        String token = TokenUtils.getToken(user);
        return token;
    }

    public Boolean chickUserName(String name){
        User user = new User();
        user.setName(name);
        List<User> select = userMapper.select(user);
        return select.size() == 0;
    }


    public User findUserById(String name) {
        User user = new User();
        user.setName(name);
        List<User> select = userMapper.select(user);

        if(select.size() == 0){
            return null;
        }
        return select.get(0);
    }
}
