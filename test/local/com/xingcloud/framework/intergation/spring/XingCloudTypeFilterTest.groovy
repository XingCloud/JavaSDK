package com.xingcloud.framework.intergation.spring;

import static org.junit.Assert.*

import org.junit.Test

import com.xingcloud.framework.integration.spring.XingCloudTypeFilter
import com.xingcloud.framework.service.test.TestServiceImpl
import com.xingcloud.test.AbstractTest

public class XingCloudTypeFilterTest extends AbstractTest {

	@Test
	public void testIsModuleClass() {
		def f = new XingCloudTypeFilter()
		assertTrue f.isModuleClass(TestServiceImpl)
	}

}
