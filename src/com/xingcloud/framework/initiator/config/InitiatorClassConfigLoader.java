package com.xingcloud.framework.initiator.config;

import com.xingcloud.framework.bean.BeanFactory;
import com.xingcloud.framework.config.clazz.AbstractClassConfigLoader;
import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.initiator.Initiator;
import com.xingcloud.framework.initiator.annotation.CloudInitiator;
import com.xingcloud.util.string.StringUtil;

public class InitiatorClassConfigLoader extends AbstractClassConfigLoader {

	@Override
	public void load() throws Exception {
		if (objects == null || objects.isEmpty()) {
			return;
		}
		// 取得XingCloudApplication单例，该类含有全局配置信息
		XingCloudApplication application = XingCloudApplication.getInstance();
		Class<?> clazz;
		CloudInitiator annotation;
		for (Object object : objects.values()) {
			clazz = object.getClass();
			annotation = clazz.getAnnotation(CloudInitiator.class);
			String initiatorName = null;
			if (annotation != null) {
				initiatorName = annotation.value();
			}
			if (initiatorName == null || initiatorName.length() == 0) {
				initiatorName = clazz.getSimpleName();
			}
			initiatorName = StringUtil.lowerCaseFirst(initiatorName);
			application.addInitiator((Initiator)BeanFactory.getInstance().getBean(initiatorName));
		}
	}

}
