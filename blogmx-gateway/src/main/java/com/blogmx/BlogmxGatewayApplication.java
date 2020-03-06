package com.blogmx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableDiscoveryClient // 表明该项目为一个eureka客户端
@EnableZuulProxy //启动zuul组件
public class BlogmxGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogmxGatewayApplication.class);
    }
}
