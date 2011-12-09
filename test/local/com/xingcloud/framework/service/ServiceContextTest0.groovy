package com.xingcloud.framework.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.xingcloud.framework.service.status.StatusService;

class ServiceContextTest0 {

	
	@Test
	public void testFindServiceMethod() {
		def ctx = new ServiceContext()
		def m = ctx.findServiceMethod StatusService, 'status'
		assertNotNull m
	}

}
