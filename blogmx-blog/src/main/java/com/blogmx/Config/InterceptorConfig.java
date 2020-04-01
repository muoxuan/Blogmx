package com.blogmx.Config;

import com.blogmx.filter.AdminAuthenticationInterceptor;
import com.blogmx.filter.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/message");
        registry.addInterceptor(adminAuthenticationInterceptor())
                .addPathPatterns("/change");
    }
    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    @Bean
    public AdminAuthenticationInterceptor adminAuthenticationInterceptor() {
        return new AdminAuthenticationInterceptor();
    }
}
