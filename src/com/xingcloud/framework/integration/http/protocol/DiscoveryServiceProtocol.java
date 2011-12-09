package com.xingcloud.framework.integration.http.protocol;

import java.io.IOException;

import com.xingcloud.framework.context.stream.input.JSONInputStream;
import com.xingcloud.framework.integration.http.stream.input.HTTPInputStream;
import com.xingcloud.framework.service.request.ServiceRequest;

/**
 * 搜索服务的协议类
 * 
 */
public class DiscoveryServiceProtocol extends RESTServiceProtocol{
	public static final String NAME = "DISCOVERY";

	public String getName(){
		return NAME;
	}
	/**
	 * 获取请求
	 */
	public ServiceRequest getRequest() throws IOException{
		ServiceRequest serviceRequest = new ServiceRequest(new JSONInputStream(new HTTPInputStream(this.httpServletRequest)));
		serviceRequest.setService("discovery");
		serviceRequest.setMethod("getServices");
		serviceRequest.setParameters(serviceRequest.getInputStream().getParameters());
		return serviceRequest;
	}
}