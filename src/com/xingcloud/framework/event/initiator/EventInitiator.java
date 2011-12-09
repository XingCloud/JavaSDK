package com.xingcloud.framework.event.initiator;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;

import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.event.XingCloudEventPublisher;
import com.xingcloud.framework.initiator.Initiator;
import com.xingcloud.framework.initiator.annotation.CloudInitiator;

/**
 * 事件的初始化类，初始化行云的事件的机制
 * 
 */
@CloudInitiator("eventInitiator")
public class EventInitiator implements Initiator{
	/**
	 * 事件初始化
	 */
	public void init() throws Exception {
		XingCloudEventPublisher eventPublisher = XingCloudEventPublisher.getInstance();
		
		ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) XingCloudApplication.getInstance().getApplicationContext();
		eventPublisher.setApplicationContext(applicationContext);
		
		SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
		
		ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
		eventMulticaster.setBeanFactory(beanFactory);
		// Register statically specified listeners first.
		for (ApplicationListener<?> listener : ((AbstractApplicationContext) applicationContext).getApplicationListeners()) {
			eventMulticaster.addApplicationListener(listener);
		}
		// Do not initialize FactoryBeans here: We need to leave all regular beans
		// uninitialized to let post-processors apply to them!
		String[] listenerBeanNames = applicationContext.getBeanNamesForType(ApplicationListener.class, true, false);
		for (String lisName : listenerBeanNames) {
			eventMulticaster.addApplicationListenerBean(lisName);
		}
		eventMulticaster.setTaskExecutor((Executor) applicationContext.getBean("asynchronousTaskExecutor"));
		
		eventPublisher.setApplicationEventMulticaster(eventMulticaster);
	}
}