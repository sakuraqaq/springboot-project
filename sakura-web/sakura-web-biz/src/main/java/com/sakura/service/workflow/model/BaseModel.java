package com.sakura.service.workflow.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: bi
 * @date: 2021/12/16 10:49
 * 基本节点模型
 */
@Data
public class BaseModel  implements Serializable {

    private Long id;

    private Long nextId;

    private String name;

    private String ext;

    private String nodeType;
}
