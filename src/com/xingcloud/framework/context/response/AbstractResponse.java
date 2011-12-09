package com.xingcloud.framework.context.response;

import java.io.Serializable;

import com.xingcloud.framework.context.result.Result;

/**
 * 响应的虚基类
 */
public abstract class AbstractResponse<T extends Serializable> implements Response<T>{
	protected Result<T> result;

	/**
	 * 获取执行结果
	 */
	public Result<T> getResult(){
		return result;
	}
	
	/**
	 * 输出响应
	 */
	public void output() throws Exception{
		
	}
	
	/**
	 * 设置执行结果
	 */
	public void setResult(Result<T> result){
		this.result = result;
	}
	
	/**
	 * 关闭响应
	 */
	public void close(){
		result = null;
	}
}