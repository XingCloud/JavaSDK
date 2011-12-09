package com.xingcloud.framework.config;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.xingcloud.test.AbstractTest;

class AppConfigFactoryTest  extends AbstractTest {

	@Before
	public void setUp() throws Exception {
		super.setUp()
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown()
	}

	@Test
	public void testGetAppConfig() {
		def appConfig = AppConfigFactory.getAppConfig()
		println appConfig.toString()
		
	}

}
