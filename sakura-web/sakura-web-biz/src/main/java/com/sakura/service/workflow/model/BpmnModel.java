package com.sakura.service.workflow.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author: bi
 * @date: 2021/12/16 14:42
 * 流程模型
 */
@Data
public class BpmnModel implements Serializable {

    private static final long serialVersionUID = -3978388377128544781L;

    private Long flowId;

    private String flowName;

    private Map<Long, BaseModel> nodeList;

    /**
     * 通过节点id获取节点
     */
    public BaseModel getNodeModel(Long nodeId) {
        if (nodeList == null || nodeList.isEmpty())
            return null;
        return nodeList.get(nodeId);
    }


    public StartModel getStartModel() {
        if (nodeList == null || nodeList.isEmpty())
            return null;
        for (Map.Entry<Long, BaseModel> entry : nodeList.entrySet()) {
            BaseModel baseModel = entry.getValue();
            if (baseModel instanceof StartModel)
                return (StartModel) baseModel;
        }
        return null;
    }

    /**
     * 通过当前节点返回上一个节点
     * @return
     */
    public BaseModel getPreNodeModel(Long nodeId){
        if (nodeList == null || nodeList.isEmpty())
            return null;
        for (Map.Entry<Long, BaseModel> entry : nodeList.entrySet()) {
            BaseModel baseModel = entry.getValue();
            if(baseModel.getNextId().equals(nodeId))
                return baseModel;
        }
        return null;
    }


    public EndModel getEndModel() {
        if (nodeList == null || nodeList.isEmpty())
            return null;
        for (Map.Entry<Long, BaseModel> entry : nodeList.entrySet()) {
            BaseModel baseModel = entry.getValue();
            if (baseModel instanceof EndModel)
                return (EndModel) baseModel;
        }
        return null;
    }

}
