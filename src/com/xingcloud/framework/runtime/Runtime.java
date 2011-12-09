package com.xingcloud.framework.runtime;

import com.xingcloud.framework.config.AppConfig;
import com.xingcloud.framework.config.AppConfigFactory;

public final class Runtime {

	protected static final Runtime instance = new Runtime();
	private String environment = "cloud";
	private AppConfig appconfig = null;
	
	private Runtime() {
	}

	public synchronized static Runtime getInstance() {
		return instance;
	}

	public String getRuntimeEnvironment() {
		return environment;
	}

	public void setRuntimeEnvironment(String runtimeEnvironment) {
		environment = runtimeEnvironment;
	}

	public AppConfig getAppconfig() {
		if(appconfig==null){
			appconfig = AppConfigFactory.getAppConfig();
		}
		return appconfig;
	}

}
