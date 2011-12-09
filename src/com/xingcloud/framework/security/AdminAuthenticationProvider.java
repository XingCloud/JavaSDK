package com.xingcloud.framework.security;

/**
 * admin模块用户验证。
 * @author Admin
 *
 */
public class AdminAuthenticationProvider extends AbstractAuthenticationProvider {

	public AdminAuthenticationProvider() {
		super();
		//admin模块使用的本地参数来自auth.xml中的admin部分。
		this.authType = "admin";
	}
}