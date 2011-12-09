package com.xingcloud.framework.util.stax;

import java.util.Map;

import javax.xml.stream.events.StartElement;

/**
 * 使用stax方式进行扫描的接口
 * 
 */
public interface StaxAttributeMapVisitor {
	void visit(StartElement element, Map<String, String> map) throws Exception;
}