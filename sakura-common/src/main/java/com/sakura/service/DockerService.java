package com.sakura.service;

import com.sakura.dto.DockerClineDTO;

public interface DockerService {

    void buildImages(DockerClineDTO dockerClineDTO) throws Exception;
}
