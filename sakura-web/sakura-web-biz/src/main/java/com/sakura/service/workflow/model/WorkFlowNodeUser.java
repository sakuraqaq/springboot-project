package com.sakura.service.workflow.model;

import lombok.Data;

/**
 * @author: bi
 * @date: 2021/12/27 15:20
 */
@Data
public class WorkFlowNodeUser {

    private Long workFlowId;

    private String nodeId;

    private Long userId;

    private String userName;

    private Integer state;
}
