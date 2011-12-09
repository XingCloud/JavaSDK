package com.xingcloud.framework.integration.http.protocol;

import java.io.IOException;

import com.xingcloud.framework.context.stream.input.JSONInputStream;
import com.xingcloud.framework.context.stream.output.JSONOutputStream;
import com.xingcloud.framework.integration.http.stream.input.HTTPInputStream;
import com.xingcloud.framework.integration.http.stream.output.HTTPOutputStream;
import com.xingcloud.framework.service.request.ServiceRequest;
import com.xingcloud.framework.service.response.ServiceResponse;

/**
 * REST服务协议类
 * 
 */
public class RESTServiceProtocol extends AbstractHTTPServiceProtocol{
	public static final String NAME = "REST";
	
	/**
	 * 请求的内容是否经压缩，返回的内容是否须压缩
	 */
	protected boolean isRequestGZipped = false;
	
	public String getName(){
		return NAME;
	}
	
	/**
	 * 获取服务请求
	 */
	public ServiceRequest getRequest() throws IOException{
		if(request != null){
			return request;
		}
		if(!uri.matches("^/\\w[/\\w]{2}[/\\w]*$")){
			//TODO 404
			return null;
		}
		//获取请求的API
		String[] array = uri.split("/");
		int length = array.length - 1;
		if(length < 3){
			//TODO 404
			return null;
		}
		StringBuffer api = new StringBuffer();
		for(int i=2;i<length;i++){
			api.append(".").append(array[i]);
		}
		api.deleteCharAt(0);
		HTTPInputStream is = new HTTPInputStream(this.httpServletRequest);
		isRequestGZipped = is.isUnGZipRequired();
		request = new ServiceRequest(new JSONInputStream(is));
		request.setAddress(httpServletRequest.getRemoteAddr());
		request.setService(api.toString());
		request.setMethod(array[length]);
		request.setParameters(request.getInputStream().getParameters());
		return request;
	}
	
	/**
	 * 获取服务响应
	 */
	public ServiceResponse getResponse(){
		if(response != null){
			return response;
		}
		HTTPOutputStream os = new HTTPOutputStream(this.httpServletResponse);
		os.setGZipRequired(isRequestGZipped);
		response = new ServiceResponse(new JSONOutputStream(os));
		return response;
	}
}