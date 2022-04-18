package com.sakura.service.workflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sakura.entity.WorkFlowTask;
import com.sakura.entity.WorkFlowUser;
import com.sakura.mapper.WorkFlowMapper;
import com.sakura.mapper.WorkFlowTaskMapper;
import com.sakura.mapper.WorkFlowUserMapper;
import com.sakura.service.workflow.IOperator;
import com.sakura.service.workflow.model.ProcessNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private final WorkFlowUserMapper workFlowUserMapper;


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
        WorkFlowTask workFlowTask = workFlowTaskMapper.selectOne(new LambdaQueryWrapper<WorkFlowTask>()
                .eq(WorkFlowTask::getWorkFlowId, param.get("workFlowId"))
                .eq(WorkFlowTask::getWorkFlowKey, getKey()));
        List<WorkFlowUser> workFlowUsers = workFlowUserMapper.selectList(new LambdaQueryWrapper<WorkFlowUser>().eq(WorkFlowUser::getWorkFlowTaskId, workFlowTask.getWorkFlowTaskId()));
        if (workFlowUsers != null) {
            //查询是否所有用户都完成了任务
            for (WorkFlowUser workFlowUser : workFlowUsers) {
                if (workFlowUser.getState().equals(3)) { //3表示有人审批不通过
                    judge = "false";
                    break;
                } else if (workFlowUser.getState().equals(0)) { //0表示有人未审批
                    judge = "wait";
                    break;
                }
            }
        }
        WorkFlowTask node = null;
        if (!judge.equals("wait")) {
            //查询下一个节点信息 如果是分支任务那么会有很多节点
            List<WorkFlowTask> nextNode = workFlowTaskMapper.selectList(new LambdaQueryWrapper<WorkFlowTask>().eq(WorkFlowTask::getWorkFlowId, param.get("workFlowId"))
                    .eq(WorkFlowTask::getSourceId, workFlowTask.getTargetId()));

            for (WorkFlowTask flowTask : nextNode) {
                if (flowTask.getCondition().equals(judge)) {
                    node = flowTask;
                    break;
                }
            }
        }

        ProcessNode processNode = new ProcessNode();
        if(node != null){
            processNode.setWorkFlowKey(node.getWorkFlowKey());
            processNode.setId(node.getId());
            processNode.setSourceId(node.getSourceId());
            processNode.setTargetId(node.getTargetId());
            processNode.setWorkFlowTaskId(node.getWorkFlowTaskId());
            processNode.setNextWorkFlowTaskId(node.getWorkFlowTaskId());
            processNode.setState(node.getState());
            processNode.setNodeType(node.getNodeType());
            processNode.setParam(param);
        }
        return processNode;


    }
}
