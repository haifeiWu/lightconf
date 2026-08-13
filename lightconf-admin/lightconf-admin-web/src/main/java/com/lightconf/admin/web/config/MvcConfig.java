package com.lightconf.admin.web.config;

import com.lightconf.admin.web.controller.interceptor.CookieInterceptor;
import com.lightconf.admin.web.controller.interceptor.PermissionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wuhf
 * @Date 2019/1/5 14:39
 **/
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    CookieInterceptor cookieInterceptor;

    @Autowired
    PermissionInterceptor permissionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cookieInterceptor).addPathPatterns("/**");
        registry.addInterceptor(permissionInterceptor).addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 替代原 CrossDomainRequestFilter：允许任意来源 + 携带凭据（仅用于管理端调试，生产建议收紧 allowedOriginPatterns）
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("POST", "GET", "OPTIONS", "DELETE")
                .allowedHeaders("Origin", "No-Cache", "X-Requested-With", "If-Modified-Since",
                        "Pragma", "Last-Modified", "Cache-Control", "Expires", "Content-Type",
                        "X-E4M-With", "userId", "token")
                .allowCredentials(true)
                .maxAge(0);
    }
}
