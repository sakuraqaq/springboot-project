package com.sakura.service.workflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sakura.entity.WorkFlowTask;
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
 * @date: 2021/12/23 16:48
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserTaskBranchI2Operator implements IOperator {

    private final WorkFlowTaskMapper workFlowTaskMapper;

    @Override
    public String getKey() {
        return "branchTask2";
    }

    @Override
    public ProcessNode doTask(Map<String, Object> param) {
        System.out.println("分支2");

        WorkFlowTask workFlowTask = workFlowTaskMapper.selectOne(new LambdaQueryWrapper<WorkFlowTask>()
                .eq(WorkFlowTask::getWorkFlowId, param.get("workFlowId"))
                .eq(WorkFlowTask::getWorkFlowKey, getKey()));

        //查询下一个节点信息
        WorkFlowTask node = workFlowTaskMapper.selectOne(new LambdaQueryWrapper<WorkFlowTask>()
                .eq(WorkFlowTask::getWorkFlowId, param.get("workFlowId"))
                .eq(WorkFlowTask::getId, workFlowTask.getTargetId()));

        ProcessNode processNode = new ProcessNode();
        processNode.setWorkFlowTaskId(node.getWorkFlowTaskId());
        processNode.setWorkFlowKey(node.getWorkFlowKey());
        processNode.setId(node.getId());
        processNode.setSourceId(node.getSourceId());
        processNode.setTargetId(node.getTargetId());
        processNode.setParam(param);
        return processNode;
    }
}
