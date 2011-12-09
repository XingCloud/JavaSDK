package com.xingcloud.framework.security;


/**
 * 安全验证异常类，封装安全验证是出现的异常
 * 
 */
public class AuthenticationException extends Exception{

	private static final long serialVersionUID = 1L;

	public AuthenticationException(String message){
		super(message);
	}
}
