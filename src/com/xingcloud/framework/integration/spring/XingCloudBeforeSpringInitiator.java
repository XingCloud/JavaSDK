package com.xingcloud.framework.integration.spring;

import com.xingcloud.framework.config.file.FileConfigLoader;
import com.xingcloud.framework.initiator.Initiator;
import com.xingcloud.framework.integration.log4j.Log4jConfigLoader;
import com.xingcloud.framework.module.initiator.ModuleInitiator;

/**
 * 启动后台时加载spring前的初始化类
 * 
 */
public class XingCloudBeforeSpringInitiator extends AbstractBeforeSpringInitiator{
	@Override
	public void init() throws Exception {
		//初始化Log4j配置
		FileConfigLoader loader = new Log4jConfigLoader();
		loader.setFilePath(getBasePath() + "/config/log4j.properties");
		loader.load();
		// 加载模块初始化类
		Initiator initiator = new ModuleInitiator();
		initiator.init();
	}
}