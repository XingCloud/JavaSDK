package com.xingcloud.framework.context;

/**
 * 需要获取context的类需要实现的接口，提供context的获取和设置方法
 * 
 */
public interface ContextAware{
	Context getContext();
	ContextAware setContext(Context context);
}