package com.sakura.controller;

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


    @NoLoginRequired
    @GetMapping("/users/users")
    public Results<?> setUser(SessionUser sessionUser){
        sessionUser.setLogin(true);
        sessionUser.setUserId(userService.getUser().getUserId());
        return Results.buildSuccess("设置完成", sessionUser);
    }


    @GetMapping("/users")
    public Results<?> getUser(@Valid UserDTO userDTO) {
        return Results.buildSuccess("查询成功", userService.getUser());
    }
}
