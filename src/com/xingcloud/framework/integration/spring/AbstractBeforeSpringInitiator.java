package com.xingcloud.framework.integration.spring;

/**
 * spring加载开始前初始化工作的虚基类
 * 
 */
public abstract class AbstractBeforeSpringInitiator implements BeforeSpringInitiator{
	protected String basePath;

	@Override
	public String getBasePath() {
		return basePath;
	}

	@Override
	public void setBasePath(String basePath) {
		this.basePath = basePath;		
	}
	
	@Override
	public abstract void init() throws Exception;
}