package com.xingcloud.framework.service.visitor;

import com.xingcloud.framework.service.config.ServiceConfig;

/**
 * 服务配置扫描接口
 * 
 */
public interface ServiceConfigVisitor {
	void visit(ServiceConfig serviceConfig);
}