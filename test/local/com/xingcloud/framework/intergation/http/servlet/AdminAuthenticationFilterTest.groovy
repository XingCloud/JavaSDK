package com.xingcloud.framework.intergation.http.servlet;

import static org.junit.Assert.*

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.junit.*

import com.xingcloud.framework.context.application.XingCloudApplication
import com.xingcloud.framework.integration.http.servlet.AdminAuthenticationFilter
import com.xingcloud.framework.security.*
import com.xingcloud.test.AbstractTest


public class AdminAuthenticationFilterTest extends AbstractTest{

	def request , response  , filter

	@Before
	public void setUp() throws Exception {
		super.setUp()
		response = [] as HttpServletResponse
		request = [] as HttpServletRequest
		filter = new AdminAuthenticationFilter()
	}

	//admin的default为false，不认证
	@Test
	public void testAuthenticate1() {
		try{
			application.setParameter("security.admin.default", "false")
			filter.authenticate(request, response)
		}catch(e){
			fail(e.toString())
		}
	}

	//admin的default为true，认证
	@Test
	public void testAuthenticate2() { 
		try{
			application.setParameter("security.admin.default", "true")
			filter.authenticate(request, response)
			fail ''
		}catch(AuthenticationException e){
		}
	}

	//admin的default没有配置，认证
	@Test
	public void testAuthenticate3() {	
		try{
			application.removeParameter 'security.admin.default'
			filter.authenticate(request, response)
			fail ''
		}catch(AuthenticationException e){
			assertTrue true
		}
	}


	//admin的default配置错误，认证
	@Test
	public void testAuthenticate4() {  	
		try{
			application.setParameter("security.admin.default", "default value error")
			filter.authenticate(request, response)
			fail('false')
		}catch(AuthenticationException e){
			assertTrue true
		}
	}
	
	//admin的default为FALse，不认证
	@Test
	public void testAuthenticate5() {
		try{
			application.setParameter("security.admin.default", "FALse")
			filter.authenticate(request, response)
		}catch(e){
			fail(e.toString())
		}
	}
	
	@After
	void tearDown(){
		super.tearDown()
		application.setParameter 'security.admin.default', 'false'
	}
}
