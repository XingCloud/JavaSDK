package com.xingcloud.framework.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ApplicationEventMulticaster;

import com.xingcloud.framework.event.annotation.Asynchronous;

/**
 * 行云事件发布类，用于发布事件
 * 
 */
public class XingCloudEventPublisher implements ApplicationEventPublisher, ApplicationContextAware {
	protected static final XingCloudEventPublisher instance = new XingCloudEventPublisher();
	protected ApplicationContext applicationContext = null;
	protected ApplicationEventMulticaster applicationEventMulticaster = null;

	/**
	 * 获取事件发布类的实例，该类单例模式
	 * 
	 */
	public synchronized static XingCloudEventPublisher getInstance() {
		return instance;
	}

	private XingCloudEventPublisher() {
	}
	
	/**
	 * 获取spring的ApplicationContext
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 设置spring的ApplicationContext
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	/**
	 * 获取异步事件发布者
	 * @return
	 */
	public ApplicationEventMulticaster getApplicationEventMulticaster() {
		return applicationEventMulticaster;
	}

	/**
	 * 设置异步事件发布者
	 * @return
	 */
	public void setApplicationEventMulticaster(ApplicationEventMulticaster applicationEventMulticaster) {
		this.applicationEventMulticaster = applicationEventMulticaster;
	}
	
	/**
	 * 发布事件方法
	 */
	@Override
	public void publishEvent(ApplicationEvent event) {
		//如果没有异步事件发布者或者是同步事件，直接发布
		if(applicationEventMulticaster == null
			|| !event.getClass().isAnnotationPresent(Asynchronous.class)){
			applicationContext.publishEvent(event);
			return;
		}
		//如果是异步事件，则异步发布
		applicationEventMulticaster.multicastEvent(event);
	}
}