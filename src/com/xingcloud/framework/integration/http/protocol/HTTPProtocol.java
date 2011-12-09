package com.xingcloud.framework.integration.http.protocol;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HTTP协议接口，提供相应URI，请求和响应的设置和获取功能
 * 
 */
public interface HTTPProtocol{
	void setUri(String uri);
	String getUri();
	HttpServletRequest getServletRequest();
	HttpServletResponse getServletResponse();
	HTTPProtocol setServletRequest(HttpServletRequest httpServletRequest);
	HTTPProtocol setServletResponse(HttpServletResponse httpServletResponse);
	void close();
}