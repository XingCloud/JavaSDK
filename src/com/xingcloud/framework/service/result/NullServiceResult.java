package com.xingcloud.framework.service.result;

import java.io.Serializable;


/**
 * 封装没有服务执行的结果的类
 * @author Admin
 *
 */
public class NullServiceResult implements ServiceResult{

	private static final long serialVersionUID = 1L;

	private static final NullServiceResult instance = new NullServiceResult();

	protected String id = null;
	
	public static final NullServiceResult getInstance(){
		return instance;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Serializable asResult(){
		return null;
	}
}