package com.sakura.service.workflow.service;

import com.sakura.service.workflow.IOperator;
import com.sakura.service.workflow.model.ProcessNode;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: bi
 * @date: 2021/12/23 14:01
 */
@Component
public class UserTaskEndIOperator implements IOperator {


    @Override
    public String getKey() {
        return ELEMENT_EVENT_END;
    }

    @Override
    public ProcessNode doTask(Map<String, Object> param) {
        System.out.println("workFlowId：" + param.get("workFlowId"));
        ProcessNode processNode = new ProcessNode();
        processNode.setWorkFlowKey(ELEMENT_EVENT_END);
        System.out.println("流程结束");
        return processNode;
    }
}
