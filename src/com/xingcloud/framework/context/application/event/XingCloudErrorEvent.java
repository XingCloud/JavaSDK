package com.xingcloud.framework.context.application.event;

import org.springframework.context.ApplicationEvent;

/**
 * 前后台框架错误事件（典型错误如启动失败）
 * @author wanglu
 *
 */
public class XingCloudErrorEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	protected Exception exception;
	
	/**
	 * 发布xingcloud加载是出错事件
	 * @param source 行云的application
	 * @param e 行云application加载出错时报的exception
	 */
	public XingCloudErrorEvent(Object source, Exception e) {
		super(source);
		exception = e;
	}

	/**
	 * 获得出错exception
	 * @return exception
	 */
	public Exception getException() {
		return exception;
	}	

}
