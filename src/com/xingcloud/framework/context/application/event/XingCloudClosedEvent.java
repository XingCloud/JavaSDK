package com.xingcloud.framework.context.application.event;

import org.springframework.context.ApplicationEvent;

/**
 * 前后台框架关闭事件
 * @author wanglu
 *
 */
public class XingCloudClosedEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	public XingCloudClosedEvent(Object source) {
		super(source);
	}
}
