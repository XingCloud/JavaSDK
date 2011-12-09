package com.xingcloud.framework.util.lock;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.xingcloud.framework.cache.CacheService;
import com.xingcloud.framework.cache.CacheServiceFactory;
import com.xingcloud.test.AbstractTest;

class MemcachedLockerTest extends AbstractTest {

	def locker = new MemcachedLocker()
	def lockable = new SimpleLockable("SimpleLockable-Test")
	
	@After
	public void tearDown() throws Exception {
		CacheServiceFactory.getCacheService().getClient().delete lockable.getLockId()
	}
	
	@Test
	public void testIsLocked() {
		assertFalse locker.isLocked(lockable)
		locker.lock lockable, 2
		assertTrue locker.isLocked(lockable)
		sleep 3000
		assertFalse locker.isLocked(lockable)
	}

	@Test
	public void testLockLockableInt() {
		locker.lock lockable, 2
		assertNotNull CacheServiceFactory.getCacheService().getClient().get(lockable.getLockId())
		sleep 3000
		assertNull CacheServiceFactory.getCacheService().getClient().get(lockable.getLockId())
	}

	@Test
	public void testLockLockable() {
		locker.lock lockable
		assertNotNull CacheServiceFactory.getCacheService().getClient().get(lockable.getLockId())
	}

	@Test
	public void testRelease() {
		locker.lock lockable
		assertTrue locker.isLocked(lockable)
		locker.release lockable
		assertFalse locker.isLocked(lockable)
	}

}
