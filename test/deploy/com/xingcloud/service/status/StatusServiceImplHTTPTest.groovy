package com.xingcloud.service.status

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

import com.xingcloud.service.AbstractServiceTest

class StatusServiceImplHTTPTest extends AbstractServiceTest {


	@Before
	public void setUp() {
		super.setUp()
	}
	
	@Test
	public void testDoStatus1() {
		def req = [id:'100', lang:'cn']
		def rlt = connector.sendURLRequest("/status/status", req)
		assertNotNull rlt
		assertNotNull rlt.'data'
		assertEquals 200, rlt.'code'
		assertNotNull rlt.'data'.'server_time'
	}

	@Test
	public void testDoStatus2() {
		def req = [data:[lang:'cn']]
		def rlt = connector.sendURLRequest("/status/status", req)
		assertNotNull rlt
		assertNotNull rlt.'data'
		assertEquals 200, rlt.'code'
		assertNotNull rlt.'data'.'server_time'
	}

	@Test
	public void testDoAdmin() {
		def req = [lang:'cn']
		def rlt = connector.sendURLRequest("/status/admin", req)
		assertNotNull rlt
		assertNotNull rlt.'data'
		assertEquals 200, rlt.'code'
		assertNotNull rlt.'data'.'server_time'
	}
}
