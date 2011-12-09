package com.xingcloud.framework.integration.http.session.memcached;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基于memcached实现分布式session。
 *
 * 建议在web.xml中采用如下配置信息
 * <filter>
 *     <filter-name>memcached-session</filter-name>
 *     <filter-class>com.xingcloud.framework.integration.http.session.memcached.MemcachedSessionFilter</filter-class>
 *     <init-param>
 *         <param-name>sessionId</param-name>
 *         <param-value>xingcloud-sid</param-value>
 *     </init-param>
 *     <init-param>
 *         <param-name>cookieDomain</param-name>
 *         <param-value></param-value>
 *     </init-param>
 * </filter>
 */
public class MemcachedSessionFilter implements Filter {

	private String sessionId = "xingcloud-sid";
	private String cookieDomain = "";
	private String cookiePath = "/";
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		Cookie[] cookies = request.getCookies();
		String sid = null;
		if (cookies != null && cookies.length > 0) {
			for(Cookie c : cookies){
				if(c.getName() != null && c.getName().equals(sessionId))
					sid = c.getValue();
			}
		}
		
		if (sid == null || sid.length() == 0) {
			sid = java.util.UUID.randomUUID().toString();
			
			Cookie mycookies = new Cookie(sessionId, sid);
			mycookies.setMaxAge(-1);
			if (this.cookieDomain != null && this.cookieDomain.length() > 0) {
				mycookies.setDomain(this.cookieDomain);
			}
			mycookies.setPath(this.cookiePath);
			response.addCookie(mycookies);
		}
		filterChain.doFilter(new HttpServletRequestWrapper(sid, request), servletResponse);
	}

	/**
	 * 初始化filter，从配置信息中获取sessionId、cookieDomain和cookiePath参数
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		if(filterConfig.getInitParameter("sessionId") != null)
			this.sessionId = filterConfig.getInitParameter("sessionId");
		if(filterConfig.getInitParameter("cookieDomain") != null)
			this.cookieDomain = filterConfig.getInitParameter("cookieDomain");
		
		this.cookiePath = filterConfig.getInitParameter("cookiePath");
		if (this.cookiePath == null || this.cookiePath.length() == 0)
			this.cookiePath = "/";
	}

}
