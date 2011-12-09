package com.xingcloud.framework.context.stream.input;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xingcloud.framework.security.Authentication;
import com.xingcloud.util.json.JSONUtil;

/**
 * 分析json格式传入的请求的输入类
 *
 */
public class JSONInputStream extends InputStreamWrapper implements Authentication{
	public static final Logger LOGGER = Logger.getLogger(JSONInputStream.class);
	
	public JSONInputStream(InputStream<Object> inputStream) {
		super(inputStream);
	}

	/**
	 * 将请求内容转成json格式进行分析，获得参数。
	 */
	public Map<String, Object> getParameters() throws IOException{
		Map<String, Object> parameters = super.getParameters();
		String rawData = getRawData();
		if(!JSONUtil.mayBeJSON(rawData)){
			return parameters;
		}
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) JSONUtil.toMap(rawData);
			if(parameters != null
				&& !parameters.isEmpty()){
				map.putAll(parameters);
			}
			return map;
		} catch (Exception e){
			LOGGER.error(e.getMessage(), e);
			return parameters;
		}
	}
}