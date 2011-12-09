package com.xingcloud.framework.context.application.event;

import org.springframework.context.ApplicationEvent;

/**
 * 前后台框架启动事件
 * @author wanglu
 *
 */
public class XingCloudStartedEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	public XingCloudStartedEvent(Object source) {
		super(source);
	}
}