package com.xingcloud.framework.config;

/**
 * 配置载入类的基本接口
 * @author tianwei
 */
public interface ConfigLoader{
	/**
	 * 配置载入类的载入方法 
	 * 
	 */
	public void load() throws Exception;
}