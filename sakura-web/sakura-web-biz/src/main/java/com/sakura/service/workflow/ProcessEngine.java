package com.sakura.service.workflow;

import com.sakura.entity.WorkFlowTask;
import com.sakura.farme.wapper.QueryWrapper;
import com.sakura.mapper.WorkFlowMapper;
import com.sakura.mapper.WorkFlowTaskMapper;
import com.sakura.service.workflow.constants.XMLConstants;
import com.sakura.service.workflow.model.ProcessNode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: bi
 * @date: 2021/12/22 16:26
 * 流程引擎
 */

public class ProcessEngine implements XMLConstants {

    private WorkFlowTaskMapper workFlowTaskMapper;
    private WorkFlowMapper workFlowMapper;
    private Map<String, IOperator> iOperatorMap; //注入所有的操作接口


    //当前节点Id
    public Long nodeId;

    public ProcessEngine(WorkFlowTaskMapper workFlowTaskMapper, Long nodeId, Map<String, IOperator> iOperatorMap) {
        this.workFlowTaskMapper = workFlowTaskMapper;
        this.nodeId = nodeId;
        this.iOperatorMap = new ConcurrentHashMap<>();
        setIOperator(iOperatorMap);
    }

    public ProcessEngine(WorkFlowTaskMapper workFlowTaskMapper, WorkFlowMapper workFlowMapper) {
        this.workFlowTaskMapper = workFlowTaskMapper;
        this.workFlowMapper = workFlowMapper;
    }

    private void setIOperator(Map<String, IOperator> iOperatorMap) {
        for (Map.Entry<String, IOperator> entry : iOperatorMap.entrySet()) {
            String key = entry.getValue().getKey();
            this.iOperatorMap.put(key, entry.getValue());
        }
    }

    public void init() {

        /*
        解析所有的xml文件
        将所有流程存入到数据库中
        流程id绑定流程节点，流程节点1对1流程节点操作类
        */

        workFlowMapper.insert(null);
        workFlowTaskMapper.insert(null);
    }


    public void start(Object[] objects) {

        /*
        当前任务节点
        获取工作流id
        通过节点操作类key 寻找操作类
        执行操作类的方法
        */

        WorkFlowTask workFlowTask = workFlowTaskMapper.selectOne(new QueryWrapper<WorkFlowTask>().eq(WorkFlowTask::getWorkFlowTaskId, nodeId));
        workFlowTask = new WorkFlowTask();
        workFlowTask.setWorkFlowKey(ELEMENT_EVENT_START);
        //Long workFlowId = workFlowTask.getWorkFlowId();
        objects[0] = 123;
        IOperator iOperator = iOperatorMap.get(workFlowTask.getWorkFlowKey());
        finishFlow(iOperator.doTask(objects), objects);

    }

    public void finishFlow(ProcessNode processNode, Object[] objects) {
        /*
            根据启动后的流程执行结果 继续选择下一个执行节点
         */
        if (XMLConstants.ELEMENT_EVENT_END.equals(processNode.getNodeKey())) {
            System.out.println("流程结束");
        } else
            execution(processNode, objects);
    }


    public void execution(ProcessNode processNode, Object[] objects) {
        /*
            继续执行下一个节点流程
         */
        WorkFlowTask workFlowTask = workFlowTaskMapper.selectOne(new QueryWrapper<WorkFlowTask>().eq(WorkFlowTask::getWorkFlowTaskId, processNode.getNextId()));
        // Long workFlowId = workFlowTask.getWorkFlowId();
        objects[0] = 123;
        workFlowTask = new WorkFlowTask();
        workFlowTask.setWorkFlowKey(processNode.getNodeKey());
        IOperator iOperator = iOperatorMap.get(workFlowTask.getWorkFlowKey());
        finishFlow(iOperator.doTask(objects), objects);
    }

}
