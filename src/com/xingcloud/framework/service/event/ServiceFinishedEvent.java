package com.xingcloud.framework.service.event;

import org.springframework.context.ApplicationEvent;

/**
 * 服务完成事件
 * @author wanglu
 *
 */
public class ServiceFinishedEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	public ServiceFinishedEvent(Object source) {
		super(source);
	}
}
