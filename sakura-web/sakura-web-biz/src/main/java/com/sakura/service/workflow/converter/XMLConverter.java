package com.sakura.service.workflow.converter;

import com.sakura.service.workflow.constants.BpmnXMLConstants;
import com.sakura.service.workflow.model.BaseElement;
import com.sakura.service.workflow.model.BpmnModel;
import com.sakura.service.workflow.model.EndModel;
import com.sakura.service.workflow.model.StartModel;
import lombok.extern.slf4j.Slf4j;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: bi
 * @date: 2021/12/17 10:21
 */
@Slf4j
public class XMLConverter implements BpmnXMLConstants {


    protected static final String DEFAULT_ENCODING = "UTF-8";
    protected static Map<String, BaseXMLConverter> convertersToBpmnMap = new HashMap<String, BaseXMLConverter>();


    static {
        //tasks
        addConverter(new UserTaskXMLConverter());
    }


    public BpmnModel convertToBaseModel(InputStream in, boolean validateSchema, boolean enableSafeBpmnXml) throws XMLStreamException {
        return convertToBaseModel(in,validateSchema,enableSafeBpmnXml,DEFAULT_ENCODING);
    }


    public BpmnModel convertToBaseModel(InputStream in, boolean validateSchema, boolean enableSafeBpmnXml, String encoding) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        return convertToBaseModel(factory.createXMLStreamReader(in));
    }


    public BpmnModel convertToBaseModel(XMLStreamReader xtr) {

        BpmnModel bpmnModel = new BpmnModel();
        try {
            while (xtr.hasNext()) {
                //根据标签调用对应parser解析
                if (ELEMENT_EVENT_START.equals(xtr.getLocalName())) {
                    StartModel startModel = new StartModel();
                    startModel.setName(ELEMENT_EVENT_START);
                    startModel.setNodeType(ELEMENT_EVENT_START);
                    startModel.setNextId(Long.parseLong(xtr.getAttributeValue(0)));
                    startModel.setId((long) (Math.random() * 100));
                    startModel.setExt(xtr.getAttributeValue(1));
                }
                //.... 根据传过来的不同节点 找不同的parser 解析
                else if (ELEMENT_EVENT_END.equals(xtr.getLocalName())) {
                    EndModel endModel = new EndModel();
                    endModel.setName(ELEMENT_EVENT_END);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }


        return bpmnModel;
    }


    public static void addConverter(BaseXMLConverter converter) {
        addConverter(converter, converter.getBpmnElementType());
    }

    public static void addConverter(BaseXMLConverter converter, Class<? extends BaseElement> elementType) {
        convertersToBpmnMap.put(converter.getXMLElementName(), converter);
    }
}
