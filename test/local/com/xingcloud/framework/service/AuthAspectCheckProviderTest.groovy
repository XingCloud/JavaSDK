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
import com.xingcloud.test.AbstractTest
import com.xingcloud.framework.integration.http.protocol.RESTServiceProtocol;
import com.xingcloud.framework.service.status.StatusServiceImpl


class AuthAspectCheckProviderTest extends AbstractTest{

	def authAspect, map , methodName
	@Before
	void setUp() throws Exception {
		super.setUp()
		authAspect = new AuthAspect()
		methodName = 'user.user.doPlatformLogin'
		map = ['user.user.doPlatformLogin']
	}

	//oauth的default为false，不认证
	@Test
	public void testCheckProvider1() {
		application.setParameter 'security.oauth.default', 'false'
		assertFalse authAspect.checkProvider(methodName)
	}

	//oauth的default为false，method在白名单中，认证
	@Test
	public void testCheckProvider2() {
		application.setParameter 'security.oauth.default', 'false'
		application.setParameter 'security.oauth.service.enabled', map
		assertTrue authAspect.checkProvider(methodName)
	}

	//oauth的default为true，method在黑名单中，不认证
	@Test
	public void testCheckProvider3() {
		application.setParameter 'security.oauth.default', 'true'
		application.setParameter 'security.oauth.service.disabled', map
		assertFalse authAspect.checkProvider(methodName)
	}

	//oauth的default为true，method没在黑名单中，认证
	@Test
	public void testCheckProvider4() {
		application.removeParameter 'security.oauth.service.disabled'
		application.setParameter 'security.oauth.default', 'true'
		assertTrue authAspect.checkProvider(methodName)
	}

	//oauth的default为错误值，method在黑名单中，不认证
	@Test
	public void testCheckProvider5() {
		application.setParameter 'security.oauth.default', 'default value error'
		application.setParameter 'security.oauth.service.disabled', map
		assertFalse authAspect.checkProvider(methodName)
	}

	//oauth的default为错误值，method没在黑名单中，认证
	@Test
	public void testCheckProvider6() {
		application.removeParameter 'security.oauth.service.disabled'
		application.setParameter 'security.oauth.default', 'default value error'
		assertTrue authAspect.checkProvider(methodName)
	}

	//oauth中没有default配置，method在黑名单中，不认证
	@Test
	public void testCheckProvider7() {
		application.removeParameter 'security.oauth.default'
		application.setParameter 'security.oauth.service.disabled', map
		assertFalse authAspect.checkProvider(methodName)
	}

	//oauth中没有default配置，method没在黑名单中，认证
	@Test
	public void testCheckProvider8() {
		application.removeParameter 'security.oauth.default'
		application.removeParameter 'security.oauth.service.disabled'
		assertTrue authAspect.checkProvider(methodName)
	}
	
	//oauth的default为FALSe，false忽略大小写，不认证
	@Test
	public void testCheckProvider9() {
		application.removeParameter 'security.oauth.service.disabled'
		application.removeParameter 'security.oauth.service.enabled'
		application.setParameter 'security.oauth.default', 'FALSe'
		assertFalse authAspect.checkProvider(methodName)
	}

	@After
	void tearDown(){
		super.tearDown()
		application.setParameter 'security.oauth.default', 'false'
	}
}
