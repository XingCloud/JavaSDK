package com.xingcloud.service.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.context.request.Request;
import com.xingcloud.framework.security.AuthenticationException;
import com.xingcloud.framework.security.AuthenticationProvider;
import com.xingcloud.framework.security.OAuthAuthenticationProvider;
import com.xingcloud.framework.service.ServiceContext;
import com.xingcloud.framework.service.annotation.CloudService;
import com.xingcloud.framework.service.annotation.Description;
import com.xingcloud.framework.service.annotation.Method;
import com.xingcloud.framework.service.annotation.Protocol;
import com.xingcloud.framework.service.annotation.ServiceParam;
import com.xingcloud.framework.service.exception.ServiceException;

/**
 * 安全验证的接口，提供取得token和进行验证的方法
 * @author tianwei
 */
@CloudService(value="authService", api="security.auth")
public class AuthServiceImpl implements AuthService {
	/**
	 * 得到token
	 */
	@Protocol(allow="ANY", deny = "none")
	@Method(allow="ANY", deny = "none")
	@Description(content="安全认证的相关方法")
	@ServiceParam(name = "oauth_consumer_key", type = "string", remark = "#consumer_key#")
	public Object doGetToken(Request req) throws Exception{
		validateConsumerKey(req);
	    Map<String, String> map = new HashMap<String, String>();
	    String token = getRandomString();
	    map.put("oauth_token", token);
	    req.getSession().setParameter("security.oauth.token", token);
		return map;
	}
	
	/**
	 * 进行安全验证
	 */
	@Protocol(allow="ANY", deny = "none")
	@Method(allow="ANY", deny = "none")
	@Description(content="安全认证的相关方法")
	@ServiceParam(name = "oauth_token,oauth_consumer_key", type = "string,string", remark = "根据GetToken生成的值,#consumer_key#")
	public Object doAuth(Request req) throws Exception{
		validateConsumerKey(req);
		validateToken(req);
		AuthenticationProvider provider = new OAuthAuthenticationProvider();
		provider.authenticate(((ServiceContext) req.getContext()).getServiceProtocol());
		req.getSession().setParameter("security.oauth.authId", getRandomString());
		return new HashMap<String, String>();
	}
	
	private void validateConsumerKey(Request req) throws Exception{
		validateParam("oauth_consumer_key", (String) XingCloudApplication.getInstance().getParameter("security.oauth.consumerKey"),req);
	}
	
	private void validateToken(Request req) throws Exception{
		validateParam("oauth_token", (String) req.getSession().getParameter("security.oauth.token"),req);
	}
	
	private void validateParam(String param, String serverParam, Request req) throws Exception{
		if(!req.hasParameter(param)){
			throw new ServiceException("param " + param +" is required");
		}
		if(serverParam == null
			|| !serverParam.equals(req.getParameter(param))){
			throw new AuthenticationException("param " + param +" is wrong");
		}
	}
	
	private String getRandomString(){
		int length = BASE.length();
		StringBuffer token = new StringBuffer();
		Random random = new Random();
		for(int i = 0; i < 16; i ++){
			token.append(BASE.charAt(random.nextInt(length)));
		}
		return token.toString();
	}
	
	public static final String BASE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
}