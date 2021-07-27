package com.sakura.dto;

import lombok.Data;

@Data
public class DockerClineDTO {


    private String dockerFile;

    private String dockerUserName;

    private String dockerPassWord;

    private String dockerHost;

    private String dockerUrl;

    private String tagName;

    private String gitUserName;

    private String gitPassword;

    private String gitUrl;

    private String branch;

    private String webSocketPort;



}
