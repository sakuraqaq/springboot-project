package com.sakura.service.workflow.process;

import com.sakura.service.workflow.model.BaseModel;

/**
 * @author: bi
 * @date: 2021/12/16 10:40
 * 节点处理接口
 */
public interface NodeProcessor {

    void process(BaseModel baseModel);
}
