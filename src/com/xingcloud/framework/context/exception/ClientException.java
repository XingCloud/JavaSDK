package com.xingcloud.framework.context.exception;

/**
 * 客户端错误类（如传入的参数不正确）
 * 
 */
public class ClientException extends Exception{

	private static final long serialVersionUID = 1L;

	public ClientException(String string) {
		super(string);
	}
}