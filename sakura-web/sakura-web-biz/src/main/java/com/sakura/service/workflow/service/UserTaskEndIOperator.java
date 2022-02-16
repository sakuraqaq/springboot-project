package com.sakura.service.workflow.service;

import com.sakura.entity.WorkFlowTask;
import com.sakura.farme.wapper.QueryWrapper;
import com.sakura.mapper.WorkFlowTaskMapper;
import com.sakura.service.workflow.IOperator;
import com.sakura.service.workflow.model.ProcessNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: bi
 * @date: 2021/12/23 14:01
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserTaskEndIOperator implements IOperator {

    private final WorkFlowTaskMapper workFlowTaskMapper;


    @Override
    public String getKey() {
        return ELEMENT_EVENT_END;
    }

    @Override
    public ProcessNode doTask(Map<String, Object> param) {
        System.out.println("workFlowId：" + param.get("workFlowId"));
        WorkFlowTask workFlowTask = workFlowTaskMapper.selectOne(new QueryWrapper<WorkFlowTask>()
                .eq(WorkFlowTask::getWorkFlowId, param.get("workFlowId"))
                .eq(WorkFlowTask::getWorkFlowKey, getKey()));

        ProcessNode processNode = new ProcessNode();
        processNode.setWorkFlowKey(ELEMENT_EVENT_END);
        processNode.setWorkFlowTaskId(workFlowTask.getWorkFlowTaskId());
        processNode.setState(4);
        processNode.setParam(param);
        processNode.setNodeType(workFlowTask.getNodeType());
        log.info("流程结束");
        return processNode;
    }
}
