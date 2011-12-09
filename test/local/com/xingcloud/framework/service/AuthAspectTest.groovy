package com.xingcloud.framework.service;

import static org.junit.Assert.*;

import com.xingcloud.framework.bean.BeanFactory;
import com.xingcloud.framework.service.annotation.Auth;
import com.xingcloud.framework.service.annotation.CloudService;
import com.xingcloud.framework.service.request.ServiceRequest;
import com.xingcloud.framework.service.response.ServiceResponse;
import com.xingcloud.test.AbstractTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

class AuthAspectTest extends AbstractTest {
	
	@Before
	public void setUp() throws Exception {
		super.setUp()
		application.setParameter("security.service.provider", "com.xingcloud.framework.security.OAuthAuthenticationProvider")
		application.setParameter("security.oauth.default", "true")
	}
	@After
	public void tearDown() throws Exception {
		super.tearDown()
		application.removeParameter "security.service.provider"
		application.setParameter("security.oauth.default", "false")
	}
	
	@Test
	public void testCheckProvider() {
		def s = new MyService()
		def m = MyService.getMethod('dosmth')
		
		def sc = (ServiceContext)BeanFactory.getInstance().getBean("serviceContext")
		def req = new ServiceRequest(null)
		req.service = 'myService1'
		try{
			sc.execute s, m, req, new ServiceResponse(null), this.persistenceSession
			fail
		} catch(e){}
	}

}

@CloudService(value = "myService1", api = "api")
class MyService {
	@Auth(type = "oauth")
	def dosmth(){
		println 'method'
	}
}
