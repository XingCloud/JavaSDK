package com.xingcloud.framework.context.stream.output;

import java.io.IOException;
import java.io.Serializable;

import com.xingcloud.framework.context.result.Result;

/**
 * 输出流的包装类，提供输出前的处理结果的基本方法
 * 
 */
public class OutputStreamWrapper implements ResultOutputStream<Serializable>{
	protected OutputStream<Serializable> outputStream;
	
	public OutputStreamWrapper(OutputStream<Serializable> outputStream) {
		this.outputStream = outputStream;
	}
	
	public void output(Result<Serializable> result) throws Exception{
		if(this.outputStream instanceof ResultOutputStream<?>){
			((ResultOutputStream<Serializable>) this.outputStream).output(result);
		}
	}

	public void output(String rawData) throws IOException {
		this.outputStream.output(rawData);
	}
	
	public void close(){
		if(this.outputStream != null){
			this.outputStream.close();
		}
		this.outputStream = null;
	}
}