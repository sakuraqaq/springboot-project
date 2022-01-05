package com.sakura.service.workflow;

import com.sakura.entity.WorkFlow;
import com.sakura.entity.WorkFlowTask;
import com.sakura.entity.WorkFlowUser;
import com.sakura.farme.wapper.QueryWrapper;
import com.sakura.mapper.WorkFlowMapper;
import com.sakura.mapper.WorkFlowTaskMapper;
import com.sakura.mapper.WorkFlowUserMapper;
import com.sakura.service.workflow.constants.XMLConstants;
import com.sakura.service.workflow.converter.util.BpmnXMLUtil;
import com.sakura.service.workflow.model.ProcessNode;
import com.sakura.service.workflow.model.WorkFlowNode;
import com.sakura.uid.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: bi
 * @date: 2021/12/22 16:26
 * 流程引擎
 */
@Slf4j
public class ProcessEngine implements XMLConstants {

    private final WorkFlowTaskMapper workFlowTaskMapper;
    private final WorkFlowMapper workFlowMapper;
    private final WorkFlowUserMapper workFlowUserMapper;
    private final IdGenerator idGenerator;
    private final Map<String, IOperator> keyMap = new ConcurrentHashMap<>(); //注入所有的操作接口


    public ProcessEngine(Map<String, IOperator> iOperatorMap, WorkFlowTaskMapper workFlowTaskMapper, WorkFlowMapper workFlowMapper, WorkFlowUserMapper workFlowUserMapper, IdGenerator idGenerator) {
        this.workFlowTaskMapper = workFlowTaskMapper;
        this.workFlowMapper = workFlowMapper;
        this.workFlowUserMapper = workFlowUserMapper;
        this.idGenerator = idGenerator;
        setIOperator(iOperatorMap);

    }

    private void setIOperator(Map<String, IOperator> iOperatorMap) {
        for (Map.Entry<String, IOperator> entry : iOperatorMap.entrySet()) {
            String key = entry.getValue().getKey();
            this.keyMap.put(key, entry.getValue());
        }
    }

    public WorkFlow init() {

        /*
        解析所有的xml文件
        将所有流程存入到数据库中
        流程id绑定流程节点，流程节点1对1流程节点操作类
        */
        List<WorkFlowNode> workFlowNodes = BpmnXMLUtil.parse("D:\\work\\springboot-project\\sakura-web\\sakura-web-exe\\src\\main\\resources\\workflow.xml");
        //初始化流程
        WorkFlow workFlow = new WorkFlow();
        workFlow.setWorkFlowId(idGenerator.getUID());
        workFlow.setWorkFlowName(BpmnXMLUtil.workFlowName);
        workFlow.setStatus(0);
        workFlow.setCreateDate(Calendar.getInstance().getTime());
        Assert.notNull(workFlowNodes, "workFlowNodes was null");
        for (WorkFlowNode workFlowNode : workFlowNodes) {
            //节点
            WorkFlowTask workFlowTask = new WorkFlowTask();
            workFlowTask.setWorkFlowTaskId(idGenerator.getUID());
            workFlowTask.setWorkFlowId(workFlow.getWorkFlowId());
            workFlowTask.setWorkFlowKey(workFlowNode.getNodeKey());
            workFlowTask.setNodeType(workFlowNode.getNodeType());
            workFlowTask.setWorkFlowTaskName(workFlowNode.getName());
            workFlowTask.setCreateDate(Calendar.getInstance().getTime());
            workFlowTask.setState(0);
            workFlowTask.setSourceId(workFlowNode.getSourceId());
            workFlowTask.setTargetId(workFlowNode.getTargetId());
            workFlowTask.setCondition(workFlowNode.getCondition());
            workFlowTask.setId(workFlowNode.getId());
            if (workFlowNode.getNodeKey().equals(ELEMENT_EVENT_START)) {//如果是开始节点
                workFlow.setNodeId(workFlowTask.getWorkFlowTaskId());
            }
            workFlowTaskMapper.insert(workFlowTask);
        }
        workFlowMapper.insert(workFlow);
        return workFlow;
    }


