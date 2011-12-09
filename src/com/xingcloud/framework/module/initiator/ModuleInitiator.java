package com.xingcloud.framework.module.initiator;

import com.xingcloud.framework.bean.annotation.CloudBean;
import com.xingcloud.framework.initiator.Initiator;
import com.xingcloud.framework.module.config.ModuleFileConfigLoader;

/**
 * 模块读取初始化类
 * 
 */
@CloudBean("moduleInitiator")
public class ModuleInitiator implements Initiator{
	public void init() throws Exception {
		//加载sdk-description.xml中的配置
		ModuleFileConfigLoader fileConfigLoader = new ModuleFileConfigLoader();
		fileConfigLoader.load();
	}
}