package com.xingcloud.framework.security;

/**
 * 安全验证接口
 * 
 */
public interface AuthenticationProvider {
	Authentication authenticate(Authentication authentication) throws AuthenticationException;
	Object authenticate(Object authentication) throws AuthenticationException, Exception;
	boolean supports(Authentication authentication);
	boolean supports(Class<? extends Object> authentication);
}