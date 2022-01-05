package com.sakura.service.workflow.model;

import lombok.Data;

import java.util.Map;


/**
 * @author: bi
 * @date: 2021/12/22 16:31
 */
@Data
public class ProcessNode {

    private String sourceId;//节点入口
    private String targetId;//节点出口
    private String id;
    private String condition;//节点条件
    private String nodeType;
    private String workFlowKey;
    private Integer state;
    private Long workFlowTaskId;
    private Map<String, Object> param;

}
