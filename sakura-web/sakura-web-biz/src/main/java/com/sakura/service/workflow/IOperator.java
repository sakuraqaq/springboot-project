package com.sakura.service.workflow;

import com.sakura.service.workflow.constants.XMLConstants;
import com.sakura.service.workflow.model.ProcessNode;

import java.util.Map;

/**
 * @author: bi
 * @date: 2021/12/23 10:17
 */
public interface IOperator extends XMLConstants {


    String getKey();

    /**
     * 流程实际业务逻辑，objects用于参数传递
     */
    ProcessNode doTask(Map<String, Object> param);

}
