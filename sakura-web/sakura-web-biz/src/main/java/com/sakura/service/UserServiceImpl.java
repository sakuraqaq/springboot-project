package com.sakura.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sakura.component.SakuraSessionUser;
import com.sakura.dto.UserDTO;
import com.sakura.entity.*;
import com.sakura.farme.pojo.Results;
import com.sakura.mapper.*;
import com.sakura.service.workflow.ProcessEngine;
import com.sakura.uid.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author : bi
 * @since : 2021年06月24日
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final IdGenerator idGenerator;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionMapper permissionMapper;
    private final UserRoleMapper userRoleMapper;
    private final ProcessEngine processEngine;

    @Override
    public User getUser(SakuraSessionUser sessionUser) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhoneNumber, "17610068303"));
        sessionUser.setUserId(user.getUserId());
        sessionUser.setUsername(user.getUsername());
        sessionUser.setLogin(true);
        //根据用户查询得到角色
        UserRole userRole = userRoleMapper.selectOne(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getUserId()));
        Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleId, userRole.getRoleId()));
        //根据角色ID查询到对应的权限ID
        RolePermission rolePermission = rolePermissionMapper.selectOne(new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, role.getRoleId()));
        List<Permission> permissions = permissionMapper.selectList(new LambdaQueryWrapper<Permission>().eq(Permission::getPermissionId, rolePermission.getPermissionId()));
        List<String> pers = new ArrayList<>();
        permissions.forEach(p -> {
            pers.add(p.getPermission());
        });
        sessionUser.setPermission(user.getUserId(), JSONObject.toJSONString(pers));
        return user;
    }

    @Override
    public int setUser(UserDTO userDTO) {

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhoneNumber, userDTO.getUsername()));

        Role role = new Role();
        role.setRoleId(idGenerator.getUID());
        role.setCreator(user.getUserId());
        role.setCreateDate(Calendar.getInstance().getTime());
        role.setName("user");
        role.setNickname("普通用户");
        roleMapper.insert(role);

        UserRole userRole = new UserRole();
        userRole.setUserRoleId(idGenerator.getUID());
        userRole.setUserId(user.getUserId());
        userRole.setRoleId(role.getRoleId());
        userRoleMapper.insert(userRole);

        Permission permission = new Permission();
        permission.setPermissionId(idGenerator.getUID());
        permission.setName("获取用户");
        permission.setPermission("users");
        permissionMapper.insert(permission);

        RolePermission rolePermission = new RolePermission();
        rolePermission.setRolePermissionId(idGenerator.getUID());
        rolePermission.setRoleId(role.getRoleId());
        rolePermission.setPermissionId(permission.getPermissionId());
        rolePermissionMapper.insert(rolePermission);

        return 0;
    }

    @Override
    public Results<?> approve(Long workFlowId) {
        Map<String, Object> hashMap = new HashMap();
        hashMap.put("workFlowId", workFlowId);
        hashMap.put("userId", 123L);
        hashMap.put("state", 1);
     //   processEngine.start(hashMap);
        return Results.buildSuccess("执行工作流", 0);
    }


    @Override
    public Results<?> approve1(Long workFlowId) {
        //processEngine.init();发起一个工作流
        Map<String, Object> hashMap = new HashMap();
        hashMap.put("workFlowId", workFlowId);
        hashMap.put("userId", 123L);
        hashMap.put("state", 1);
     //   processEngine.start(hashMap);
        return Results.buildSuccess("执行工作流", 0);
    }


//    JSONReader jsonReader = new JSONReader(new FileReader(path + File.separator + fileInfo.getPath()));
//            jsonReader.startObject();
//            while (jsonReader.hasNext()) {
//        if ("Geomery".equals(jsonReader.readString())) {
//            jsonReader.startArray();
//            while (jsonReader.hasNext()){
//                Geomery geomery = new Geomery();
//                jsonReader.startObject();
//                while(jsonReader.hasNext()){
//                    String key = jsonReader.readString();
//                    if("material".equals(key)){
//                        geomery.setMaterial(jsonReader.readInteger());
//                    }else if("type".equals(key)){
//                        geomery.setType(jsonReader.readString());
//                    }else if("uuid".equals(key)){
//                        geomery.setUuid(jsonReader.readInteger());
//                    }else if("vertices".equals(key)){
//                        jsonReader.startArray();
//                        List<Long> points = new ArrayList<>();
//                        while(jsonReader.hasNext()){
//                            points.add(jsonReader.readLong());
//                        }
//                        jsonReader.endArray();
//                        geomery.setVertices(points);
//                    }else if("visible".equals(key)){
//                        geomery.setVisible(jsonReader.readObject(Boolean.class));
//                    }
//                }
//                geomeries.add(geomery);
//                jsonReader.endObject();
//            }
//            jsonReader.endArray();
//        }
//    }
//            jsonReader.endObject();
//            jsonReader.close();
}
