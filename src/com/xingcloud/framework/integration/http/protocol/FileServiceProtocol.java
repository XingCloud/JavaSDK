package com.xingcloud.framework.integration.http.protocol;

import com.xingcloud.framework.integration.http.stream.output.FileOutputStream;
import com.xingcloud.framework.service.response.ServiceResponse;

/**
 * 文件服务的协议类
 * 
 */
public class FileServiceProtocol extends RESTServiceProtocol{
	public static final String NAME = "FILE";

	public String getName(){
		return NAME;
	}
	/**
	 * 获取响应
	 */
	public ServiceResponse getResponse() {
		FileOutputStream os = new FileOutputStream(this.httpServletResponse);
		os.setGZipRequired(isRequestGZipped);
		return new ServiceResponse(os);
	}
}