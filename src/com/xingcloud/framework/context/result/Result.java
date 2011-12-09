package com.xingcloud.framework.context.result;

import java.io.Serializable;

/**
 * 后台服务执行结果的基本接口
 */
public interface Result<T extends Serializable> extends Serializable{
	/**
	 * 将执行结果变为可序列化的字符序列的方法
	 */
	Serializable asResult();
}