package com.sakura.service.workflow.model;

import lombok.Data;

/**
 * @author: bi
 * @date: 2021/12/22 16:32
 */
@Data
public class WorkFlowNode {



    private Long workFlowId;

    private String workFlowName;

    private String id;

    private String name;

    /**
     * 上一个id
     */
    private String sourceId;

    /**
     * 下一个id
     */
    private String targetId;

    private Integer state;

    private String nodeKey;

    private String nodeType;

    private String condition;


}
