package com.xingcloud.framework.config.clazz;

import java.util.Map;

import com.xingcloud.framework.config.ConfigLoader;

/**
 * 类信息载入的接口
 * @author tianwei
 */
public interface ClassConfigLoader extends ConfigLoader{
	/**
	 * 获取类信息
	 * @return Map
	 */
	Map<String, Object> getObjects();
	/**
	 * 设置类信息
	 */
	void setObjects(Map<String, Object> objects);
}