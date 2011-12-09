package com.xingcloud.framework.util.stax;

import javax.xml.stream.events.StartElement;

/**
 * 使用stax以元素的方式进行扫描的接口
 * 
 */
public interface StaxElementVisitor {
	void visit(StartElement element) throws Exception;
}