package com.sakura.service.workflow.process.impl;

import com.sakura.service.workflow.model.BaseModel;
import com.sakura.service.workflow.process.NodeProcessor;

/**
 * @author: bi
 * @date: 2021/12/16 10:42
 */
public class StartNodeProcessorImpl implements NodeProcessor {


    @Override
    public void process(BaseModel baseModel) {
        //控制流程流转过程 业务代码


        //结束时 根据流程id 调用下一个节点
    }
}
