package com.sakura.controller;

import com.sakura.entity.User;
import com.sakura.oss.OssService;
import com.sakura.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public User getUser() {
        return userService.getUser();
    }
}
