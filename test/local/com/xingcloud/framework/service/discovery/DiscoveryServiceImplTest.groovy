package com.xingcloud.framework.service.discovery;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.xingcloud.framework.context.request.Request
import com.xingcloud.framework.service.request.ServiceRequest
import com.xingcloud.test.AbstractTest;

class DiscoveryServiceImplTest extends AbstractTest {

	def ds
	@Before
	public void setUp() throws Exception {
		super.setUp()
		ds = new DiscoveryServiceImpl()
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetServices() {
		def result = ds.getServices()
		assertNotNull result.status
		assertNotNull result.discovery
	}

	@Test
	public void testGetSingleService() {
		def request = new ServiceRequest(null)
		request.setParameter('methodName', 'getSingleService')
		request.setParameter('serviceID', 'discovery')
		
		def result = ds.getSingleService(request)
		assertNotNull result.getSingleService
	}

}
