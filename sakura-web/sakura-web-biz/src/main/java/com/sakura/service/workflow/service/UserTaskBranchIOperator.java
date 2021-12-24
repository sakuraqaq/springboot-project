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

/**
 * @author: bi
 * @date: 2021/12/23 16:48
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserTaskBranchIOperator implements IOperator {

    private final WorkFlowTaskMapper workFlowTaskMapper;

    @Override
    public String getKey() {
        return "branchTask";
    }

    @Override
    public ProcessNode doTask(Object[] objects) {
        ProcessNode processNode = new ProcessNode();
        WorkFlowTask workFlowTask = workFlowTaskMapper.selectOne(new QueryWrapper<WorkFlowTask>().eq(WorkFlowTask::getWorkFlowTaskId, objects[0]));
        log.info("分支任务");
        if (workFlowTask == null)
            processNode.setNodeKey("branchTask2");
        else
            processNode.setNodeKey(ELEMENT_EVENT_END);
        return processNode;
    }
}
