package com.xingcloud.framework.service.config;

import com.xingcloud.framework.bean.config.BeanConfig;

/**
 * 储存服务信息的容器类
 * 
 */
public class ServiceConfig extends BeanConfig{

	private static final long serialVersionUID = 1L;
	
	/**
	 * ServiceConfig在环境中的标识
	 */
	public static final String SERVICE = "service";
	
	protected String api; 
	
	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}
}