package com.sakura.application;

import com.sakura.farme.annotation.EnableSakuraMybatis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author : bi
 * @since : 2021年06月24日
 */
@EnableSakuraMybatis
@SpringBootApplication
@ComponentScan({"com.sakura"})
public class SakuraWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SakuraWebApplication.class, args);
    }
}