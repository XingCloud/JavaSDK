package com.xingcloud.framework.integration.http.session.memcached;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.xingcloud.framework.integration.http.session.SessionService;

/**
 * 
 * 封装session对象，实现分布式session
 *
 */
public class HttpSessionSidWrapper extends HttpSessionImpl {

	private String sid = "";

	private Map<String, Object> map = null;

	public HttpSessionSidWrapper(String sid, HttpSession session) {
		super(session);
		this.sid = sid;
		this.map = SessionService.getInstance().getSession(sid);
	}

	/**
	 * 根据缓存获取session数据。
	 */
	public Object getAttribute(String arg0) {
		return this.map.get(arg0);
	}

	/**
	 * 根据缓存获取session数据。
	 */
	@SuppressWarnings("rawtypes")
	public Enumeration getAttributeNames() {
		return (new Enumerator(this.map.keySet(), true));
	}

	/**
	 * 清除session数据，并更新到缓存中。
	 */
	public void invalidate() {
		this.map.clear();
		SessionService.getInstance().removeSession(this.sid);
	}

	/**
	 * 删除session数据，并更新到缓存中。
	 */
	public void removeAttribute(String arg0) {
		this.map.remove(arg0);
		SessionService.getInstance().saveSession(this.sid, this.map);
	}

	/**
	 * 设置session数据，并保存到缓存中。
	 */
	public void setAttribute(String arg0, Object arg1) {
		this.map.put(arg0, arg1);
		SessionService.getInstance().saveSession(this.sid, this.map);
	}

}
