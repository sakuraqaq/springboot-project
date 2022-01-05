package com.sakura.service.workflow.service;

import com.sakura.entity.WorkFlow;
import com.sakura.entity.WorkFlowTask;
import com.sakura.farme.wapper.QueryWrapper;
import com.sakura.mapper.WorkFlowMapper;
import com.sakura.mapper.WorkFlowTaskMapper;
import com.sakura.service.workflow.IOperator;
import com.sakura.service.workflow.model.ProcessNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * @author: bi
 * @date: 2021/12/23 13:58
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserTaskIOperator implements IOperator {

    private final WorkFlowTaskMapper workFlowTaskMapper;
    private final WorkFlowMapper workFlowMapper;

    @Override
    public String getKey() {
        return ELEMENT_TASK_USER;
    }

    @Override
    public ProcessNode doTask(Map<String, Object> param) {
        System.out.println("用户任务");
        //业务逻辑判断下一个节点
        String judge = "true";

        //当前节点任务
        WorkFlowTask workFlowTask = workFlowTaskMapper.selectOne(new QueryWrapper<WorkFlowTask>()
                .eq(WorkFlowTask::getWorkFlowId, param.get("workFlowId"))
                .eq(WorkFlowTask::getWorkFlowKey, getKey()));

        //查询下一个节点信息 如果是分支任务那么会有很多节点
        List<WorkFlowTask> nextNode = workFlowTaskMapper.selectList(new QueryWrapper<WorkFlowTask>().eq(WorkFlowTask::getWorkFlowId, param.get("workFlowId"))
                .eq(WorkFlowTask::getSourceId, workFlowTask.getTargetId()));
        WorkFlowTask node = null;
        for (WorkFlowTask flowTask : nextNode) {
            if (flowTask.getCondition().equals(judge)) {
                node = flowTask;
                break;
            }
        }

        ProcessNode processNode = new ProcessNode();
        processNode.setWorkFlowKey(node.getWorkFlowKey());
        processNode.setSourceId(node.getSourceId());
        processNode.setTargetId(node.getTargetId());
        processNode.setWorkFlowTaskId(node.getWorkFlowTaskId());
        processNode.setParam(param);
        return processNode;


    }
}
