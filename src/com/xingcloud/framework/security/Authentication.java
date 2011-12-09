package com.xingcloud.framework.security;

/**
 * 安全验证接口
 * 
 */
public interface Authentication {
	Object getCredentials() throws Exception;
	Object getDetails() throws Exception;
}