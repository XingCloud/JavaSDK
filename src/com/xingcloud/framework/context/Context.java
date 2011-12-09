package com.xingcloud.framework.context;

/**
 * 上下文类的基本接口
 * 
 */
public interface Context{
	void start() throws Exception;
	void close();
}