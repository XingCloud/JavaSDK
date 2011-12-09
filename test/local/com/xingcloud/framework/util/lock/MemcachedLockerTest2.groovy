package com.xingcloud.framework.util.lock;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.xingcloud.framework.cache.CacheService;
import com.xingcloud.framework.cache.CacheServiceFactory;
import com.xingcloud.test.AbstractTest;

class MemcachedLockerTest2 extends AbstractTest {

	def locker = new MemcachedLocker()
	def lockable = new MyLockable()
	
	@After
	public void tearDown() throws Exception {
		CacheServiceFactory.getCacheService().getClient().delete lockable.getLockId()
	}
	
	@Test
	public void testLockLockable() {
		assertEquals 'none', lockable.status
		locker.lock lockable
		
		def locker2 = new MemcachedLocker()
		try{
			locker2.lock lockable
			fail 'should not'
		}catch(e){}
		assertEquals 'conflicted', lockable.status
	}

	@Test
	public void testRelease() {
		assertEquals 'none', lockable.status
		locker.lock lockable
		locker.release lockable
		assertEquals 'released', lockable.status
	}

}

class MyLockable implements Lockable{
	def status = 'none'
	String getLockId() throws Exception{'MyLockable'}
	void onLockConflicted(Locker locker) throws Exception{status = 'conflicted'}
	void onLockReleased(Locker locker) throws Exception{status = 'released'}
}