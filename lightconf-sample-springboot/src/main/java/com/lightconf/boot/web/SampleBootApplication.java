package com.lightconf.boot.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wuhaifei 2019-11-08
 */
@SpringBootApplication(scanBasePackages = "com.lightconf.boot.web")
public class SampleBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(SampleBootApplication.class, args);
    }
}
