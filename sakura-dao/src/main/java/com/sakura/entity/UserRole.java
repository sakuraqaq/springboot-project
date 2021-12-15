package com.sakura.entity;

import com.sakura.farme.annotation.Column;
import com.sakura.farme.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;

@Data
@Table(auto = "add", value = "user_role")
public class UserRole implements Serializable {

    @Column(isPrimaryKey = true, isNull = false, jdbcType = JdbcType.BIGINT, columnName = "user_role_id", comment = "主键id")
    private Long userRoleId;

    @Column(columnName = "role_id", jdbcType = JdbcType.BIGINT, comment = "角色Id")
    private Long roleId;

    @Column(columnName = "user_id", jdbcType = JdbcType.BIGINT, comment = "权限id")
    private Long userId;

}
