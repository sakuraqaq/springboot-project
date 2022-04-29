package com.sakura.Task;

import com.sakura.quartz.ScheduleJob;
import org.springframework.stereotype.Component;

/**
 * @author: bi
 * @date: 2022/4/21 14:57
 */
//@Component
public class TestTask implements ScheduleJob {
    @Override
    public void execute() {
        System.out.println("TestTask运行中");
    }

    @Override
    public String getJobName() {
        return "testTask";
    }

    @Override
    public String getJobGroup() {
        return "testTask";
    }

    @Override
    public boolean isSync() {
        return false;
    }

    @Override
    public String getCronExpression() {
        return "* * * * * ? *";
    }

    @Override
    public void setParams(String s) {

    }
}
