package com.sakura.application.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author: bi
 * @date: 2021/12/23 14:14
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({WorkFlowRegistrar.class})
public @interface EnableWorkFlow {

    String scanBasePackage() default "";
}
