package com.sakura.service.workflow;

import com.sakura.entity.WorkFlowDefine;
import com.sakura.entity.WorkFlowTask;
import com.sakura.mapper.WorkFlowDefineMapper;
import com.sakura.mapper.WorkFlowMapper;
import com.sakura.mapper.WorkFlowTaskMapper;
import com.sakura.service.WorkFlowService;
import com.sakura.uid.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * @author: bi
 * @date: 2021/12/16 11:06
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WrokFlowServiceImpl implements WorkFlowService {

    private final WorkFlowDefineMapper workFlowDefineMapper;
    private final WorkFlowMapper workFlowMapper;
    private final WorkFlowTaskMapper workFlowTaskMapper;
    private final IdGenerator idGenerator;

    @Override
    public void startProcess() {

        WorkFlowDefine workFlowDefine = new WorkFlowDefine();
        workFlowDefine.setWorkFlowDefineId(idGenerator.getUID());
        workFlowDefine.setDefineName("oneWorkFlowDefine");
        workFlowDefine.setVersion(1);

        long workId = idGenerator.getUID();
        WorkFlowTask workFlowTask = new WorkFlowTask();
//        workFlowTask.setWorkFlowId(workId);
        workFlowTask.setWorkFlowTaskId(workId);
        workFlowTask.setWorkFlowTaskName("oneTaskProcess");
        workFlowTask.setStatus(0);
        workFlowTask.setCreateDate(Calendar.getInstance().getTime());

    }

    @Override
    public void endProcess() {

    }
}
