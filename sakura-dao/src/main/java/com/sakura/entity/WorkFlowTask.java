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

    @Column(columnName = "work_flow_task_name", jdbcType = JdbcType.VARCHAR, isNull = false, length = 16, comment = "工作流节点名称")
    private String workFlowTaskName;

    @Column(columnName = "id", jdbcType = JdbcType.VARCHAR, length = 16, comment = "节点Id")
    private String id;

    @Column(columnName = "source_id", jdbcType = JdbcType.VARCHAR, length = 16, comment = "节点入口")
    private String sourceId;

    @Column(columnName = "target_id", jdbcType = JdbcType.VARCHAR, length = 16, comment = "节点出口")
    private String targetId;

    @Column(columnName = "node_condition", jdbcType = JdbcType.VARCHAR, length = 16, comment = "节点条件")
    private String condition;

    @Column(columnName = "node_type", jdbcType = JdbcType.VARCHAR, length = 16, comment = "任务类别")
    private String nodeType;

    @Column(columnName = "work_flow_key", jdbcType = JdbcType.VARCHAR, length = 16, comment = "操作类的key")
    private String workFlowKey;

    @Column(columnName = "state", jdbcType = JdbcType.BIGINT,  columnDefault = "0", comment = "流程状态")
    private Integer state;

    @Column(columnName = "create_date", jdbcType = JdbcType.DATE, comment = "创建时间")
    private Date createDate;

    @Column(columnName = "modify_date", jdbcType = JdbcType.DATE, comment = "修改日期")
    private Date modifyDate;
}
