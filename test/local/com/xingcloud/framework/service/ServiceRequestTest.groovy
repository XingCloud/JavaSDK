package com.xingcloud.framework.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.xingcloud.framework.service.request.ServiceRequest;

class ServiceRequestTest {

	@Test
	public void testGetParameters1() {
		ServiceRequest r = new ServiceRequest(null)
		r.parameters.put 'data', []
		assertNotNull r.parameters.data
		r.close()
	}
	
	@Test
	public void testGetParameters2() {
		ServiceRequest r = new ServiceRequest(null)
		r.parameters.put 'data', [:]
		println r.getData()
		println r.parameters
		assertNull r.parameters.data
		r.close()
	}
}
