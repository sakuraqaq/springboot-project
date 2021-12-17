package com.sakura.service.workflow.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: bi
 * @date: 2021/12/16 10:43
 */
@Data
public class TaskModel  extends BaseModel implements Serializable {

    /**
     * 参与方式(ANY->任何一个参与者处理完即可执行下一步,ALL->所有参与者都完成，才可执行下一步)
     */
    private String performType;

    /**
     * 期望完成时间
     */
    private String expireTime;

    /**
     * 是否自动执行
     */
    private boolean autoExecute;
}
