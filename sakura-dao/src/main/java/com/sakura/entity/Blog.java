package com.sakura.entity;

import com.sakura.farme.annotation.Column;
import com.sakura.farme.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;

/**
 * @author: bi
 * @date: 2021/11/23 10:12
 */
@Data
@Table(value = "blog", auto = "add", comment = "博客表")
public class Blog {

    @Column(columnName = "blog_id", jdbcType = JdbcType.BIGINT, isNull = false, isPrimaryKey = true, comment = "主键id", auto = true)
    private Long blogId;

    @Column(columnName = "content", jdbcType = JdbcType.VARCHAR, length = 128, comment = "评论内容")
    private String content;

    @Column(columnName = "username", jdbcType = JdbcType.VARCHAR, length = 32, comment = "用户名")
    private String username;

    @Column(columnName = "create_date", jdbcType = JdbcType.DATE)
    private Date createDate;

    @Column(columnName = "type", jdbcType = JdbcType.SMALLINT, comment = "状态")
    private Short type;

}
