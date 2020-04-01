package com.blogmx.Config;

import com.blogmx.pojo.Blog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Date;

@Configuration
public class RedisConfig{
    @Bean
    public RedisTemplate<String,Blog>redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String,Blog>template=new RedisTemplate<>();
        //关联
        template.setConnectionFactory(factory);
        //设置key的序列化器
        template.setKeySerializer(new StringRedisSerializer());
        //设置value的序列化器
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Blog.class));
        return template;
    }

    @Bean
    public RedisTemplate<String, Date>redisTemplateDate(RedisConnectionFactory factory){
        RedisTemplate<String,Date>template=new RedisTemplate<>();
        //关联
        template.setConnectionFactory(factory);
        //设置key的序列化器
        template.setKeySerializer(new StringRedisSerializer());
        //设置value的序列化器
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Date.class));
        return template;
    }


}

