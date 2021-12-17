package com.sakura.service.workflow.converter;


import com.sakura.service.workflow.model.BaseElement;
import com.sakura.service.workflow.model.BpmnModel;

import javax.xml.stream.XMLStreamReader;

/**
 * @author: bi
 * @date: 2021/12/17 10:23
 */
public class UserTaskXMLConverter extends BaseXMLConverter {



    @Override
    protected BaseElement convertXMLToElement(XMLStreamReader xtr, BpmnModel model) throws Exception {
        return null;
    }

    @Override
    protected Class<? extends BaseElement> getBpmnElementType() {
        return null;
    }

    @Override
    protected String getXMLElementName() {
        return null;
    }


}
