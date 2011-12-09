package com.xingcloud.framework.config.file;

import java.io.InputStream;

import com.xingcloud.framework.config.ConfigLoader;

/**
 * 配置文件载入类接口
 * @author tianwei
 */
public interface FileConfigLoader extends ConfigLoader{
	/**
	 * 获取文件路径
	 */
	String getFilePath();
	/**
	 * 设置文件路径
	 */
	void setFilePath(String filePath);
	/**
	 * 获取文件流
	 */
	InputStream getStream();
	/**
	 * 设置文件流
	 */
	void setStream(InputStream stream);
	/**
	 * 该方法定义了实现该接口的类需要如何加载配置文件
	 */
	void load() throws Exception;
}