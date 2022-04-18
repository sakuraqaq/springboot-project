package com.sakura.service.workflow;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sakura.entity.WorkFlow;
import com.sakura.entity.WorkFlowTask;
import com.sakura.entity.WorkFlowUser;
import com.sakura.mapper.WorkFlowMapper;
import com.sakura.mapper.WorkFlowTaskMapper;
import com.sakura.mapper.WorkFlowUserMapper;
import com.sakura.service.workflow.constants.XMLConstants;
import com.sakura.service.workflow.converter.util.BpmnXMLUtil;
import com.sakura.service.workflow.converter.util.LimitQueue;
import com.sakura.service.workflow.model.ProcessNode;
import com.sakura.service.workflow.model.WorkFLowParam;
import com.sakura.service.workflow.model.WorkFlowNode;
import com.sakura.uid.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

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

    public final BlockingQueue<ProcessNode> blockingQueue = new LinkedBlockingDeque();

    public final ThreadPoolExecutor exec = new ThreadPoolExecutor(5,
            10,
            5,
            TimeUnit.SECONDS,
            new LimitQueue<>(10),
            (r, executor) -> {
                log.error("当前的任务已经被拒绝");
                if (!executor.isShutdown()) {
                    try {
                        log.info(" exec.getQueue().put(r)");
                        executor.getQueue().put(r);
                    } catch (InterruptedException e) {
                        log.info(e + "\r\n" + e.getMessage());
                    }
                }
            }
    );

    public final Thread thread = new Thread(() -> {
        while (true) {
            try {
                ProcessNode take = blockingQueue.take();
                IOperator iOperator = keyMap.get(take.getWorkFlowKey());
                finishFlow(iOperator.doTask(take.getParam()), take.getParam());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });


    public ProcessEngine(Map<String, IOperator> iOperatorMap, WorkFlowTaskMapper workFlowTaskMapper, WorkFlowMapper workFlowMapper, WorkFlowUserMapper workFlowUserMapper, IdGenerator idGenerator) {
        this.workFlowTaskMapper = workFlowTaskMapper;
        this.workFlowMapper = workFlowMapper;
        this.workFlowUserMapper = workFlowUserMapper;
        this.idGenerator = idGenerator;
        setIOperator(iOperatorMap);
        thread.start();
    }

    private void setIOperator(Map<String, IOperator> iOperatorMap) {
        for (Map.Entry<String, IOperator> entry : iOperatorMap.entrySet()) {
            String key = entry.getValue().getKey();
            this.keyMap.put(key, entry.getValue());
        }
    }

    /**
     * 在生成工作流时调用
     */
    public WorkFlow init(String param) {

        /*
        先通过给定的模板 下载对应的xml文件
        在解析xml文件
        将所有流程存入到数据库中
        流程id绑定流程节点，流程节点1对1流程节点操作类
        */
        String fileName = "workflow.xml";
        List<WorkFlowNode> workFlowNodes = BpmnXMLUtil.parse("D:\\work\\springboot-project\\sakura-web\\sakura-web-exe\\src\\main\\resources\\" + fileName);
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
            } else if (workFlowNode.getNodeType().equals(ELEMENT_TASK_USER)) { //如果是多人节点
                WorkFLowParam workFLowParam = JSONObject.parseObject(param, WorkFLowParam.class);
                if (workFLowParam.getUsers() != null && workFLowParam.getUsers().size() > 0) {
                    for (Long user : workFLowParam.getUsers()) {
                        WorkFlowUser workFlowUser = new WorkFlowUser();
                        workFlowUser.setWorkFlowId(workFlow.getWorkFlowId());
                        workFlowUser.setWorkFlowTaskId(workFlowTask.getWorkFlowTaskId());
                        workFlowUser.setCreateDate(Calendar.getInstance().getTime());
                        workFlowUser.setState(0);
                        workFlowUser.setUserId(user);
                        workFlowUserMapper.insert(workFlowUser); //目前没有insert多个 先这样
                    }

                }
            }
            workFlowTaskMapper.insert(workFlowTask);
        }
        workFlowMapper.insert(workFlow);
        return workFlow;
    }

    /**
     * 在第一次启动工作流时调用
     */
    public void start(Map<String, Object> param) {

        /*
        所有流程 在队列中 轮询执行
        获取工作流id
        通过节点操作类key 寻找操作类
        执行操作类的方法
        */
        //加一个多线程执行当前所有的流程 更具流程状态判断是否需要进行下一步

        //查询当前任务执行到的节点
        WorkFlow workFlow = workFlowMapper.selectOne(new LambdaQueryWrapper<WorkFlow>().eq(WorkFlow::getWorkFlowId, param.get("workFlowId")));
        if (workFlow.getStatus() != 4) {
            WorkFlowTask workFlowTask = workFlowTaskMapper.selectOne(new LambdaQueryWrapper<WorkFlowTask>().eq(WorkFlowTask::getWorkFlowTaskId, workFlow.getNodeId()));
            ProcessNode processNode = new ProcessNode();
            processNode.setWorkFlowTaskId(workFlowTask.getWorkFlowTaskId());
            processNode.setWorkFlowKey(workFlowTask.getWorkFlowKey());
            processNode.setId(workFlowTask.getId());
            processNode.setNodeType(workFlowTask.getNodeType());
            processNode.setSourceId(workFlowTask.getSourceId());
            processNode.setTargetId(workFlowTask.getTargetId());
            processNode.setState(workFlowTask.getState());
            processNode.setParam(param);
            execution(processNode, param);
        }

//        Runnable runnable = () -> {
//            try {
//                ProcessNode take = blockingQueue.take();
//                IOperator iOperator = keyMap.get(take.getWorkFlowKey());
//                finishFlow(iOperator.doTask(take.getParam()), take.getParam());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        };
//        exec.execute(runnable);


    }

    /*
         做一个不同任务类型的  审批判断
     */
    public void execution(ProcessNode processNode, Map<String, Object> param) {

        if(processNode.getState() == 0){
            return;
        }
        if (processNode.getState() != 4) {
            try {
                blockingQueue.put(processNode);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //保存当前节点id
        WorkFlow workFlowId = workFlowMapper.selectOne(new LambdaQueryWrapper<WorkFlow>().eq(WorkFlow::getWorkFlowId, param.get("workFlowId")));
        workFlowId.setNodeId(processNode.getWorkFlowTaskId());
        //更新工作流下一个执行的节点
        WorkFlowTask nextNode = workFlowTaskMapper.selectOne(new LambdaQueryWrapper<WorkFlowTask>().eq(WorkFlowTask::getWorkFlowId, param.get("workFlowId"))
                .eq(WorkFlowTask::getId, processNode.getId()));
        //查询下一个节点任务
        if (processNode.getNodeType().equals(ELEMENT_EVENT_END)) {
            workFlowId.setNextNodeId(0L);
        } else
            workFlowId.setNextNodeId(nextNode.getWorkFlowTaskId());
        workFlowId.setModifyDate(Calendar.getInstance().getTime());
        workFlowId.setStatus(processNode.getState());
        workFlowMapper.updateById(workFlowId);
    }


    /**
     * 判断工作流是否结束 否则继续
     */
    public void finishFlow(ProcessNode processNode, Map<String, Object> param) {
        execution(processNode, param);
    }
}
