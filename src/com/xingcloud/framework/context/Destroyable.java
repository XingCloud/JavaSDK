package com.xingcloud.framework.context;

/**
 * 析构接口
 * 
 */
public interface Destroyable {
	/**
	 * 析构方法，在方法中实现该类的资源的释放
	 */
	void destroy();
}
