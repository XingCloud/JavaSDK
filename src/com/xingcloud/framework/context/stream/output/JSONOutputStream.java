package com.xingcloud.framework.context.stream.output;

import java.io.Serializable;

import com.xingcloud.framework.context.result.Result;
import com.xingcloud.util.json.JSONUtil;

/**
 * 以json格式输出的输出类
 * 
 */

public class JSONOutputStream extends OutputStreamWrapper{

	public JSONOutputStream(OutputStream<Serializable> outputStream) {
		super(outputStream);
	}
	
	/**
	 * 向输出流中输出result的json格式数据
	 */
	public void output(Result<Serializable> result) throws Exception{
		output(JSONUtil.toJSON(result.asResult()));
 	}
}