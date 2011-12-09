package com.xingcloud.framework.context.response;

import java.io.Serializable;

import com.xingcloud.framework.context.result.Result;
import com.xingcloud.framework.context.stream.output.OutputStream;

/**
 * 响应接口
 */
public interface Response<T extends Serializable> {
	Result<T> getResult();
	void output() throws Exception;
	void setResult(Result<T> result);
	void close();
	OutputStream<Serializable> getOutputStream();
}