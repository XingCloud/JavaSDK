package com.xingcloud.framework.integration.http.protocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xingcloud.framework.integration.http.stream.AMFStream;
import com.xingcloud.framework.service.request.ServiceRequest;
import com.xingcloud.framework.service.response.ServiceResponse;

import flex.messaging.io.amf.ActionMessage;
import flex.messaging.messages.CommandMessage;
import flex.messaging.messages.Message;
import flex.messaging.messages.RemotingMessage;

/**
 * AMF协议，对获得的request和response进行分类处理
 *
 * @author hsx
 */
public class AMFServiceProtocol extends AbstractHTTPServiceProtocol{
	public static final String NAME = "AMF";
	protected AMFStream stream;
	
	public String getName(){
		return NAME;
	}
	
	/**
	 * AMF协议的request，对不同的请求设置不用的服务调用
	 * @exception IOException
	 * @author hsx
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ServiceRequest getRequest() throws IOException{
		if(request != null){
			return request;
		}
		stream = new AMFStream(httpServletRequest, httpServletResponse);
		request = new ServiceRequest(stream);
		request.setAddress(httpServletRequest.getRemoteAddr());
		ActionMessage message = stream.getMessage();
		if(message.getBodyCount() == 0){
			return request;
		}
		Object object = message.getBody(0).getData();
		if(object == null
			|| !Object[].class.isAssignableFrom(object.getClass())
			|| ((Object[]) object).length == 0){
			return request;
		}
		object = ((Object[]) object)[0];
		if(!Message.class.isAssignableFrom(object.getClass())){
			return request;
		}
		if(RemotingMessage.class.isAssignableFrom(object.getClass())){
			RemotingMessage remotingMessage = (RemotingMessage) object;
			String operation = remotingMessage.getOperation();
			//如果请求是getService则调用discovery的getservice
			if(operation.equalsIgnoreCase("getServices")){
				request.setService("amf.discovery");
				request.setMethod("getServices");
				
			}else{
				//如果有传入参数的将参数都放入data中，传入参数都是map类型
				if(remotingMessage.getParameters().get(0)!=null&&Map.class.isAssignableFrom(remotingMessage.getParameters().get(0).getClass())){
					Map<String, Object> temp = (Map<String, Object>)(remotingMessage.getParameters().get(0));
					request.setParameters(temp);
					if(Object[].class.isAssignableFrom(temp.get("data").getClass())){
						Object[] dataObject = (Object[])temp.get("data");
						List<Map<?,?>> datalist = new ArrayList<Map<?,?>>();
						for(int i=0;i<dataObject.length;i++)
						{
							datalist.add((Map)dataObject[i]);
						}
						request.setParameter("data", datalist);
					}
					
				}
				//对服务的描述
				if(remotingMessage.getSource().equals("amfphp.DiscoveryService")){
					request.setService("amf.discovery");
					request.setMethod("describeService");
				}
				//对一般服务的调用
				else{
					request.setService(remotingMessage.getSource());
					request.setMethod(operation);
				}		
			}			
			return request;
		}else if(CommandMessage.class.isAssignableFrom(object.getClass())){
			return request;
		}
		return request;
	}
	
	/**
	 *  获得AMF协议的响应
	 */
	public ServiceResponse getResponse(){
		if(response != null){
			return response;
		}
		response = new ServiceResponse(stream);
		return response;
	}
}