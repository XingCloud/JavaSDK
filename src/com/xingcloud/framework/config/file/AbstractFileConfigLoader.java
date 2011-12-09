package com.xingcloud.framework.config.file;

import java.io.InputStream;

/**
 * 配置文件载入的抽象类
 * @author tianwei
 */
public abstract class AbstractFileConfigLoader implements FileConfigLoader{
	protected String filePath;
	protected InputStream stream;
	/**
	 * 获取文件路径
	 */
	public String getFilePath(){
		return filePath;
	}
	/**
	 * 设置文件路径
	 */
	public void setFilePath(String filePath){
		this.filePath = filePath;
	}
	/**
	 * 获取文件流
	 */
	public InputStream getStream(){
		return stream;
	}
	/**
	 * 设置文件流
	 */
	public void setStream(InputStream stream){
		this.stream = stream;
	}
	/**
	 * 实现该虚基类需要实现的方法
	 * <p>该方法具体定义了如何加载配置文件</p>
	 */
	public abstract void load() throws Exception;
}