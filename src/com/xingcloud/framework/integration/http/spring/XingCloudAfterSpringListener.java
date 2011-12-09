package com.xingcloud.framework.integration.http.spring;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.xingcloud.framework.context.application.XingCloudApplication;

/**
 * spring加载完成的监听类，用于处理spring加载后的后续处理工作
 * 
 */
public class XingCloudAfterSpringListener implements ServletContextListener {
	/**
	 * 执行spring加载后的后续处理工作
	 */
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		try {			
			//获得Spring ApplicationContext
			ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());

			//启动application实例
			XingCloudApplication application = XingCloudApplication.getInstance();
			application.setApplicationContext(applicationContext);
			application.start();
			if(!application.hasParameter("security.service.provider")){
				application.setParameter("security.service.provider", "com.xingcloud.framework.security.OAuthAuthenticationProvider");
			}
		} catch (Exception e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		XingCloudApplication.getInstance().close();
	}
}