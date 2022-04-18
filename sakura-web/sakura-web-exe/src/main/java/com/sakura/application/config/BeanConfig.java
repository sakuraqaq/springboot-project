package com.sakura.application.config;

import com.sakura.mapper.WorkFlowMapper;
import com.sakura.mapper.WorkFlowTaskMapper;
import com.sakura.mapper.WorkFlowUserMapper;
import com.sakura.service.workflow.IOperator;
import com.sakura.service.workflow.ProcessEngine;
import com.sakura.uid.IdGenerator;
import com.sakura.web.session.AfterLoginRequired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class BeanConfig {

    @Bean
    public AfterLoginRequired afterLoginRequired() {
        return (request, isLogin) -> true;
    }

//    @Bean
//    public ProcessEngine processEngine(Map<String, IOperator> iOperatorMap, WorkFlowTaskMapper workFlowTaskMapper, WorkFlowMapper workFlowMapper, WorkFlowUserMapper workFlowUserMapper, IdGenerator idGenerator) {
//        return new ProcessEngine(iOperatorMap, workFlowTaskMapper, workFlowMapper, workFlowUserMapper, idGenerator);
//    }
}
