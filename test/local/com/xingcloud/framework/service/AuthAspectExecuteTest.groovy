package com.xingcloud.framework.service;

import static org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.xingcloud.framework.context.application.XingCloudApplication
import com.xingcloud.framework.security.AuthenticationException
import com.xingcloud.framework.service.AuthAspect
import com.xingcloud.framework.service.ServiceContext
import com.xingcloud.framework.service.request.ServiceRequest
import com.xingcloud.framework.service.test.TestServiceImpl
import com.xingcloud.test.AbstractTest
import com.xingcloud.framework.integration.http.protocol.RESTServiceProtocol;
import com.xingcloud.framework.service.status.StatusServiceImpl


class AuthAspectExecuteTest extends AbstractTest{

	def authAspect , request , service , method
	@Before
	void setUp() throws Exception {
		super.setUp()
		application.setParameter 'security.service.provider', 'com.xingcloud.framework.service.AuthenticationProviderTestClass'
		authAspect = [checkProvider:{true}] as AuthAspect
		def context = new ServiceContext()
		context.serviceProtocol = new RESTServiceProtocol()
		service = new TestServiceImpl()
		request = new ServiceRequest();
		request.setContext(context)
		def methods = service.getClass().getMethods()
		for(def md : methods) {
			if(md.getName() == 'test') {
				method = md
			}
		}
	}

	//模拟安全认证，执行AuthenticationProviserTestClass的authentication()方法自动抛出AuthenticationException异常
	//message为'This is a AuthenticationProviser Test.'
	@Test
	public void testExecute1() {
		try{
			authAspect.checkAuthentication(service, method, request)
			fail ''
		}catch(AuthenticationException e){
			assertEquals 'This is a AuthenticationProviser Test.', e.getMessage()
		}
	}

	//target为空直接返回
	@Test
	public void testExecute2() {
		try{
			authAspect.checkAuthentication(null, method, request)
		}catch(Exception e){
			fail(e.toString())
		}
	}

	//method为空直接返回
	@Test
	public void testExecute3() {
		try{
			authAspect.checkAuthentication(service, null, request)
		}catch(Exception e){
			fail(e.toString())
		}
	}

	//method没有anth注解直接返回
	@Test
	public void testExecute4() {
		try{
			def methods = StatusServiceImpl.class.getMethods()
			for(def md : methods) {
				if(md.getName() == 'status') {
					method = md
				}
			}
			authAspect.checkAuthentication(service, method, request)
		}catch(Exception e){
			fail(e.toString())
		}
	}

	@After
	void tearDown(){
		super.tearDown()
	}
}
