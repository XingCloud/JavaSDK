package com.xingcloud.framework.config.clazz;

import java.util.Map;

/**
 * 类信息载入的抽象类
 * 类信息从Spring容器中调用ListableBeanFactory.getBeansWithAnnotation方法取得
 * {@link org.springframework.beans.factory.ListableBeanFactory#getBeansWithAnnotation}
 * @author tianwei
 */
public abstract class AbstractClassConfigLoader implements ClassConfigLoader{
	protected Map<String, Object> objects;
	
	/**
	 * 获取spring容器扫描到的类
	 */
	public Map<String, Object> getObjects() {
		return objects;
	}

	/**
	 * 为spring扫描到的类提供设置方法
	 */
	public void setObjects(Map<String, Object> objects) {
		this.objects = objects;
	}

	/**
	 * <p>实现该类时需要覆盖该方法，提供具体的处理spring容器中的类的方法</p>
	 */
	public abstract void load() throws Exception;
}