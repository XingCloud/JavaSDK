package com.xingcloud.service.security;

import com.xingcloud.framework.context.request.Request;


/**
 * 安全验证服务的接口
 * @author tianwei
 */
public interface AuthService {
	Object doGetToken(Request req) throws Exception;
	Object doAuth(Request req) throws Exception;
}