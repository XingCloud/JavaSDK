package com.xingcloud.service.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import com.xingcloud.framework.service.discovery.DiscoveryServiceImpl;
import com.xingcloud.framework.service.request.ServiceRequest;
import com.xingcloud.framework.service.status.StatusServiceImpl;
import com.xingcloud.util.annotation.CloudTest;
import com.xingcloud.util.json.JSONObject;

/**
 * 
 * 服务测试类
 * 
 */
@CloudTest(value="serviceTest")
public class ServiceTest{


	private ServiceRequest request;
	
	private void setUp() throws Exception {
		request = new ServiceRequest(null);
	}
	
	@SuppressWarnings("unchecked")
	private void testStatusDoStatus() throws Exception {
		StatusServiceImpl statusService = new StatusServiceImpl();
		JSONObject jasonArray = new JSONObject();
		jasonArray.put("lang", "cn");
		request.setParameter("data", jasonArray);
		request.setParameter("lang", "cn");
		JSONObject result = new JSONObject((Map<String, Object>)statusService.status(request));
		Assert.assertNotNull(result);
	}

	@SuppressWarnings("unchecked")
	private void testDiscoveryService() throws Exception {
		DiscoveryServiceImpl discoveryService = new DiscoveryServiceImpl();
		JSONObject result = new JSONObject((Map<String, Object>)discoveryService.getServices());		
		Assert.assertNotNull(result);
	}
	
	/**
	 * 测试status服务，以测试服务能否正常访问
	 */
	public Map<String, String> test(){
		Map<String, String> service = new HashMap<String, String>();
		boolean STest = true;
		try{
			setUp();
			testStatusDoStatus();
			testDiscoveryService();			
		} catch (Exception e) {
			//如果出现exception保存错误信息
			service.put(e.getClass().getName(), e.toString());
			STest = false;
		} catch (AssertionFailedError e){
			//如果出现返回异常保存信息
			service.put(e.getClass().getName(), e.toString());
			STest = false;		
		}
		//如果serivce测试通过
		if (STest) {
			service.put("result", "success");
		} else {
		//如果service测试不通过
			service.put("result", "failure");
		}
		return service;
	}
}
