package com.sakura.entity;

import com.sakura.farme.annotation.Column;
import com.sakura.farme.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.util.Date;

@Data
@Table(value = "role", auto = "add", comment = "角色")
public class Role implements Serializable {

    @Column(columnName = "role_id", isPrimaryKey = true, isNull = false, jdbcType = JdbcType.BIGINT, comment = "主键ID")
    private Long roleId;

    @Column(columnName = "name", jdbcType = JdbcType.VARCHAR, comment = "角色名,用于权限校验", length = 16)
    private String name;

    @Column(columnName = "nick_name", jdbcType = JdbcType.VARCHAR, comment = "角色中文名,用于显示", length = 16)
    private String nickname;

    @Column(columnName = "creator", jdbcType = JdbcType.BIGINT, comment = "创建者",isNull = false)
    private Long creator;

    @Column(columnName = "create_date", jdbcType = JdbcType.DATE)
    private Date createDate;

    @Column(columnName = "modifier", jdbcType = JdbcType.BIGINT, comment = "修改者")
    private Long modifier;

    @Column(columnName = "modify_date", jdbcType = JdbcType.DATE, comment = "修改日期")
    private Date modifyDate;

}
