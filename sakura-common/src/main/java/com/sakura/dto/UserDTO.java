package com.sakura.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDTO {


    @NotBlank(message = "用户名不能为空")
    private String username;
}
