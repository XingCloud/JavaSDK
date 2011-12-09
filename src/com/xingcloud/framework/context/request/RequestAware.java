package com.xingcloud.framework.context.request;

/**
 * 需要获取请求的类实现的接口
 *
 */
public interface RequestAware<P>{
	Request getRequest();
	RequestAware<P> setRequest(Request request);
}