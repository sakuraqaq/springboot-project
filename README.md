# springboot-project

springboot 多模块项目的创建模板

2021 7/27 新增oss + websocket + 自定义 mybatis-plus
2022 4/18 自定义mybatis-plus 改为 mybatis-plus 3.5.1官方版本  使用@EnableCreateTable可以自动建表
2022 4/25 改为多数据源配置

yml文件：
---------
```
server:
  port: 4398

spring:
  datasource:
    dynamic:
      primary: master
      strict: false
      datasource:
        master:
          url: 
          username: 
          password: 
          initialSize: 5
          maxActive: 20
          basePackage: com.sakura.mapper
          entityPackage: com.sakura.entity
          qualifier: sakuraQualifier
        slave_1:
          url: 
          username: 
          password: 
          initialSize: 5
          maxActive: 20
          basePackage: com.sakura.mapper
          entityPackage: com.sakura.entity.zhgd
```
 

在启动类启用 自动建表工具
---------
```java
@EnableCreateTable
@MapperScan("com.sakura.mapper")
@SpringBootApplication(scanBasePackages = {"com.sakura"})
public class SakuraWebApplication {
}
```

自动建表功能：
--------

```java
@Data
@Table(value = "user", auto = "auto")
public class User implements Serializable {


    @Column(columnName = "user_id", isPrimaryKey = true, isNull = false, jdbcType = JdbcType.BIGINT, comment = "用户id")
    private Long userId;

    @Column(columnName = "username", jdbcType = JdbcType.VARCHAR, length = 32, comment = "用户名")
    private String username;

    @Column(columnName = "age", jdbcType = JdbcType.BIGINT, comment = "年龄")
    private Integer age;

    @Column(columnName = "create_date", jdbcType = JdbcType.DATE)
    private Date createDate;

    @Column(columnName = "count", jdbcType = JdbcType.BIGINT)
    private Integer count;

    @Column(columnName = "phone_number", jdbcType = JdbcType.VARCHAR, length = 32, comment = "手机号")
    private String phoneNumber;

    @Column(columnName = "password", jdbcType = JdbcType.VARCHAR, length = 32, comment = "密码")
    private String password;
}
```

#mapper 接口需要实现 BaseMapper<br>

```java
public interface UserMapper extends BaseMapper<User> {
}
```

#IdGenerator 是基于雪花算法的id生成器

```java
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
```
***
#2021/8/13 新增基于RedisTemplate的Redis

在启动类加上注解 @EnableRedis <br>
在yml文件中配置：<br>
````
sakura
    redis:
    host: xxxx
    port: 6379
    password: xxxx
    timeout: 10000
    jedisConfig:
      maxActive: 600
      maxWait: 3000
      maxIdle: 10
      minIdle: 4
````


***
#2021/08/19基于redis的登录认证
启动类加入注解 @EnableRedisSession
在配置文件中加入:
````
sakura
  web:
    sessionCookieName: SakuraSessionDev
    sessionCookieDomain: aichidoubao.com
    sessionCacheKeyPre: sakura.session
    sessionExpireSeconds: 604800
````

***
#定时任务
````java
package com.sakura.Task;

import com.sakura.quartz.ScheduleJob;
import org.springframework.stereotype.Component;

/**
 * @author: bi
 * @date: 2022/4/21 14:57
 */
@Component
public class TestTask implements ScheduleJob {
    @Override
    public void execute() {
        System.out.println("TestTask运行中");
    }

    //任务name和group 用于区分多个定时任务
    @Override
    public String getJobName() {
        return "testTask";
    }

    @Override
    public String getJobGroup() {
        return "testTask";
    }

    //是否让任务同步执行or异步执行
    @Override
    public boolean isSync() {
        return false;
    }

    //这里写cron表达式
    @Override
    public String getCronExpression() {
        return "* * * * * ? *";
    }

    @Override
    public void setParams(String s) {

    }
}

````