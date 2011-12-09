package com.xingcloud.framework.util.lock;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.test.AbstractTest;

class LockTest2 extends AbstractTest {

	@Before
	public void setUp() throws Exception {
		super.setUp()
		Lock.getInstance().lockers = null
	}
	
	@Test
	public void testGet() {
		assertTrue XingCloudApplication.getInstance().hasParameter("memcached.host")
		assertTrue XingCloudApplication.getInstance().hasParameter("memcached.port")
		
		def locker = Lock.get()
		assertNotNull locker
	}

}
