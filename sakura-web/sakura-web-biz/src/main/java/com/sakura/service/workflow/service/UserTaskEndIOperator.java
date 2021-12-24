package com.sakura.service.workflow.service;

import com.sakura.service.workflow.IOperator;
import com.sakura.service.workflow.model.ProcessNode;
import org.springframework.stereotype.Component;

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
    public ProcessNode doTask(Object[] objects) {
        System.out.println("workFlowId：" + objects[0]);
        ProcessNode processNode = new ProcessNode();
        processNode.setNodeKey(ELEMENT_EVENT_END);
        System.out.println("流程结束");
        return processNode;
    }
}
