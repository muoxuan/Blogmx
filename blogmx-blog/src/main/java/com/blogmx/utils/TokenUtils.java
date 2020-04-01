package com.blogmx.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.blogmx.pojo.User;

public class TokenUtils {

    public static String getToken(User user) {
        String token="";
        token= JWT.create().withAudience(user.getName())
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }

}

