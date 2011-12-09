package com.xingcloud.framework.service.event;

import org.springframework.context.ApplicationEvent;

/**
 * 服务错误事件
 * @author wanglu
 *
 */
public class ServiceErrorEvent extends ApplicationEvent {

	protected Exception exception;
	
	/**
	 * service出错是发布的事件
	 * @param source serviceRequest
	 * @param e 出错是报的exception
	 */
	public ServiceErrorEvent(Object source, Exception e) {
		super(source);
		exception = e;
	}

	/**
	 * 返回错误exception
	 * @return exception
	 */
	public Exception getException() {
		return exception;
	}



	private static final long serialVersionUID = 1L;

}
