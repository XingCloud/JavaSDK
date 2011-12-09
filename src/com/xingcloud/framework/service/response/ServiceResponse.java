package com.xingcloud.framework.service.response;

import java.io.Serializable;

import com.xingcloud.framework.context.response.AbstractResponse;
import com.xingcloud.framework.context.stream.output.OutputStream;
import com.xingcloud.framework.context.stream.output.ResultOutputStream;

/**
 * 服务响应封装类
 * 
 *
 */
public class ServiceResponse extends AbstractResponse<Serializable>{
	protected ResultOutputStream<Serializable> outputStream;

	public ServiceResponse(ResultOutputStream<Serializable> outputStream){
		this.outputStream = outputStream;
	}
	
	/**
	 * 输出响应信息
	 */
	public void output() throws Exception{
		if(result != null){
			outputStream.output(result);
		}
	}
	
	/**
	 * 关闭响应，释放响应的资源
	 */
	public void close(){
		outputStream.close();
		super.close();
	}
	
	/**
	 * 获得输出流
	 */
	public OutputStream<Serializable> getOutputStream(){
		return outputStream;
	}
}