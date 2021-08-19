package com.sakura.application.config;

import com.sakura.web.session.AfterLoginRequired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public AfterLoginRequired afterLoginRequired() {
        return (request, isLogin) -> true;
    }
}
