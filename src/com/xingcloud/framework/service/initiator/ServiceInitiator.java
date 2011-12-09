package com.xingcloud.framework.service.initiator;

import com.xingcloud.framework.config.clazz.ClassConfigLoader;
import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.initiator.Initiator;
import com.xingcloud.framework.initiator.annotation.CloudInitiator;
import com.xingcloud.framework.service.annotation.CloudService;
import com.xingcloud.framework.service.config.ServiceClassConfigLoader;

/**
 * 服务初始化类，加载标注了CloudService注解的服务，并存入全局信息中
 * 
 */
@CloudInitiator("serviceInitiator")
public class ServiceInitiator implements Initiator{
	public void init() throws Exception {
		//加载注解为CloudService的类信息
		ClassConfigLoader classConfigLoader = new ServiceClassConfigLoader();
		classConfigLoader.setObjects(XingCloudApplication.getInstance().getApplicationContext().getBeansWithAnnotation(CloudService.class));
		classConfigLoader.load();
	}
}