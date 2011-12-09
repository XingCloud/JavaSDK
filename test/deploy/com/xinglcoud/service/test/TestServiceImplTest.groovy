package com.xinglcoud.service.test

import static org.junit.Assert.*

import org.junit.Test

import com.xingcloud.service.AbstractServiceTest

class TestServiceImplHTTPTest extends AbstractServiceTest {


	@Test
	public void testDoService() {
		def rlt = connector.sendURLRequest("test/test", [:])
		assertNotNull rlt
		assertEquals 200, rlt["code"]
		assertEquals "success", rlt["data"]["serviceTest"]["result"]
	}

}
