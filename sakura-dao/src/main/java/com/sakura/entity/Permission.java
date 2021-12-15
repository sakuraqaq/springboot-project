package com.sakura.entity;

import com.sakura.farme.annotation.Column;
import com.sakura.farme.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;

@Data
@Table(value = "permission", auto = "add", comment = "权限表")
public class Permission implements Serializable {

    @Column(columnName = "permission_id", jdbcType = JdbcType.BIGINT, isNull = false, isPrimaryKey = true, comment = "主键id")
    private Long permissionId;

    @Column(columnName = "url", length = 2048, comment = "链接", jdbcType = JdbcType.VARCHAR)
    private String url;

    @Column(columnName = "permission", length = 1024, comment = "接口名英文", jdbcType = JdbcType.VARCHAR)
    private String permission;

    @Column(columnName = "name", length = 32, comment = "接口名中文", jdbcType = JdbcType.VARCHAR)
    private String name;
}
