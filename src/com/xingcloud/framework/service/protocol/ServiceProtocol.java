package com.xingcloud.framework.service.protocol;

import java.io.IOException;

import com.xingcloud.framework.context.session.Session;
import com.xingcloud.framework.security.Authentication;
import com.xingcloud.framework.service.request.ServiceRequest;
import com.xingcloud.framework.service.response.ServiceResponse;

/**
 * 服务协议接口
 * 
 */
public interface ServiceProtocol extends Authentication{
	String getName();
	ServiceRequest getRequest() throws IOException;
	ServiceResponse getResponse();
	Session getSession();
	void close();
}