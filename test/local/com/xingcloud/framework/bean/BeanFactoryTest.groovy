package com.xingcloud.framework.bean;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import com.xingcloud.test.AbstractTest;

class BeanFactoryTest extends AbstractTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void testGetBeanConfig() {
		def bc = BeanFactory.getInstance().getBeanConfig('BeanFactoryTestClass')
		assertNotNull bc
	}
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
