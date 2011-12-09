package com.xingcloud.framework.service.test.initiator;

import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.initiator.Initiator;
import com.xingcloud.framework.initiator.annotation.CloudInitiator;
import com.xingcloud.framework.service.test.config.CloudTestClassConfigLoader;
import com.xingcloud.util.annotation.CloudTest;

@CloudInitiator("cloudTestInitiator")
public class CloudTestInitiator implements Initiator{
	/**
	 * 实现了Initiator接口的类都需要实现该方法。
	 * 加载CloudTest的初始化方法，用于向全局信息中加载Test的信息。
	 * 
	 */
	public void init() throws Exception {
		//加载注解为CloudTest的类信息
		CloudTestClassConfigLoader classConfigLoader = new CloudTestClassConfigLoader();
		classConfigLoader.setObjects(XingCloudApplication.getInstance().getApplicationContext().getBeansWithAnnotation(CloudTest.class));
		classConfigLoader.load();
	}
}