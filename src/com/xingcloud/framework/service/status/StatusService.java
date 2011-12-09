package com.xingcloud.framework.service.status;

import com.xingcloud.framework.context.request.Request;

/**
 * status服务接口
 * 
 */
public interface StatusService {
	Object status(Request req) throws Exception;
	Object admin(Request req) throws Exception;
}