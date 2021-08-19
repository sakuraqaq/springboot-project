package com.sakura.application;

import com.sakura.farme.annotation.EnableRedis;
import com.sakura.farme.annotation.EnableSakuraMybatis;
import com.sakura.web.annotation.EnableRedisSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : bi
 * @since : 2021年06月24日
 */
@EnableRedis
@EnableRedisSession
@EnableSakuraMybatis
@SpringBootApplication(scanBasePackages = {"com.sakura"})
public class SakuraWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SakuraWebApplication.class, args);
    }
}