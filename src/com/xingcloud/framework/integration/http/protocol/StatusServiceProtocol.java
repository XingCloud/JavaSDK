package com.xingcloud.framework.integration.http.protocol;

import java.io.IOException;

import com.xingcloud.framework.context.stream.input.JSONInputStream;
import com.xingcloud.framework.integration.http.stream.input.HTTPInputStream;
import com.xingcloud.framework.service.request.ServiceRequest;

/**
 * 状态协议服务类
 * 
 */
public class StatusServiceProtocol extends RESTServiceProtocol{
	public static final String NAME = "STATUS";

	public String getName(){
		return NAME;
	}
	/**
	 * 获取请求
	 */
	public ServiceRequest getRequest() throws IOException{
		HTTPInputStream is = new HTTPInputStream(this.httpServletRequest);
		isRequestGZipped = is.isUnGZipRequired();
		ServiceRequest serviceRequest = new ServiceRequest(new JSONInputStream(is));
		serviceRequest.setService("status");
		
		//根据uri判断是否使用admin模块的status接口
		if(getUri().equalsIgnoreCase("/status/admin"))
			serviceRequest.setMethod("admin");
		else serviceRequest.setMethod("status");
		
		serviceRequest.setParameters(serviceRequest.getInputStream().getParameters());
		return serviceRequest;
	}
}