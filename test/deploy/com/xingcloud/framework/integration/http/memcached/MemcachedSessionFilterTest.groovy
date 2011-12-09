package com.xingcloud.framework.integration.http.memcached;

import static org.junit.Assert.*

import org.apache.commons.httpclient.Cookie
import org.junit.Test

import com.xingcloud.framework.context.request.Request
import com.xingcloud.framework.integration.http.session.SessionService
import com.xingcloud.framework.service.annotation.CloudService
import com.xingcloud.service.AbstractServiceTest

class MemcachedSessionFilterTest extends AbstractServiceTest {

	@Test
	public void testDoFilter() {
		def rlt = connector.sendAPIRequestWithHTTPClient("filter.test.sendRequest1", [id:1], null)
		
		assertEquals 100, rlt.result.data.value
		def sid = rlt.cookies.find { it.name == 'xingcloud-sid' }.value
		def s = SessionService.getInstance().getSession sid
		assertEquals 'ok', s.test
		
		def mycookie = new Cookie("", "xingcloud-sid", sid, "/", null, false)
		rlt = connector.sendAPIRequestWithHTTPClient("filter.test.sendRequest2", [id:1], [mycookie])
		assertEquals 'ok', rlt.result.data
	}

}

@CloudService(value = "filterTestService", api = "filter.test")
class FilterTestService{
	def sendRequest1(Request req){
		def s = req.getSession()
		s.setParameter('test', 'ok')
		[value:100]
	}
	
	def sendRequest2(Request req){
		def s = req.getSession()
		s.getParameter('test')
	}
}
