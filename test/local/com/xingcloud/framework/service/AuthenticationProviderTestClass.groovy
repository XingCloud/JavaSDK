package com.xingcloud.framework.service

import com.xingcloud.framework.security.Authentication;
import com.xingcloud.framework.security.AuthenticationException;
import com.xingcloud.framework.security.AuthenticationProvider;

class AuthenticationProviderTestClass implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		throw new AuthenticationException('This is a AuthenticationProviser Test.')
	}

	@Override
	public Object authenticate(Object authentication) throws AuthenticationException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supports(Authentication authentication) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supports(Class<? extends Object> authentication) {
		// TODO Auto-generated method stub
		return false;
	}

}
