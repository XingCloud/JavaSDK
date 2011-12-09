package com.xingcloud.framework.context.stream.output;

import java.io.Serializable;

import com.xingcloud.framework.context.result.Result;

public interface ResultOutputStream<T extends Serializable> extends OutputStream<T>{
	void output(Result<T> result) throws Exception;
}