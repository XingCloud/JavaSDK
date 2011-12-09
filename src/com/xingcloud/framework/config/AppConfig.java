package com.xingcloud.framework.config;

import java.util.Map;

public class AppConfig {
	
	protected Map<String, Object> parameters;
	
	public AppConfig(Map<String, Object> parameters) {
		super();
		this.parameters = parameters;
	}

	public Object get(String parameter){
		if(hasParameter(parameter)){
			return parameters.get(parameter);
		}
		return null;
	}
	
	public boolean hasParameter(String key) {
		return parameters.containsKey(key);
	}
	
	public Integer getInteger(String key){
		Object parameter = get(key);
		if(parameter == null) return null;
		if(Integer.class.isInstance(parameter))
			return (Integer) parameter;
		return Integer.parseInt(parameter.toString());
	}
	
	public Long getLong(String key){
		Object parameter = get(key);
		if(parameter == null) return null;
		if(Long.class.isInstance(parameter))
			return (Long) parameter;
		return Long.parseLong(parameter.toString());
	}
	
	public Boolean getBoolean(String key){
		Object parameter = get(key);
		if(parameter == null) return null;
		if(Boolean.class.isInstance(parameter))
			return (Boolean) parameter;
		return Boolean.parseBoolean(parameter.toString());
	}
	
	public String getString(String key){
		Object parameter = get(key);
		return parameter == null ? null : parameter.toString();
	}
}
