package com.sakura.service.workflow.converter.util;

import com.sakura.service.workflow.constants.XMLConstants;
import com.sakura.service.workflow.model.WorkFlowNode;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: bi
 * @date: 2021/12/16 13:53
 */
@Slf4j
public class BpmnXMLUtil implements XMLConstants {

    public static String workFlowName;

    public static List<WorkFlowNode> parse(String filePath) {
        try {
            File file = new File(filePath);
            SAXReader reader = new SAXReader();
            Document document = reader.read(file); //读取xml 文件获取Document文件
            Element rootElement = document.getRootElement().getName().equals("cim-workFlow") ? document.getRootElement() : null;//获取根节点
            Assert.notNull(rootElement, "rootElement was null");
            Element element = rootElement.element("process") != null ? rootElement.element("process"):null;
            Assert.notNull(element, "process(流程) was null");
            workFlowName = element.attribute("name").getText();
            Iterator<Element> elementIterator = element.elementIterator();//遍历所有子节点
            List<WorkFlowNode> list = new ArrayList<>();
            elementIterator.forEachRemaining(it -> {
                String text = it.getName();
                switch (text) {
                    case ELEMENT_EVENT_START:  //开始节点和结束节点特殊处理
                    case ELEMENT_EVENT_END:
                    case ELEMENT_TASK_USER:  //如果是用户节点
                        list.add(setWorkFlowNode(it));
                        break;
                    case BRANCH: //如果是分支任务
                        it.elementIterator().forEachRemaining(branch -> {
                            list.add(setWorkFlowNode(branch));
                        });
                        break;
                }
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static WorkFlowNode setWorkFlowNode(Element it) {

        WorkFlowNode workFlowNode = new WorkFlowNode();
        String type = it.getName();
        Attribute name = it.attribute(ATTRIBUTE_NAME);
        Attribute id = it.attribute(ATTRIBUTE_ID);
        Attribute nodeKey = it.attribute(ATTRIBUTE_KEY);
        Attribute sourceRef = it.attribute(ATTRIBUTE_SOURCE);
        Attribute targetRef = it.attribute(ATTRIBUTE_TARGET);

        workFlowNode.setId(id.getText());
        workFlowNode.setName(name.getText());
        workFlowNode.setNodeKey(nodeKey.getText());
        workFlowNode.setNodeType(type);
        workFlowNode.setSourceId(sourceRef.getText());
        workFlowNode.setTargetId(targetRef.getText());

        //查看节点是否有条件
        Element element = it.element(ELEMENT_CONDITION);
        if (element != null) {
            String condition = element.attribute("value").getText();//内容
            workFlowNode.setCondition(condition);
        }

        return workFlowNode;

    }
}
