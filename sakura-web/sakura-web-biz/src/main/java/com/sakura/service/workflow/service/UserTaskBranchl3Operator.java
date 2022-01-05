package com.sakura.service.workflow.service;

import com.sakura.service.workflow.IOperator;
import com.sakura.service.workflow.model.ProcessNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: bi
 * @date: 2021/12/27 10:04
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserTaskBranchl3Operator implements IOperator {
    @Override
    public String getKey() {
        return "branchTask3";
    }

    @Override
    public ProcessNode doTask(Map<String, Object> param) {
        log.info("分支任务3");
        ProcessNode processNode = new ProcessNode();
        processNode.setParam(param);
        return processNode;
    }
}
