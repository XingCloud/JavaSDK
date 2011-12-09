package com.xingcloud.framework.service;

import com.xingcloud.framework.context.Context;
import com.xingcloud.framework.context.ContextAware;
import com.xingcloud.framework.context.application.Application;
import com.xingcloud.framework.service.request.ServiceRequest;
import com.xingcloud.framework.service.response.ServiceResponse;

/**
 * 服务虚基类，提供服务获取相关信息的功能
 * 
 * @deprecated
 */
public abstract class AbstractService implements Service{
	protected Application application;
	protected ServiceRequest request;
	protected ServiceResponse response;
	protected Context context;
	
	/**
	 * 获得Application
	 * @see com.xingcloud.framework.context.application.Application
	 */
	public Application getApplication() {
		return application;
	}
	
	/**
	 * 获得上下文
	 */
	public Context getContext(){
		return this.context;
	}

	/**
	 * 获取服务请求
	 */
	public ServiceRequest getRequest() {
		return request;
	}

	/**
	 * 设置Application
	 */
	public Service setApplication(Application application) {
		this.application = application;
		return this;
	}

	/**
	 * 设置上下文
	 */
	public ContextAware setContext(Context context){
		this.context = context;
		return this;
	}

	/**
	 * 设置服务的请求
	 */
	public Service setRequest(ServiceRequest request) {
		this.request = request;
		return this;
	}

	/**
	 * 获取服务的响应
	 */
	public ServiceResponse getResponse() {
		return response;
	}

	/**
	 * 设置服务的响应
	 */
	public Service setResponse(ServiceResponse response) {
		this.response = response;
		return this;
	}
	
	/** 
	 * 关闭服务
	 */
	public void close(){
		application = null;
		context = null;
		if(request != null){
			request.close();
		}
		request = null;
		if(response != null){
			response.close();
		}
		response = null;
	}
	
}