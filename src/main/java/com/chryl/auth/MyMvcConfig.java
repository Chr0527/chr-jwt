package com.chryl.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created By Chr on 2019/7/19.
 */
@Configuration
public class MyMvcConfig extends WebMvcConfigurerAdapter {
    // 注册拦截器,注册才生效
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加拦截的请求，并排除几个不拦截的请求
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/**")//拦截所有


        //
        ;
    }
}
