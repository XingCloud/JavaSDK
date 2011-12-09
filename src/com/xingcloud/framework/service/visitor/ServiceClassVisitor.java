package com.xingcloud.framework.service.visitor;



/**
 * 服务类扫描接口
 * 
 */
public interface ServiceClassVisitor {
	@SuppressWarnings("rawtypes")
	void visit(String serviceId, Class clazz);
}