package com.xingcloud.framework.context.response;

import java.io.Serializable;


/**
 * 需要获取响应的类实现的接口
 *
 */
public interface ResponseAware<T extends Serializable>{
	Response<T> getResponse();
	ResponseAware<T> setResponse(Response<T> response);
}