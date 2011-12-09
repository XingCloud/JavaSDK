package com.xingcloud.framework.context.exception;

/**
 * 服务端错误类（如后台逻辑执行错误）
 * 
 */
public class ServerException extends Exception{

	private static final long serialVersionUID = 1L;

	public ServerException(String string) {
		super(string);
	}
}