package com.xingcloud.framework.config;

import com.xingcloud.framework.context.application.XingCloudApplication;

public class AppConfigFactory {

	private static AppConfig myAppConfig = null;
	
	public static AppConfig getAppConfig(){
		if(myAppConfig == null)
			myAppConfig = new AppConfig(XingCloudApplication.getInstance().getParameters());
		return myAppConfig;
	}
	
}
