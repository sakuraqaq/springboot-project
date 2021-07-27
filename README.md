# springboot-project

springboot 多模块项目的创建模板

2021 7/27 新增oss + websocket + 自定义 mybatis-plus


yml文件：
---------
sakura:<br>
  datasource:<br>
    entityPackage: com.sakura.entity<br>
    name: sakura<br>
    basePackage: com.sakura.mapper<br>
    qualifier: sakuraQualifier<br>
    jdbcInfos:<br>
      url:<br>
      username:<br>
      password:<br>
      initialSize: 10<br>
      maxActive: 20<br>
<br>
idGenerator:<br>
  timeBits: 28<br>
  workerBits: 22<br>
  seqBits: 13<br>
  epochStr: 1622875679513<br>

  
在启动类启用 自定义mybaits-plus
---------

@EnableSakuraMybatis<br>
@SpringBootApplication<br>
@ComponentScan({"com.sakura"})<br>
public class SakuraWebApplication {<br>
}<br>

自动建表功能：
--------

@Data<br>
@Table(value = "user", auto = "auto")<br>
public class User implements Serializable {<br>
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
}<br>

mapper 接口需要实现 BaseMapper<br>

public interface UserMapper extends BaseMapper<User, Long> {<br>
}<br>
IdGenerator 是基于雪花算法的id生成器
-----------


@Service<br>
@RequiredArgsConstructor(onConstructor = @__(@Autowired))<br>
public class UserServiceImpl implements UserService {<br>

    private final IdGenerator idGenerator;<br>
    private final UserMapper userMapper;<br>
    private final OssService ossService;<br>

    @Override<br>
    public User getUser() {<br>
        System.out.println(idGenerator.getUID()+"\r\n"+ossService);<br>
        return  userMapper.selectOne(new QueryWrapper<User>()<br>
                .eq(User::getPhoneNumber, "17610068303"));<br>
    }<br>
}<br>
