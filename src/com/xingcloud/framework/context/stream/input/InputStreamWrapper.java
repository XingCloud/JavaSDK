package com.xingcloud.framework.context.stream.input;

import java.io.IOException;
import java.util.Map;

/**
 * 输入流的包装类，提供从输入流获取基本信息的方法
 *
 */
public class InputStreamWrapper implements ResultInputStream<Object>{
	protected InputStream<Object> inputStream;
	
	public InputStreamWrapper(InputStream<Object> inputStream){
		this.inputStream = inputStream;
	}
	
	public Map<String, Object> getParameters() throws IOException{
		if(ResultInputStream.class.isAssignableFrom(inputStream.getClass())){
			return ((ResultInputStream<Object>) inputStream).getParameters();
		}
		return null;
	}
	
	public String getRawData() throws IOException{
		return inputStream.getRawData();
	}
	
	public void close(){
		inputStream.close();
		inputStream = null;
	}

	public Object getCredentials() throws Exception {
		if(ResultInputStream.class.isAssignableFrom(inputStream.getClass())){
			return ((ResultInputStream<Object>) inputStream).getCredentials();
		}
		return null;
	}

	public Object getDetails() throws Exception {
		if(ResultInputStream.class.isAssignableFrom(inputStream.getClass())){
			return ((ResultInputStream<Object>) inputStream).getDetails();
		}
		return null;
	}
}