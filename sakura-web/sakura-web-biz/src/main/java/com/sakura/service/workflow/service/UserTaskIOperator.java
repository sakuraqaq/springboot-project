package com.sakura.service.workflow.service;

import com.sakura.service.workflow.IOperator;
import com.sakura.service.workflow.model.ProcessNode;
import org.springframework.stereotype.Component;

/**
 * @author: bi
 * @date: 2021/12/23 13:58
 */
@Component
public class UserTaskIOperator implements IOperator {


    @Override
    public String getKey() {
        return ELEMENT_EVENT_START;
    }

    @Override
    public ProcessNode doTask(Object[] objects) {

        System.out.println("开始节点");
        System.out.println("workFlowId：" + objects[0]);
        ProcessNode processNode = new ProcessNode();
        processNode.setNodeKey("branchTask");
        return processNode;
    }
}
