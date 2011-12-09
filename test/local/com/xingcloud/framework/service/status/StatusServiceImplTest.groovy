package com.xingcloud.framework.service.status;

import static org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

import com.xingcloud.framework.service.annotation.AdminStatus
import com.xingcloud.framework.service.annotation.CloudService
import com.xingcloud.framework.service.annotation.Status
import com.xingcloud.framework.service.request.ServiceRequest
import com.xingcloud.test.AbstractTest

class StatusServiceImplTest extends AbstractTest{

	def ss, request,md5
	@Before
	public void setUp() throws Exception {
		super.setUp()
		ss = new StatusServiceImpl()
		request = new ServiceRequest(null)
		request.setParameter('lang', 'cn')
	}
	@After
	public void tearDown() throws Exception {
		super.tearDown()
	}

	@Test
	public void testDoStatus() {
		def r = ss.status(request)
		println r
		assertNotNull r
		assertNotNull r.server_time
		assertNotNull r.teststatus
		assertNotNull r.teststatus.justTest
		assertNotNull r.teststatus.justTest.timestamp
		assertNotNull r.teststatus.justTest.md5
		md5 = r.teststatus.justTest.md5
	}

	@Test
	public void testDoAdmin() {
		def r = ss.admin(request)
		println r
		assertNotNull r
		assertNotNull r.server_time
		assertNotNull r.teststatus
		assertNotNull r.teststatus.justTest
		assertNotNull r.teststatus.justTest.timestamp
		assertNotNull r.teststatus.justTest.md5
		assertFalse md5.equals(r.teststatus.justTest.md5)
	}

}

@CloudService(value="testStatus", api="teststatus")
public class TestStatus{
	
	@Status(file="/config/global.xml")
	@AdminStatus(file="/config/spring.xml")
	public Object justTest() {
		return 1;
	}
}
