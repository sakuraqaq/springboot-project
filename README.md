# springboot-project

springboot 多模块项目的创建模板

2021 7/27 新增oss + websocket + 自定义 mybatis-plus


yml文件：
--------------------------------------------------------------
server:
  port: 4396

sakura:
  datasource:
    entityPackage: com.sakura.entity
    name: sakura
    basePackage: com.sakura.mapper
    qualifier: sakuraQualifier
    jdbcInfos:
      url:
      username:
      password:
      initialSize: 10
      maxActive: 20

idGenerator:
  timeBits: 28
  workerBits: 22
  seqBits: 13
  epochStr: 1622875679513
  --------------------------------------------------------------
  
启用 自定义mybaits-plus-----------------------------------------
package com.sakura.application;

import com.sakura.farme.annotation.EnableSakuraMybatis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author : bi
 * @since : 2021年06月24日
 */
@EnableSakuraMybatis
@SpringBootApplication
@ComponentScan({"com.sakura"})
public class SakuraWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SakuraWebApplication.class, args);
    }
}
--------------------------------------------------------------------------------------------
自动建表功能：
 @Data
@Table(value = "user", auto = "auto")
public class User implements Serializable {


    @Column(columnName = "user_id", isPrimaryKey = true, isNull = false, jdbcType = JdbcType.BIGINT,length = 32, comment = "用户id")
    private Long userId;

    @Column(columnName = "user_name", jdbcType = JdbcType.VARCHAR,length = 32, comment = "用户名")
    private String userName;

    @Column(columnName = "age", jdbcType = JdbcType.BIGINT, comment = "年龄")
    private Integer age;

    @Column(columnName = "create_date", jdbcType = JdbcType.DATE)
    private Date createDate;

    @Column(columnName = "count", jdbcType = JdbcType.BIGINT)
    private Integer count;

    @Column(columnName = "phone_number", jdbcType = JdbcType.VARCHAR, length = 32, comment = "手机号")
    private String phoneNumber;

    @Column(columnName = "pass_word", jdbcType = JdbcType.VARCHAR, length = 32, comment = "密码")
    private String password;
}
-----------------------------------------------------------------------------------------------------------------------------
mapper 接口需要实现 BaseMapper
package com.sakura.mapper;

import com.sakura.entity.User;
import com.sakura.farme.base.BaseMapper;

/**
 * @author : bi
 * @since : 2021年06月24日
 */
public interface UserMapper extends BaseMapper<User, Long> {
}
-------------------------------------------------------------------------------------------------------------------------------
IdGenerator 是基于雪花算法的id生成器
/**
 * @author : bi
 * @since : 2021年06月24日
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final IdGenerator idGenerator;
    private final UserMapper userMapper;
    private final OssService ossService;

    @Override
    public User getUser() {
        System.out.println(idGenerator.getUID()+"\r\n"+ossService);
        return  userMapper.selectOne(new QueryWrapper<User>()
                .eq(User::getPhoneNumber, "17610068303"));
    }
}
---------------------------------------------------------------------------------------------------------------------------------------


