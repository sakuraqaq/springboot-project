package com.sakura.service.workflow.event;

/**
 * @author: bi
 * @date: 2021/12/16 10:41
 */
public interface FlowNodeEvent {

    /**
     * 流程中的触发事件，控制流程的流向
     */
    void onEvent();
}
