package com.xingcloud.framework.integration.http.stream.output;

import java.io.IOException;
import java.io.Serializable;
import java.util.zip.DeflaterOutputStream;

import javax.servlet.http.HttpServletResponse;

import com.xingcloud.framework.context.stream.output.OutputStream;
import com.xingcloud.util.string.Charset;

/**
 * HTTP输出流类
 * 
 */
public class HTTPOutputStream implements OutputStream<Serializable>{
	protected HttpServletResponse httpServletResponse;
	
	public HTTPOutputStream(HttpServletResponse httpServletResponse){
		this.httpServletResponse = httpServletResponse;
	}
	
	boolean GZipRequired = false;
	
	public void setGZipRequired(boolean gZipRequired) {
		GZipRequired = gZipRequired;
	}

	/**
	 * 向客户端作出回复。有可能需要对回复的数据进行压缩。
	 */
	public void output(String rawData) throws IOException{
		java.io.OutputStream os = null;
		if(isGZipRequired()){
			os = new DeflaterOutputStream(httpServletResponse.getOutputStream());
		}
		else os = this.httpServletResponse.getOutputStream();
		
		os.write(rawData.getBytes(Charset.UTF8));
		os.flush();
		os.close();
	}
	
	/**
	 * 设置响应的Header
	 * @param key
	 * @param value
	 */
	public void setHeader(String key, String value){
		this.httpServletResponse.setHeader(key, value);
	}
	
	public void close(){
		this.httpServletResponse = null;
	}
	
	/**
	 * 检查当前请求的数据是否需要压缩
	 * @return
	 */
	public boolean isGZipRequired(){
		return GZipRequired;
	}
}