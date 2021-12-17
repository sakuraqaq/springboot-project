package com.sakura.service.workflow.converter;

import com.sakura.service.workflow.constants.BpmnXMLConstants;
import com.sakura.service.workflow.model.BaseElement;
import com.sakura.service.workflow.model.BpmnModel;

import lombok.extern.slf4j.Slf4j;

import javax.xml.stream.XMLStreamReader;

/**
 * @author: bi
 * @date: 2021/12/16 15:47
 */
@Slf4j
public abstract class BaseXMLConverter implements BpmnXMLConstants {




    public void convertToBpmnModel(XMLStreamReader xtr, BpmnModel model) throws Exception {

        String elementId = xtr.getAttributeValue(null, ATTRIBUTE_ID);
        String elementName = xtr.getAttributeValue(null, ATTRIBUTE_NAME);

    }

    protected abstract BaseElement convertXMLToElement(XMLStreamReader xtr, BpmnModel model) throws Exception;

    protected abstract Class<? extends BaseElement> getBpmnElementType();

    protected abstract String getXMLElementName();
}
