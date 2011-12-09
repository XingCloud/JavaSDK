package com.xingcloud.framework.security;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.integration.http.protocol.HTTPProtocol;
import com.xingcloud.framework.service.protocol.ServiceProtocol;
import com.xingcloud.util.security.BASE64;
import com.xingcloud.util.security.MD5;
import com.xingcloud.util.security.SHA1;
import com.xingcloud.util.string.Charset;
import com.xingcloud.util.string.URLEncoder;

/**
 * 安全验证的虚基类
 * 
 */
public abstract class AbstractAuthenticationProvider implements AuthenticationProvider{
	
	public static final Logger LOGGER = Logger.getLogger(AbstractAuthenticationProvider.class);
	
	private static final int DEFAULT_EXPIRES = 86400;
	
	protected String authType = "oauth";
	
	private String getSecurityParameter(String param){
		return "security." + authType + "." + param;
	}
	
	public static final String[] OAUTH_PARAMS = new String[] { 
			"oauth_consumer_key", 
			"oauth_nonce", 
			"oauth_signature",
			"oauth_signature_method", 
			"oauth_timestamp",
			"oauth_version",
		};
	
	/**
	 * 进行安全认证。
	 */
	@SuppressWarnings("unchecked")
	public Authentication authenticate(Authentication authentication) throws AuthenticationException{
		if(!this.supports(authentication)){
			return authentication;
		}
		Map<String, Serializable> credentials = null;
		try {
			credentials = (Map<String, Serializable>) authentication.getCredentials();
		} catch (Exception e) {
			throw new AuthenticationException(e.toString());
		}
		if(credentials == null
			|| credentials.isEmpty()){
			throw new AuthenticationException("credentials is null or empty");
		}
		for(String param : OAUTH_PARAMS){
			//检查是否有认证参数丢失
			if(!credentials.containsKey(param)){
				throw new AuthenticationException("oauth param " + param + " is missing");
			}
		}
		
		//检查请求是否超时
		int expires = XingCloudApplication.getInstance().hasParameter(getSecurityParameter("expires")) 
			? Integer.parseInt((String) XingCloudApplication.getInstance().getParameter(getSecurityParameter("expires"))) 
					: DEFAULT_EXPIRES;
		if(credentials.get("oauth_timestamp") == null || credentials.get("oauth_timestamp").equals("")
				|| Math.abs(new Date().getTime()/1000 - Integer.parseInt((String) credentials.get("oauth_timestamp"))) > expires)
			throw new AuthenticationException("oauth_timestamp expires");
		
		//检查consumer key
		String consumerKey = (String) XingCloudApplication.getInstance().getParameter(getSecurityParameter("consumerKey"));
		if(consumerKey == null
			|| !consumerKey.equals(credentials.get("oauth_consumer_key"))){
			throw new AuthenticationException("oauth_consumer_key is wrong");
		}
		
		//生成base字符串，其中包括认证信息、request参数及其内容
		StringBuffer base = new StringBuffer();
		if(HTTPProtocol.class.isAssignableFrom(authentication.getClass())){
			HttpServletRequest request = ((HTTPProtocol) authentication).getServletRequest();
			try {
				base.append(request.getMethod().toUpperCase())
					.append("&")
					.append(URLEncoder.encode(request.getRequestURL().toString(), Charset.UTF8));
			} catch (UnsupportedEncodingException e) {
				throw new AuthenticationException(e.toString());
			}
		}
		String signature = (String) credentials.get("oauth_signature");
		credentials.remove("oauth_signature");
		SortedSet<String> sortedSet= new TreeSet<String>(credentials.keySet());
		StringBuffer paramString = new StringBuffer();
		for(String key : sortedSet){
			paramString.append(key).append("=").append(credentials.get(key)).append("&");
		}
		if(paramString.length() > 0){
			paramString.deleteCharAt(paramString.length() - 1);
			try {
				base.append("&")
					.append(URLEncoder.encode(paramString.toString(), Charset.UTF8));
			} catch (UnsupportedEncodingException e) {
				throw new AuthenticationException(e.toString());
			}
		}
		Object details;
		try {
			details = authentication.getDetails();
		} catch (Exception e) {
			throw new AuthenticationException(e.toString());
		}
		if(details != null
			&& String.class.isAssignableFrom(details.getClass())
			&& !((String) details).isEmpty()){
			try {
				base.append("&")
					.append(URLEncoder.encode((String) details, Charset.UTF8));
			} catch (UnsupportedEncodingException e) {
				throw new AuthenticationException(e.toString());
			}
		}
		LOGGER.debug("base string: " + base.toString());
		
		String secretKey = (String) XingCloudApplication.getInstance().getParameter(getSecurityParameter("secretKey"));
		if(secretKey == null
			|| secretKey.length() == 0){
			throw new AuthenticationException("secret key is not defined in auth.xml");
		}
		//根据base生成signature
		String serverSignature = null;
		String signatureMethod = (String) credentials.get("oauth_signature_method");
		if(signatureMethod.equalsIgnoreCase("HMAC-SHA1")){
			try {
				serverSignature = BASE64.encode(SHA1.encode(base.toString(), secretKey));
			} catch (Exception e) {
				throw new AuthenticationException(e.toString());
			}
		}else if(signatureMethod.equalsIgnoreCase("MD5")){
		    try {
				serverSignature = BASE64.encode(MD5.encode(base.append("&").append(secretKey).toString()));
			} catch (Exception e) {
				throw new AuthenticationException(e.toString());
			}
		}else {
			throw new AuthenticationException("oauth_signature_method is not supported");
		}
		//检查signature
		if(serverSignature == null
			|| !serverSignature.equals(signature)){
			String msg = "signature is not valid!\n" 
				+ "Signature Not Valid: expected " + serverSignature + ", but is " + signature + "\n"
				+ "Base String: " + base + "\n"
				+ "Details: " + details;
			LOGGER.debug(msg);
			throw new AuthenticationException(msg);
		}
		LOGGER.debug("oauth authentication ok");
		return authentication;
	}
	public boolean supports(Authentication authentication){
		return this.supports(authentication.getClass());
	}
	public boolean supports(Class<? extends Object> authentication){
		return ServiceProtocol.class.isAssignableFrom(authentication);
	}
	public Object authenticate(Object authentication)
			throws AuthenticationException, Exception {
		if(!this.supports(authentication.getClass())){
			return authentication;
		}
		return (Authentication) authenticate((Authentication) authentication);
	}
}
