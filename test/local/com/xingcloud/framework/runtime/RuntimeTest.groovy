package com.xingcloud.framework.runtime;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.xingcloud.framework.config.AppConfig
import com.xingcloud.test.AbstractTest;
class RuntimeTest extends AbstractTest {

	def runtime
	@Before
	public void setUp() throws Exception {
		super.setUp()
		runtime = Runtime.getInstance()
	}

	@Test
	public void testSetRuntime_environment() {
		
		assertEquals 'cloud', runtime.getRuntimeEnvironment()
		runtime.setRuntimeEnvironment("local")
		assertEquals 'local', runtime.getRuntimeEnvironment()
		runtime.setRuntimeEnvironment("cloud")
		
	}

	@Test
	public void testGet() {
		
		AppConfig appconfig = runtime.getAppconfig()
		assertNotNull appconfig
		assertEquals 'xingcloud-game', appconfig.get('project.name')
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
