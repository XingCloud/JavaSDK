package com.xingcloud.framework.cache;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.xingcloud.test.AbstractTest;

class CacheServiceTest extends AbstractTest {

	@Test
	public void testIsMemcachedAvailable() {
		assertTrue CacheServiceFactory.getCacheService().isMemcachedAvailable()
	}

}
