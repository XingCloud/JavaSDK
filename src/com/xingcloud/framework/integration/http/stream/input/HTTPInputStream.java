package com.xingcloud.framework.integration.http.stream.input;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.InflaterInputStream;

import javax.servlet.http.HttpServletRequest;

import com.xingcloud.framework.context.stream.input.ResultInputStream;
import com.xingcloud.util.string.Charset;

/**
 * HTTP输入流类
 * 
 */
public class HTTPInputStream implements ResultInputStream<Object>{
	protected HttpServletRequest httpServletRequest;
	protected String rawData;
	protected Map<String, Object> parameters;
	
	public HTTPInputStream(HttpServletRequest httpServletRequest){
		this.httpServletRequest = httpServletRequest;
	}
	
	/**
	 * 获取请求所附的参数
	 */
	public Map<String, Object> getParameters() throws IOException{
		getRawData();
		if(parameters != null){
			return parameters;
		}
		parameters = new HashMap<String, Object>();
		String query = httpServletRequest.getQueryString();
		if(query == null
			|| query.isEmpty()){
			return parameters;
		}
		String[] keyAndValues = query.split("&");
		for(String keyAndValue : keyAndValues){
			String[] splits = keyAndValue.split("=");
			if(splits == null
				|| splits.length < 2){
				continue;
			}
			parameters.put(splits[0].trim(), splits[1].trim());
		}
		return parameters;
	}
	
	/**
	 * 获取请求数据。有可能需要对请求的数据进行解压缩。
	 */
	public String getRawData() throws IOException{
		if(rawData != null){
			return rawData;
		}
		InputStream is = null;
		if(isUnGZipRequired())
			is = new InflaterInputStream(httpServletRequest.getInputStream());
		else is = httpServletRequest.getInputStream();
		
        List<Integer> blst = new ArrayList<Integer>();
        int b;
        while((b = is.read()) != -1)
        	blst.add(b);
        byte[] bytes = new byte[blst.size()];
        for(int i=0;i<bytes.length;i++)
        	bytes[i] = blst.get(i).byteValue();
        
        rawData = new String(bytes, Charset.UTF8);
        return rawData;
	}
	
	/**
	 * 检查当前请求的数据是否需要解压
	 * @return
	 */
	public boolean isUnGZipRequired(){
		return httpServletRequest.getHeader("Content-Encoding") != null
			&& httpServletRequest.getHeader("Content-Encoding").equals("gzip");
	}
	
	public void close(){
		httpServletRequest = null;
	}
	
	/**
	 * 获取认证信息
	 */
	public Object getCredentials() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		for(Entry<String, Object> entry : getParameters().entrySet()){
			map.put(entry.getKey(), URLDecoder.decode(entry.getValue().toString(), Charset.UTF8));
		}
		String authorization = httpServletRequest.getHeader("Authorization");
		if(authorization == null
			|| authorization.isEmpty()){
			return map;
		}
		Matcher matcher = Pattern.compile("([\\w_]+)\\s*=[\\s\'\"]*([^,\\s\'\"]+)[,\\s\'\"]*").matcher(authorization);
		while(matcher.find()){
			String group = matcher.group();
			map.put(group.replaceFirst("\\s*=[\\s\'\"]*([^,\\s\'\"])+[,\\s\'\"]*$", ""), 
				group.replaceFirst("^[\\w_]+\\s*=[\\s\'\"]*", "").replaceFirst("[,\\s\'\"]*$", ""));
		}
		return map;
	}
	
	public Object getDetails() throws Exception{
		return getRawData();
	}
}