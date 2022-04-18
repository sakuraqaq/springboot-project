package com.sakura.service.workflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sakura.entity.WorkFlowTask;
import com.sakura.mapper.WorkFlowTaskMapper;
import com.sakura.service.workflow.IOperator;
import com.sakura.service.workflow.model.ProcessNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: bi
 * @date: 2022/1/5 14:08
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserTaskStartIOperator implements IOperator {

    private final WorkFlowTaskMapper workFlowTaskMapper;

    @Override
    public String getKey() {
        return ELEMENT_EVENT_START;
    }

    @Override
    public ProcessNode doTask(Map<String, Object> param) {

        System.out.println("开始节点");
        //当前节点任务
        WorkFlowTask workFlowTask = workFlowTaskMapper.selectOne(new LambdaQueryWrapper<WorkFlowTask>()
                .eq(WorkFlowTask::getWorkFlowId, param.get("workFlowId"))
                .eq(WorkFlowTask::getWorkFlowKey, getKey()));

        //查询下一个节点信息
        WorkFlowTask nextNode = workFlowTaskMapper.selectOne(new LambdaQueryWrapper<WorkFlowTask>().eq(WorkFlowTask::getWorkFlowId, param.get("workFlowId"))
                .eq(WorkFlowTask::getSourceId, workFlowTask.getId()));

        ProcessNode processNode = new ProcessNode();
        processNode.setWorkFlowTaskId(nextNode.getWorkFlowTaskId());
        processNode.setId(nextNode.getId());
        processNode.setNodeType(nextNode.getNodeType());
        processNode.setSourceId(nextNode.getSourceId());
        processNode.setTargetId(nextNode.getTargetId());
        processNode.setWorkFlowKey(nextNode.getWorkFlowKey());
        processNode.setParam(param);
        processNode.setState(2);
        return processNode;
    }
}
