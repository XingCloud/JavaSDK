package com.xingcloud.framework.security;

import static org.junit.Assert.*

import java.net.URLEncoder

import org.junit.Before
import org.junit.Test

import com.xingcloud.test.AbstractTest
import com.xingcloud.util.security.BASE64
import com.xingcloud.util.security.SHA1
import com.xingcloud.util.string.Charset

class OAuthAuthenticationProviderTest extends AbstractTest {

	def auth, provider
	@Before
	public void setUp() throws Exception {
		super.setUp()
		
		def detail = 'a b'
		
		def cred = [oauth_consumer_key:'#consumer_key#',
					oauth_nonce:'a69c20cfd885ac0ca41ffa8681c4675b',
					oauth_signature_method:'HMAC-SHA1',
					oauth_timestamp: "" +(int)(new Date().getTime()/1000),
					oauth_version:1]
		
		def authStr = cred.keySet().sort().collect{"${it}=${cred.get(it)}"}.join('&')
		def base = ['', authStr, detail].collect{URLEncoder.encode(it, Charset.UTF8)}.join('&')
		base = base.replaceAll('\\+', '%20')
		cred.oauth_signature = BASE64.encode(SHA1.encode(base, '#secret_key#'))
		
		auth = [
			servletRequest:[method:'POST'],
			getCredentials:{cred},
			getDetails:{detail}
			] as Authentication
		
		provider = [
			supports:{true}
			] as OAuthAuthenticationProvider
		
	}

	@Test
	public void testAuthenticateAuthentication() {
		try{
			provider.authenticate auth
		} catch(e){
			assertTrue false
		}
	}

}
