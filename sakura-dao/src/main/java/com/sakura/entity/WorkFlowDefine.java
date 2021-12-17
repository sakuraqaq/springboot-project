package com.sakura.entity;

import com.sakura.farme.annotation.Column;
import com.sakura.farme.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

/**
 * @author: bi
 * @date: 2021/12/17 14:05
 */
@Data
@Table(value = "work_flow_define", auto = "add", comment = "工作流定义表")
public class WorkFlowDefine {

    @Column(columnName = "work_flow_define_id", jdbcType = JdbcType.BIGINT, isNull = false, isPrimaryKey = true, comment = "主键id")
    private Long workFlowDefineId;

    @Column(columnName = "define_name", jdbcType = JdbcType.VARCHAR, comment = "流程定义名称", length = 16)
    private String defineName;

    @Column(columnName = "version", jdbcType = JdbcType.BIGINT, comment = "版本", isNull = false, columnDefault = "0")
    private Integer version;

    @Column(columnName = "status", jdbcType = JdbcType.BIGINT, comment = "流程定义状态", isNull = false, columnDefault = "1")
    private Integer status;


}
