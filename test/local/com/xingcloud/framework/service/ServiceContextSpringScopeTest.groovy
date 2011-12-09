package com.xingcloud.framework.service;

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

import com.xingcloud.framework.bean.BeanFactory
import com.xingcloud.framework.integration.http.protocol.RESTServiceProtocol
import com.xingcloud.test.AbstractTest

class ServiceContextSpringScopeTest extends AbstractTest {
	
	@Before 
	public void setUp() throws Exception{
		super.setUp()
	}
	@Test
	public void testSpringScope(){
		def sc = (ServiceContext)BeanFactory.getInstance().getBean("ServiceContext")
		def sp = new RESTServiceProtocol()
		sc.serviceProtocol = sp
		def scanother = (ServiceContext)BeanFactory.getInstance().getBean("ServiceContext")
		def spanother = scanother.getServiceProtocol()
		assertNull spanother
		
	}

}
