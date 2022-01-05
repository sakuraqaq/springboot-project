package com.sakura.entity;

import com.sakura.farme.annotation.Column;
import com.sakura.farme.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;

/**
 * @author: bi
 * @date: 2021/12/17 14:13
 */
@Data
@Table(value = "work_flow", auto = "add", comment = "工作流表")
public class WorkFlow {

    @Column(columnName = "work_flow_id", jdbcType = JdbcType.BIGINT, isNull = false, isPrimaryKey = true, comment = "主键Id")
    private Long workFlowId;

    @Column(columnName = "node_id", jdbcType = JdbcType.BIGINT, comment = "正在执行的当前节点ID")
    private Long nodeId;

    @Column(columnName = "next_node_id", jdbcType = JdbcType.BIGINT, comment = "下一个节点Id")
    private Long nextNodeId;

    @Column(columnName = "work_flow_name", jdbcType = JdbcType.VARCHAR, length = 16, comment = "工作流名称")
    private String workFlowName;

    @Column(columnName = "status", jdbcType = JdbcType.BIGINT,  columnDefault = "0", comment = "流程状态")
    private Integer status;

    @Column(columnName = "create_date", jdbcType = JdbcType.DATE, comment = "创建时间")
    private Date createDate;

    @Column(columnName = "modify_date", jdbcType = JdbcType.DATE, comment = "修改日期")
    private Date modifyDate;
}
