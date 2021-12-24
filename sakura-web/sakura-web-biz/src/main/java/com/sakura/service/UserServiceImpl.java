package com.sakura.service;

import com.alibaba.fastjson.JSONObject;
import com.sakura.component.SakuraSessionUser;
import com.sakura.dto.UserDTO;
import com.sakura.entity.*;
import com.sakura.farme.pojo.Results;
import com.sakura.farme.wapper.QueryWrapper;
import com.sakura.mapper.*;
import com.sakura.service.workflow.IOperator;
import com.sakura.service.workflow.ProcessEngine;
import com.sakura.uid.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

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
    private final WorkFlowTaskMapper workFlowTaskMapper;
    private final Map<String, IOperator> iOperator;

    @Override
    public User getUser(SakuraSessionUser sessionUser) {
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq(User::getPhoneNumber, "17610068303"));
        sessionUser.setUserId(user.getUserId());
        sessionUser.setUsername(user.getUsername());
        sessionUser.setLogin(true);
        //根据用户查询得到角色
        UserRole userRole = userRoleMapper.selectOne(new QueryWrapper<UserRole>().eq(UserRole::getUserId, user.getUserId()));
        Role role = roleMapper.selectOne(new QueryWrapper<Role>().eq(Role::getRoleId, userRole.getRoleId()));
        //根据角色ID查询到对应的权限ID
        RolePermission rolePermission = rolePermissionMapper.selectOne(new QueryWrapper<RolePermission>().eq(RolePermission::getRoleId, role.getRoleId()));
        List<Permission> permissions = permissionMapper.selectList(new QueryWrapper<Permission>().eq(Permission::getPermissionId, rolePermission.getPermissionId()));
        List<String> pers = new ArrayList<>();
        permissions.forEach(p -> {
            pers.add(p.getPermission());
        });
        sessionUser.setPermission(user.getUserId(), JSONObject.toJSONString(pers));
        return user;
    }

    @Override
    public int setUser(UserDTO userDTO) {

        User user = userMapper.selectOne(new QueryWrapper<User>()
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
        Object[] objects = new Object[1];
        ProcessEngine processEngine = new ProcessEngine(workFlowTaskMapper, workFlowId, iOperator);
        processEngine.start(objects);
        return Results.buildSuccess("执行工作流", 0);
    }
}
