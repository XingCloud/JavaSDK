package com.xingcloud.framework.context.application;

public interface ApplicationAware{
	Application getApplication();
	ApplicationAware setApplication(Application context);
}