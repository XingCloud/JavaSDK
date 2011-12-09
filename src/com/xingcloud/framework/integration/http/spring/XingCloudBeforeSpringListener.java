package com.xingcloud.framework.integration.http.spring;

import java.io.File;
import java.net.URLDecoder;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.integration.spring.BeforeSpringInitiator;
import com.xingcloud.framework.integration.spring.XingCloudBeforeSpringInitiator;
import com.xingcloud.util.string.Charset;

/**
 * spring加载开始前的监听类，用于处理spring加载前的准备工作
 * 
 */
public class XingCloudBeforeSpringListener implements ServletContextListener {
	/**
	 * 执行spring加载前的准备工作
	 */
	public static String BasePath = null;
	
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		try {
			// 获得WEB-INF的绝对目录
			String path = servletContextEvent.getServletContext().getRealPath(File.separator);
			if (path == null || path.length() == 0) {
				File file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toString());
				path = file.getParent().replaceAll("file\\:\\\\", "").replaceAll("[\\/]", File.separator);
				//打包为jar后游戏开发者使用jetty
				if (path.endsWith("lib")) {
					path = path.substring(0, path.length()-3-File.separator.length());
				}
			} else {
				path = path + "/WEB-INF";
			}
			if(BasePath!=null){
				path = BasePath+"/WebContent/Web-INF";
			}
			
			// 设置XingCloudApplication的basePath值为WEB-INF的绝对目录
			XingCloudApplication application = XingCloudApplication.getInstance();
			application.setBasePath(URLDecoder.decode(path, Charset.UTF8));
			// 在Spring之前做初始化配置
			BeforeSpringInitiator beforeSpringInitiator = new XingCloudBeforeSpringInitiator();
			beforeSpringInitiator.setBasePath(application.getBasePath());
			beforeSpringInitiator.init();
		} catch (Exception e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
	}
}