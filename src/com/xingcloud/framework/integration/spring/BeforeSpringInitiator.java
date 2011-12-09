package com.xingcloud.framework.integration.spring;

import com.xingcloud.framework.initiator.Initiator;

/**
 * spring加载开始前初始化工作的接口
 * 
 */
public interface BeforeSpringInitiator extends Initiator{
	String getBasePath();
	void setBasePath(String basePath);
}