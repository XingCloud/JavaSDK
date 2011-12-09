package com.xingcloud.framework.service.result;

import java.io.Serializable;

import com.xingcloud.framework.context.result.Result;

/**
 * 服务结果封装类的基本接口
 * 
 *
 */
public interface ServiceResult extends Result<Serializable>{
	String getId();
	void setId(String id);
}