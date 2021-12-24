package com.sakura.entity;

import com.sakura.farme.annotation.Column;
import com.sakura.farme.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;

/**
 * @author: bi
 * @date: 2021/12/17 14:23
 */
@Data
@Table(value = "work_flow_task", auto = "add", comment = "工作流任务表")
public class WorkFlowTask {

    @Column(columnName = "work_flow_task_id", jdbcType = JdbcType.BIGINT, isNull = false, isPrimaryKey = true, comment = "主键Id")
    private Long workFlowTaskId;

    @Column(columnName = "work_flow_id", jdbcType = JdbcType.BIGINT, isNull = false, comment = "工作流Id")
    private Long workFlowId;

    @Column(columnName = "work_flow_task_name", jdbcType = JdbcType.VARCHAR, isNull = false, length = 16, comment = "工作流名称")
    private String workFlowTaskName;

    @Column(columnName = "work_flow_key", jdbcType = JdbcType.VARCHAR, length = 16, comment = "操作类的key")
    private String workFlowKey;

    @Column(columnName = "work_flow_node_id", jdbcType = JdbcType.BIGINT, comment = "工作流任务节点Id")
    private Long workFlowNodeId;

    @Column(columnName = "work_flow_param", jdbcType = JdbcType.VARCHAR, length = 1024, comment = "工作流参数")
    private String workFlowParam;

    @Column(columnName = "status", jdbcType = JdbcType.BIGINT, isNull = false, columnDefault = "0", comment = "流程状态")
    private Integer status;

    @Column(columnName = "user_id", jdbcType = JdbcType.BIGINT, comment = "执行人", isNull = false)
    private Long userId;

    @Column(columnName = "creator", jdbcType = JdbcType.BIGINT, comment = "创建者")
    private Long creator;

    @Column(columnName = "create_date", jdbcType = JdbcType.DATE, comment = "创建时间")
    private Date createDate;

    @Column(columnName = "modify_date", jdbcType = JdbcType.DATE, comment = "修改日期")
    private Date modifyDate;
}
