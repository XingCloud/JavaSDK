package com.xingcloud.framework.integration.http.session.memcached;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 
 * 封装request对象，实现分布式session
 *
 */
public class HttpServletRequestWrapper extends
		javax.servlet.http.HttpServletRequestWrapper {

	String sid = "";

	public HttpServletRequestWrapper(String sid, HttpServletRequest req) {
		super(req);
		this.sid = sid;
	}

	/**
	 * 根据sessionId，获取经过缓存的session数据。
	 */
	public HttpSession getSession(boolean create) {
		return new HttpSessionSidWrapper(this.sid, super.getSession(create));
	}

	/**
	 * 根据sessionId，获取经过缓存的session数据。
	 */
	public HttpSession getSession() {
		return new HttpSessionSidWrapper(this.sid, super.getSession());
	}

}
