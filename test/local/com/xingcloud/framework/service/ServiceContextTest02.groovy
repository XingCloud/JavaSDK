package com.xingcloud.framework.service;

import org.junit.Before
import org.junit.Test

import com.xingcloud.framework.context.application.XingCloudApplication
import com.xingcloud.framework.service.annotation.CloudService
import com.xingcloud.framework.service.protocol.ServiceProtocol
import com.xingcloud.framework.service.request.ServiceRequest
import com.xingcloud.framework.service.response.ServiceResponse
import com.xingcloud.test.AbstractTest


class ServiceContextTest02 extends AbstractTest {

	def s
	
	@Before
	public void setUp(){
		super.setUp()
		
		def req = new ServiceRequest(null)
		req.service = 'testwarning.time'
		req.method = 'timefly'
		req.parameters = [:]
		
		def resp = new ServiceResponse(null){
			void close() {}
			void output(){println result.asResult()}
		}
		
		s = new ServiceContext()
		s.serviceProtocol = [
			getRequest:{req},
			getResponse:{resp},
			] as ServiceProtocol
	}
	
	@Test
	public void testFindServiceMethod() {
		XingCloudApplication.getInstance().setParameter('service.slow_request_warning', 1)
		s.start();
	}

}

@CloudService(value = "warnService", api = "testwarning.time")
class WarnService {

	def timefly(){
		sleep(3000)
	}
}
 
