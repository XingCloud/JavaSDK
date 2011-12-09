package com.xingcloud.framework.integration.http.protocol;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xingcloud.framework.context.session.Session;
import com.xingcloud.framework.integration.http.session.HttpSessionWrapper;
import com.xingcloud.framework.service.protocol.ServiceProtocol;
import com.xingcloud.framework.service.request.ServiceRequest;
import com.xingcloud.framework.service.response.ServiceResponse;

/**
 * HTTP服务协议的虚基类
 * 
 */
public abstract class AbstractHTTPServiceProtocol implements HTTPProtocol, ServiceProtocol{
	protected String uri;
	protected HttpServletRequest httpServletRequest;
	protected HttpServletResponse httpServletResponse;
	protected ServiceRequest request;
	protected ServiceResponse response;
	
	/**
	 * 获取请求的uri
	 */
	public String getUri() {
		return uri;
	}
	/**
	 * 设置请求的uri
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	/**
	 * 获取HttpServletRequest
	 */
	public HttpServletRequest getServletRequest() {
		return httpServletRequest;
	}
	/**
	 * 获取HttpServletResponse
	 */
	public HttpServletResponse getServletResponse() {
		return httpServletResponse;
	}
	/**
	 * 设置HttpServletRequest
	 */
	public HTTPProtocol setServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
		return this;
	}
	/**
	 * 设置HttpServletResponse
	 */
	public HTTPProtocol setServletResponse(HttpServletResponse httpServletResponse) {
		this.httpServletResponse = httpServletResponse;
		return this;
	}
	/**
	 * 关闭HTTP服务协议
	 */
	public void close(){
		httpServletRequest = null;
		httpServletResponse = null;
	}	
	/**
	 * 获得HttpSession
	 */
	public Session getSession(){
		return new HttpSessionWrapper(httpServletRequest.getSession());
	}	
	/**
	 * 获取认证信息
	 */
	public Object getCredentials() throws Exception {
		return getRequest().getInputStream().getCredentials();
	}	
	/**
	 * 获取请求的详细信息
	 */
	public Object getDetails() throws Exception{
		return getRequest().getInputStream().getDetails();
	}
}