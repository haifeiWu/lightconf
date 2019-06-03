package com.lightconf.admin.web.config;

import com.lightconf.admin.web.controller.interceptor.CookieInterceptor;
import com.lightconf.admin.web.controller.interceptor.PermissionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author wuhf
 * @Date 2019/1/5 14:39
 **/
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    CookieInterceptor cookieInterceptor;

    @Autowired
    PermissionInterceptor permissionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cookieInterceptor).addPathPatterns("/**");
        registry.addInterceptor(permissionInterceptor).addPathPatterns("/**");
    }
}
