package com.xingcloud.framework.util.lock;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.xingcloud.framework.cache.CacheInitiator;
import com.xingcloud.framework.cache.CacheService;
import com.xingcloud.framework.cache.CacheServiceFactory;
import com.xingcloud.test.AbstractTest;

class LockTest1 extends AbstractTest {

	
	@Before
	public void setUp() throws Exception {
		super.setUp()
		
		Lock.getInstance().lockers = null
		CacheServiceFactory.config = null
		CacheServiceFactory.cacheService = null
		CacheServiceFactory.config([:])
//		if(CacheServiceFactory.getCacheService().isMemcachedAvailable()){
//			CacheServiceFactory.getCacheService().client.shutdown()
//			CacheServiceFactory.getCacheService().client = null
//		}
	}

	@After
	public void tearDown() throws Exception {
		Lock.getInstance().lockers = null
		CacheServiceFactory.config = null
		CacheServiceFactory.cacheService = null
		new CacheInitiator().init()
		super.tearDown()
	}
	
	@Test
	public void testGet() {
		def locker = Lock.get()
		assertNull locker
	}

}
