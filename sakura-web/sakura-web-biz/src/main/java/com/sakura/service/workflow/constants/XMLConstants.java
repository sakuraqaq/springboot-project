package com.sakura.service.workflow.constants;

/**
 * @author: bi
 * @date: 2021/12/16 15:43
 */
public interface XMLConstants {

    String ATTRIBUTE_ID = "id";
    String ATTRIBUTE_NAME = "name";
    String ATTRIBUTE_TYPE = "nodeType";
    String ATTRIBUTE_KEY = "nodeKey";
    String ATTRIBUTE_SOURCE = "sourceRef";
    String ATTRIBUTE_TARGET="targetRef";


    String ELEMENT_EVENT_START = "startEvent";
    String ELEMENT_EVENT_END = "endEvent";
    String ELEMENT_CONDITION = "conditionExpression";

    String ELEMENT_TASK = "task";
    String ELEMENT_TASK_BUSINESSRULE = "businessRuleTask";
    String ELEMENT_TASK_MANUAL = "manualTask";
    String ELEMENT_TASK_RECEIVE = "receiveTask";
    String ELEMENT_TASK_SCRIPT = "scriptTask";
    String ELEMENT_TASK_SEND = "sendTask";
    String ELEMENT_TASK_SERVICE = "serviceTask";
    String ELEMENT_TASK_USER = "userTask";
    String SEQUENCE_FLOW="sequenceFlow";
    String ELEMENT_CALL_ACTIVITY = "callActivity";


    String BRANCH="branch";

    /**
     * 指定多实例按照并行或者串行方式进行 true时表示串行
     */
    String ATTRIBUTE_MULTIINSTANCE_SEQUENTIAL = "isSequential";

    /**
     * 指定多实例任务
     */
    String ELEMENT_MULTIINSTANCE = "multiinstance";

    /**
     * 用于执行会签任务参与的人
     */
    String ATTRIBUTE_MULTIINSTANCE_COLLECTION = "collection";

    /**
     * 表示每个分支都有一个名叫xxx的流程变量和task节点中属性中的assignee一致
     */
    String ATTRIBUTE_MULTIINSTANCE_VARIABLE = "elementVariable";

    /**
     * 表示会签节点的结束条件
     */
    String ELEMENT_MULTIINSTANCE_CONDITION = "completionCondition";


    String ATTRIBUTE_TASK_USER_ASSIGNEE = "assignee";
    String ATTRIBUTE_TASK_USER_OWNER = "owner";
}
