package com.xingcloud.framework.bean.initiator;

import com.xingcloud.framework.bean.annotation.CloudBean;
import com.xingcloud.framework.bean.config.BeanClassConfigLoader;
import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.initiator.Initiator;
import com.xingcloud.framework.initiator.annotation.CloudInitiator;


/**
 * 加载CloudBean的初始化类，用于向全局信息中加载bean的信息
 * 
 */
@CloudInitiator("beanInitiator")
public class BeanInitiator implements Initiator{
	/**
	 * 实现了Initiator接口的类都需要实现该方法。
	 * 加载CloudBean的初始化方法，用于向全局信息中加载bean的信息。
	 * 
	 */
	public void init() throws Exception {
		//加载注解为CloudBean的类信息
		BeanClassConfigLoader classConfigLoader = new BeanClassConfigLoader();
		classConfigLoader.setObjects(XingCloudApplication.getInstance().getApplicationContext().getBeansWithAnnotation(CloudBean.class));
		classConfigLoader.load();
	}
}