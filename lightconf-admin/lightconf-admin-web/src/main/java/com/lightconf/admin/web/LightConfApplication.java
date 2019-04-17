package com.lightconf.admin.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * @author wuhaifei
 */
@SpringBootApplication
@PropertySource("classpath:light-conf.properties")
@MapperScan(basePackages = "com.lightconf.admin.dal.dao")
public class LightConfApplication {
    public static void main(String[] args) {
        SpringApplication.run(LightConfApplication.class, args);
    }
}
