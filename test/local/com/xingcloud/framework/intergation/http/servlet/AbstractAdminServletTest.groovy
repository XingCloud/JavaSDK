package com.xingcloud.framework.intergation.http.servlet;

import static org.junit.Assert.*

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.junit.After
import org.junit.Before
import org.junit.Test

import com.xingcloud.framework.context.application.XingCloudApplication
import com.xingcloud.framework.integration.http.servlet.AbstractAdminServlet
import com.xingcloud.framework.security.AuthenticationException
import com.xingcloud.test.AbstractTest


class AbstractAdminServletTest extends AbstractTest{

	def request , response , servlet

	@Before
	void setUp() throws Exception {
		super.setUp()
		response = []as HttpServletResponse
		request = []as HttpServletRequest
		servlet = [
					AbstractAdminServlet:{}
				] as AbstractAdminServlet
	}

	//admin中default为false，不认证
	@Test
	void testAuthenticate1() {
		try{
			application.setParameter "security.admin.default", "false"
			servlet.authenticate request, response
		}catch(e){
			fail(e.toString())
		}
	}
	
	//admin中default为FalSE，不认证，false忽略大小写
	@Test
	void authenticatetest1() {
		try{
			application.setParameter "security.admin.default", "FalSE"
			servlet.authenticate request, response
		}catch(e){
			fail(e.toString())
		}
	}

	//admin中default为true，认证
	@Test
	void authenticatetest2() {
		try{
			application.setParameter "security.admin.default", "true"
			servlet.authenticate request, response
			fail ''
		}catch(AuthenticationException e){
		}
	}

	//admin中default没有配置，认证
	@Test
	void authenticatetest3() {
		try{
			application.removeParameter 'security.oauth.default'
			servlet.authenticate request, response
			fail ''
		}catch(AuthenticationException e){
		}
	}

	//admin中default配置错误，认证
	@Test
	void authenticatetest4() {
		try{
			application.setParameter "security.admin.default", "default value error"
			servlet.authenticate request, response
			fail ''
		}catch(AuthenticationException e){
		}
	}
	
	@After
	void tearDown(){
		super.tearDown()
		application.setParameter 'security.oauth.default', 'false'
	}
}
