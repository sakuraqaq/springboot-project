package com.sakura.service.workflow.model;

import lombok.Data;


/**
 * @author: bi
 * @date: 2021/12/22 16:31
 */
@Data
public class ProcessNode {

    private Long nodeId;

    private Long workFlowId;

    private Long nextId;

    private Integer status;

    private String nodeKey;

}
