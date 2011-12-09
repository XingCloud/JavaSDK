package com.xingcloud.framework.service.exception;


/**
 * 用于标识服务运行中出现异常时返回的异常信息的异常类
 * 
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 501标识用户已经存在
	 */
	public static final int ACCOUNT_ALREADY_EXIST = 501;
	/**
	 * 502标识用户不存在
	 */
	public static final int ACCOUNT_NOT_EXIST = 502;
	/**
	 * 503标识密码不正确
	 */
	public static final int INCCORECT_PASSWORD = 503;
	/**
	 * 504标识平台用户已经存在
	 */
	public static final int PLATFORM_USER_ALREADY_EXIST = 504;
	/**
	 * 401标识服务方法未找到
	 */
	public static final int SERVICE_METHOD_NOT_FOUND = 401;
	/**
	 * 402标识服务方法出现重复
	 */
	public static final int SERVICE_METHOD_DUPLICATE = 402;

	private int code;

	public ServiceException(int code) {
		super("");
		this.code = code;
	}

	public ServiceException(int code, String message) {
		super(message);
		this.code = code;
	}
	
	public ServiceException(String message) {
		super(message);
		this.code = 400;
	}

	/**
	 * 取得异常的编号
	 */
	public int getCode() {
		return code;
	}

	/**
	 * 设置异常的编号
	 * @param code
	 */
	public void setCode(int code) {
		this.code = code;
	}

}