    public void start(Map<String, Object> param) {

        /*
        当前任务节点
        获取工作流id
        通过节点操作类key 寻找操作类
        执行操作类的方法
        */

        //查询当前任务执行到的节点
        WorkFlow workFlow = workFlowMapper.selectOne(new QueryWrapper<WorkFlow>().eq(WorkFlow::getWorkFlowId, param.get("workFlowId")));
        WorkFlowTask workFlowTask = workFlowTaskMapper.selectOne(new QueryWrapper<WorkFlowTask>().eq(WorkFlowTask::getWorkFlowTaskId, workFlow.getNodeId()));
//        if (!workFlowTask.getWorkFlowKey().equals(ELEMENT_EVENT_START)) { //如果不是开始节点进入
//            //查看任务类别 区分 人工任务 还是 脚本 ,服务, 手动任务等....
//            if (workFlowTask.getNodeType().equals(ELEMENT_TASK_USER)) { //人工任务
//                //设置当前用户执行的操作
//                WorkFlowUser workFlowUser = workFlowUserMapper.selectOne(new QueryWrapper<WorkFlowUser>().eq(WorkFlowUser::getWorkFlowTaskId, workFlow.getNodeId()).eq(WorkFlowUser::getUserId, param.get("userId")));
//                if (workFlowUser != null) {
//                    workFlowUser.setState((Integer) param.get("state"));
//                    workFlowUserMapper.updateById(workFlowUser);
//                }
//            }
//        }
        IOperator iOperator = keyMap.get(workFlowTask.getWorkFlowKey());
        finishFlow(iOperator.doTask(param), param);

    }
    /*
         做一个不同任务类型的  审批判断
     */

    /**
     * 判断工作流是否结束 否则继续
     */
    public void finishFlow(ProcessNode processNode, Map<String, Object> param) {
        /*
            根据启动后的流程执行结果 继续选择下一个执行节点
         */
        WorkFlow workFlowId = workFlowMapper.selectOne(new QueryWrapper<WorkFlow>().eq(WorkFlow::getWorkFlowId, param.get("workFlowId")));

        if (XMLConstants.ELEMENT_EVENT_END.equals(processNode.getWorkFlowKey())) {
            workFlowId.setNodeId(processNode.getWorkFlowTaskId());
            workFlowId.setStatus(4);//结束标识
            workFlowMapper.updateById(workFlowId);
            log.info("流程结束");
        } else if (XMLConstants.ELEMENT_EVENT_START.equals(processNode.getWorkFlowKey())) {
            workFlowId.setNodeId(processNode.getWorkFlowTaskId());
            workFlowId.setStatus(0);//结束标识
            workFlowMapper.updateById(workFlowId);
            log.info("当前流程返回到了开始节点");
        } else
            execution(processNode, param);
    }


    public void execution(ProcessNode processNode, Map<String, Object> param) {
        if (processNode.getWorkFlowTaskId() == null)
            return;
        /*
            根据不同类型任务,去执行不同业务逻辑查询 当所有条件满足后在执行下一个节点
            继续执行下一个节点流程
         */
        //保存当前节点id
        WorkFlow workFlowId = workFlowMapper.selectOne(new QueryWrapper<WorkFlow>().eq(WorkFlow::getWorkFlowId, param.get("workFlowId")));
        workFlowId.setNodeId(processNode.getWorkFlowTaskId());

        //更新工作流下一个执行的节点
        //查询下一个节点任务
        WorkFlowTask workFlowTask = workFlowTaskMapper.selectOne(new QueryWrapper<WorkFlowTask>().eq(WorkFlowTask::getWorkFlowTaskId, processNode.getWorkFlowTaskId()));
        IOperator iOperator = keyMap.get(workFlowTask.getWorkFlowKey());
        ProcessNode newNode = iOperator.doTask(param); //返回下一个节点任务
        workFlowId.setNextNodeId(newNode.getWorkFlowTaskId());
        workFlowId.setModifyDate(Calendar.getInstance().getTime());
        workFlowMapper.updateById(workFlowId);
        finishFlow(newNode, param);

    }

}
