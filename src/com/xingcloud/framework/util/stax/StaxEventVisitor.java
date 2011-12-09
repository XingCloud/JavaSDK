package com.xingcloud.framework.util.stax;

import javax.xml.stream.events.XMLEvent;


/**
 * 使用stax以事件的方式进行扫描的接口
 * 
 */
public interface StaxEventVisitor {
	void visit(XMLEvent event) throws Exception;
}