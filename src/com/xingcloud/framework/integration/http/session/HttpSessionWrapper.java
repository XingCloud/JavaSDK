package com.xingcloud.framework.integration.http.session;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import com.xingcloud.framework.context.session.Session;
import com.xingcloud.framework.context.state.StateContainer;

/**
 * http session封装类，提供一些基本的获取session参数的方法
 * 
 */
public class HttpSessionWrapper implements Session{
	protected HttpSession session;
	/**
	 * 新建一个封装类
	 * @param session
	 */
	public HttpSessionWrapper(HttpSession session){
		this.session = session;
	}
	/**
	 * 关闭这个session
	 */
	public void close(){
		session.invalidate();
	}
	
	public HttpSession getHttpSession(){
		return session;
	}
	/**
	 * 验证session中是否有这个参数
	 */
	public boolean hasParameter(String key) {
		return session.getAttribute(key) != null;
	}
	/**
	 * 取得session中指定值的参数
	 */
	public Serializable getParameter(String key) {
		return (Serializable) session.getAttribute(key);
	}
	/**
	 * 取得session中所有的参数
	 */
	public Map<String, Object> getParameters() {
		Map<String, Object> parameters = new ConcurrentHashMap<String, Object>();
		@SuppressWarnings("unchecked")
		Enumeration<String> attributeNames = session.getAttributeNames();
		while(attributeNames.hasMoreElements()){
			String key = (String)attributeNames.nextElement();
			Serializable value = (Serializable) session.getAttribute(key);
			parameters.put(key, value);
		}
		return parameters;
	}
	
	/**
	 * 删除session中相应的参数
	 */
	public StateContainer removeParameter(String key){
		session.removeAttribute(key);
		return this;
	}
	/**
	 * 设置session中的参数
	 */
	public StateContainer setParameter(String key, Object value){
		session.setAttribute(key, value);
		return this;
	}
	
	/**
	 * 根据传入的参数集设定session中的参数
	 */
	public StateContainer setParameters(Map<String, Object> parameters){
		Iterator<Entry<String, Object>> iterator = parameters.entrySet().iterator(); 
		while (iterator.hasNext()) { 
			Entry<String, Object> entry = iterator.next(); 
		    String key = entry.getKey(); 
		    Object value = entry.getValue(); 
			session.setAttribute(key, value);
		}
		return this;
	}
}