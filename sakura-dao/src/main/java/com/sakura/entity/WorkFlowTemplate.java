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
@Table(value = "work_flow_template", auto = "add", comment = "工作流模板文件")
public class WorkFlowTemplate {

    @Column(columnName = "work_flow_template_id", jdbcType = JdbcType.BIGINT, isNull = false, isPrimaryKey = true, comment = "主键id")
    private Long workFlowTemplateId;

    @Column(columnName = "template_id", jdbcType = JdbcType.VARCHAR, comment = "模板key", length = 16)
    private String templateId;

    @Column(columnName = "template_name", jdbcType = JdbcType.VARCHAR, comment = "模板名称", length = 16)
    private String templateName;


}
