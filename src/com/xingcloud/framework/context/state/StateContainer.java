package com.xingcloud.framework.context.state;

import java.util.Map;

public interface StateContainer{
	boolean hasParameter(String key);
	Object getParameter(String key);
	
	Map<String, Object> getParameters();
	StateContainer removeParameter(String key);
	StateContainer setParameter(String key, Object value);
	StateContainer setParameters(Map<String, Object> map);
	void close();
}