package com.xingcloud.framework.service.visitor;

import java.lang.reflect.Method;

/**
 * 服务方法扫描接口
 * 
 */
public interface ServiceMethodVisitor {
	void visit(String serviceId, Method method);
}