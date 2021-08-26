package com.sakura.entity;

import com.sakura.farme.annotation.Column;
import com.sakura.farme.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;

@Data
@Table(value = "role_permission", auto = "add")
public class RolePermission implements Serializable {

    @Column(columnName = "role_permission_id", jdbcType = JdbcType.BIGINT, comment = "主键ID", isPrimaryKey = true, isNull = false, length = 32)
    private Long rolePermissionId;

    @Column(columnName = "role_id", jdbcType = JdbcType.BIGINT, comment = "角色Id", length = 32)
    private Long roleId;

    @Column(columnName = "permission_id", jdbcType = JdbcType.BIGINT, comment = "权限Id", length = 32)
    private Long permissionId;
}
