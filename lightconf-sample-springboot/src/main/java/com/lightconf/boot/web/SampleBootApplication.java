package com.lightconf.boot.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @author wuhaifei 2019-11-08
 */
@SpringBootApplication(scanBasePackages = "com.lightconf.boot.web")
@ImportResource("classpath:spring/applicationcontext-light-conf.xml")
public class SampleBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(SampleBootApplication.class, args);
    }
}
