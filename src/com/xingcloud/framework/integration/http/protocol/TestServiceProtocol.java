package com.xingcloud.framework.integration.http.protocol;

import java.io.IOException;

import com.xingcloud.framework.context.stream.input.JSONInputStream;
import com.xingcloud.framework.integration.http.stream.input.HTTPInputStream;
import com.xingcloud.framework.service.request.ServiceRequest;


/**
 * test服务的协议
 * 
 */
public class TestServiceProtocol extends RESTServiceProtocol{
	
	/**
	 * 获取请求
	 */
	@Override
	public ServiceRequest getRequest() throws IOException {
		//request继承自AbstractHTTPServiceProtocol
		if(request != null){
			return request;
		}
		HTTPInputStream is = new HTTPInputStream(this.httpServletRequest);
		//检测是否GZipz压缩
		isRequestGZipped = is.isUnGZipRequired();
		ServiceRequest serviceRequest = new ServiceRequest(new JSONInputStream(is));			
		//传入参数
		serviceRequest.setParameters(serviceRequest.getInputStream().getParameters());
		//url继承自AbstractHTTPServiceProtocol，由HTTPProtocolFactory设置的uri参数
		//如果uri是空或者null则直接返回
		if(uri==null||uri==""){
			return serviceRequest;			
		}
		else{
			if(!uri.matches("^/\\w[/\\w]{2}[/\\w]*$")){
				//TODO 404
				return null;
			}
			//uri形如"/test/simpletest"的形式
			String[] array = uri.split("/");
			int length = array.length - 1;
			//如果方法设置失败，或者服务名设置失败
			if(length < 2){
				return null;
			}
			//保存服务名
			StringBuffer api = new StringBuffer();
			for(int i=1;i<length;i++){
				api.append(".").append(array[i]);
			}
			//删除第一个"."
			api.deleteCharAt(0);
			//设置服务
			serviceRequest.setService(api.toString());
			//设置方法
			serviceRequest.setMethod(array[length]);
			serviceRequest.setAddress(httpServletRequest.getRemoteAddr());
			return serviceRequest;			
		}
	}

	public static final String NAME = "TEST";

	public String getName(){
		return NAME;
	}
	
	
}
