package com.sakura.application;

import com.sakura.application.config.EnableWorkFlow;
import com.sakura.farme.annotation.EnableCreateTable;
import com.sakura.farme.annotation.EnableRedis;
import com.sakura.web.annotation.EnableRedisSession;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author : bi
 * @since : 2021年06月24日
 */
@EnableRedis
@EnableRedisSession
//@EnableCreateTable
@MapperScan("com.sakura.mapper")
@SpringBootApplication(scanBasePackages = {"com.sakura"})
@EnableWorkFlow(scanBasePackage = "com.sakura.service.workflow.service")
public class SakuraWebApplication {

    public static void main(String[] args) {
       SpringApplication.run(SakuraWebApplication.class, args);
    }
}