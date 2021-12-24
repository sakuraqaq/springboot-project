package com.sakura.service.workflow.model;

import lombok.Data;

import java.util.List;

/**
 * @author: bi
 * @date: 2021/12/22 16:32
 */
@Data
public class WorkFlowNode {


    private Long nodeId;

    private Long workFlowId;

    private List<Long> nextId;

    private Integer status;

    private String nodeKey;


}
