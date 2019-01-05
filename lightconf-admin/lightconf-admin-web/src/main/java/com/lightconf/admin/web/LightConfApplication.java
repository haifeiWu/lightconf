package com.lightconf.admin.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:light-conf.properties")
public class LightConfApplication {
    public static void main(String[] args) {
        SpringApplication.run(LightConfApplication.class, args);
    }
}
