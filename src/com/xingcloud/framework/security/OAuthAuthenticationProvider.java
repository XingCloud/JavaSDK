package com.xingcloud.framework.security;
/**
 * 提取用户请求中的安全信息，进行安全认证。
 * @author Admin
 *
 */
public class OAuthAuthenticationProvider extends AbstractAuthenticationProvider{
	public OAuthAuthenticationProvider(){
		super();
		this.authType="oauth";
	}
}