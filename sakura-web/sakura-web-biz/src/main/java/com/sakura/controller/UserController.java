package com.sakura.controller;

import com.sakura.dto.UserDTO;
import com.sakura.farme.pojo.Results;
import com.sakura.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    @GetMapping("/users")
    public Results<?> getUser(@Valid UserDTO userDTO) {
        return Results.buildSuccess("查询成功", userService.getUser());
    }
}
