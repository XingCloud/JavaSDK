package com.xingcloud.framework.security;

import com.xingcloud.framework.context.session.Session;
import com.xingcloud.framework.context.session.SessionAware;

/**
 * 基于session的安全验证类
 * 
 */
public class SessionAuthenticationProvider implements AuthenticationProvider{
	/**
	 * 进行安全验证
	 */
	public Authentication authenticate(Authentication authentication) throws AuthenticationException{
		if(!this.supports(authentication)){
			return authentication;
		}
		return (Authentication) authenticate((Object) authentication);
	}
	/**
	 * 验证是否支持当前的安全验证
	 */
	public boolean supports(Authentication authentication){
		return this.supports(authentication.getClass());
	}
	/**
	 * 验证是否支持当前的安全验证
	 */
	public boolean supports(Class<? extends Object> authentication){
		return SessionAware.class.isAssignableFrom(authentication);
	}
	/**
	 * 进行安全验证
	 */
	public Object authenticate(Object authentication)
			throws AuthenticationException{
		if(!this.supports(authentication.getClass())){
			return authentication;
		}
		SessionAware sessionAware = (SessionAware) authentication;
		Session session = sessionAware.getSession();
		if(session == null
			|| !session.hasParameter("security.oauth.authId")){
			throw new AuthenticationException("the session is not authenticated");
		}
		return authentication;
	}
}