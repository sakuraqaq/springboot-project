package com.sakura.service.workflow.model;

import lombok.Data;

import java.util.List;

/**
 * @author: bi
 * @date: 2022/2/15 10:06
 */
@Data
public class WorkFLowParam {


    private String key;

    private List<Long> users;

}
