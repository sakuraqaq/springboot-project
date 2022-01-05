package com.sakura.entity;

import com.sakura.farme.annotation.Column;
import com.sakura.farme.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;

/**
 * @author: bi
 * @date: 2021/12/28 10:10
 */
@Data
@Table(value = "work_flow_user", auto = "add", comment = "工作流操作人表")
public class WorkFlowUser {

    @Column(columnName = "work_flow_user_id", jdbcType = JdbcType.BIGINT, isNull = false, isPrimaryKey = true, comment = "主键Id")
    private Long workFlowUserId;

    @Column(columnName = "work_flow_task_id", jdbcType = JdbcType.BIGINT, isNull = false, comment = "工作流Id")
    private Long workFlowTaskId;

    @Column(columnName = "state", jdbcType = JdbcType.BIGINT, isNull = false, columnDefault = "0", comment = "审批状态")
    private Integer state;

    @Column(columnName = "user_id", jdbcType = JdbcType.BIGINT, comment = "执行人", isNull = false)
    private Long userId;

    @Column(columnName = "create_date", jdbcType = JdbcType.DATE, comment = "创建时间")
    private Date createDate;

    @Column(columnName = "modify_date", jdbcType = JdbcType.DATE, comment = "修改日期")
    private Date modifyDate;


}
