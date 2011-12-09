package com.xingcloud.framework.service.test.config;

import java.util.HashMap;
import java.util.Map;

import com.xingcloud.framework.config.clazz.AbstractClassConfigLoader;
import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.util.annotation.CloudTest;
import com.xingcloud.util.string.StringUtil;

public class CloudTestClassConfigLoader extends AbstractClassConfigLoader{

	/**
	 * 从spring扫描到的类中加载添加了CloudTest注解的类到全局信息中
	 * <p>要在程序中得到配置，使用</p>
	 * {@code XingCloudApplication.getInstance().getParameter("Test");}
	 */
	@SuppressWarnings("unchecked")
	public void load(){
		if(objects == null
			|| objects.isEmpty()){
			return;
		}
		//取得XingCloudApplication单例，该类含有全局配置信息
		XingCloudApplication application = XingCloudApplication.getInstance();		
		//bean的初始配置
		if (!application.hasParameter("Test")) {
			application.setParameter("Test",
					new HashMap<String, String>());
		}
		Class<?> clazz;
		CloudTest annotation;
		for(Object object : objects.values()){
			clazz = object.getClass();
			annotation = clazz.getAnnotation(CloudTest.class);
			String testName = null;
			if(annotation != null){
				testName = annotation.value();
			}
			if(testName == null
				|| testName.length() == 0) {
				testName = clazz.getSimpleName();
			}
			testName = StringUtil.lowerCaseFirst(testName);
			((Map<String, String>) XingCloudApplication.getInstance()
					.getParameter("Test")).put(testName, clazz.getName());
		}
	}
}
