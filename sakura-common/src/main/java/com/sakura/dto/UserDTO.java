package com.sakura.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDTO {


    @NotBlank
    private String username;
}
