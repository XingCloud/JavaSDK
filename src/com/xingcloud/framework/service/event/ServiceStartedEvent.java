package com.xingcloud.framework.service.event;

import org.springframework.context.ApplicationEvent;

/**
 * 服务启动事件
 * @author wanglu
 *
 */
public class ServiceStartedEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	public ServiceStartedEvent(Object source) {
		super(source);
	}
}
