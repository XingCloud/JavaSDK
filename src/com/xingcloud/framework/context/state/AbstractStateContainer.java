package com.xingcloud.framework.context.state;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 状态容器虚基类，提供基本的获取的设置参数的方法
 * 
 */
public abstract class AbstractStateContainer implements StateContainer{
	protected Map<String, Object> parameters = new ConcurrentHashMap<String, Object>();
	
	public boolean hasParameter(String key) {
		return parameters.containsKey(key);
	}
	
	public Object getParameter(String key) {
		if (parameters.containsKey(key)) {
			return parameters.get(key);
		}
		return null;
	}
	
	public Integer getInteger(String key){
		Object parameter = getParameter(key);
		if(parameter == null) return null;
		if(Integer.class.isInstance(parameter))
			return (Integer) parameter;
		return Integer.parseInt(parameter.toString());
	}
	
	public Long getLong(String key){
		Object parameter = getParameter(key);
		if(parameter == null) return null;
		if(Long.class.isInstance(parameter))
			return (Long) parameter;
		return Long.parseLong(parameter.toString());
	}
	
	public Boolean getBoolean(String key){
		Object parameter = getParameter(key);
		if(parameter == null) return null;
		if(Boolean.class.isInstance(parameter))
			return (Boolean) parameter;
		return Boolean.parseBoolean(parameter.toString());
	}
	
	public String getString(String key){
		Object parameter = getParameter(key);
		return parameter == null ? null : parameter.toString();
	}

	public Map<String, Object> getParameters(){
		return parameters;
	}
	
	public StateContainer removeParameter(String key){
		parameters.remove(key);
		return this;
	}
	
	public StateContainer setParameter(String key, Object value){
		parameters.put(key, value);
		return this;
	}
	
	public StateContainer setParameters(Map<String, Object> parameters){
		this.parameters = parameters;
		return this;
	}
	
	public void close(){
		parameters.clear();
		parameters = null;
	}
}