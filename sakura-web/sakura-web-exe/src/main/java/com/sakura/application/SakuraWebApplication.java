package com.sakura.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author : bi
 * @since : 2021年06月24日
 */
@SpringBootApplication
@ComponentScan({"com.sakura"})
public class SakuraWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SakuraWebApplication.class, args);
    }
}