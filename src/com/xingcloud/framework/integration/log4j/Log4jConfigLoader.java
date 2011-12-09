package com.xingcloud.framework.integration.log4j;

import org.apache.log4j.PropertyConfigurator;

import com.xingcloud.framework.config.AbstractConfigLoader;
import com.xingcloud.framework.context.application.XingCloudApplication;

/**
 * Log4j配置文件读取类
 * 
 */
public class Log4jConfigLoader extends AbstractConfigLoader{
	public void load() throws Exception{
		//将log4j.path的目录设置为XingCloudApplication.getInstance().getBasePath()，并设置为系统属性
		System.setProperty("log4j.path", XingCloudApplication.getInstance().getBasePath());
		//从给定的文件目录中读取log4j的配置
		PropertyConfigurator.configure(getFilePath());
	}
}