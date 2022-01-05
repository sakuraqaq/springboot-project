package com.sakura.controller;

import com.sakura.component.SakuraSessionUser;
import com.sakura.dto.UserDTO;
import com.sakura.farme.pojo.Results;
import com.sakura.service.UserService;
import com.sakura.web.annotation.NoLoginRequired;
import com.sakura.web.session.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author : bi
 * @since : 2021年06月24日
 */

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;


    @GetMapping("/users/users")
    public Results<?> setUser(@Valid UserDTO userDTO, SessionUser sessionUser){
        return Results.buildSuccess("添加成功", userService.setUser(userDTO));
    }


    @NoLoginRequired
    @GetMapping("/users")
    public Results<?> getUser(@Valid UserDTO userDTO, SakuraSessionUser sessionUser) {
        return Results.buildSuccess("获取完毕", userService.getUser(sessionUser));
    }

    @NoLoginRequired
    @GetMapping("/approves")
    public Results<?> approve(Long workFlowId){
        return Results.buildSuccess("点击审批", userService.approve(workFlowId));
    }

    @NoLoginRequired
    @GetMapping("/approves/approves")
    public Results<?> approve1(Long workFlowId){
        return Results.buildSuccess("点击审批", userService.approve1(workFlowId));
    }

    @GetMapping("/users/list")
    public Results<?> userList(){
        return Results.buildSuccess("获取完毕","1");
    }
}
