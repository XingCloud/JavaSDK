package com.xingcloud.framework.integration.http.memcached;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.xingcloud.framework.cache.CacheService;
import com.xingcloud.framework.cache.CacheServiceFactory;
import com.xingcloud.framework.integration.http.session.SessionService;
import com.xingcloud.test.AbstractTest;

class SessionServiceTest extends AbstractTest{

	@Test
	public void testGetSession() {
		def sid = new Date().time.toString()
		def s = SessionService.getInstance().getSession sid
		assertNotNull s
	}

	@Test
	public void testSaveSession() {
		def sid = new Date().time.toString()
		def s = SessionService.getInstance().getSession sid
		s.test = 'ok'
		SessionService.getInstance().saveSession(sid, s)
		
		s = SessionService.getInstance().getSession sid
		assertNotNull s
		assertEquals 'ok', s.test
	}

	@Test
	public void testRemoveSession() {
		def sid = new Date().time.toString()
		def s = SessionService.getInstance().getSession sid
		assertNotNull s
		
		SessionService.getInstance().removeSession sid
		s = CacheServiceFactory.getCacheService().client.get sid
		assertNull s
	}

}
