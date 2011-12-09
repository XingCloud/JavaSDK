package com.xingcloud.test.other;

import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.xingcloud.test.AbstractTest;

public class XingcloudApplicationParameterTest extends AbstractTest {
	
	@Before
	public void setUp() throws Exception{
		super.setUp();
	}
	
	@Test
	public void ParameterTest() {
		Map<String, Object> param = application.getParameters();
		
		assertTrue(param.containsKey("security.oauth.expires")); 
		assertTrue(param.containsKey("security.oauth.default")); 
		assertTrue(param.containsKey("security.oauth.secretKey")); 	
		assertTrue(param.containsKey("security.oauth.consumerKey")); 
		
		assertTrue(param.containsKey("security.admin.expires")); 
		assertTrue(param.containsKey("security.admin.default")); 
		assertTrue(param.containsKey("security.admin.secretKey"));
		assertTrue(param.containsKey("security.admin.consumerKey"));
		
		assertTrue(param.containsKey("project.name"));
		assertTrue(param.containsKey("project.locale"));
		assertTrue(param.containsKey("project.language"));
		
		assertTrue(param.containsKey("persistence"));
		if(param.containsKey("persistence.mysql")){
			assertTrue(param.containsKey("persistence.mysql.host"));
			assertTrue(param.containsKey("persistence.mysql.port"));
			assertTrue(param.containsKey("persistence.mysql.user"));
			assertTrue(param.containsKey("persistence.mysql.password"));
			assertTrue(param.containsKey("persistence.mysql.database"));
		}else if(param.containsKey("persistence.data_proxy")) {
			param.containsKey("persistence.data_proxy.instance_id");
			param.containsKey("persistence.data_proxy.host");
			param.containsKey("persistence.data_proxy.port");
		}else if(param.containsKey("persistence.mc_mysql")) {
			param.containsKey("persistence.mc_mysql.mchost");
			param.containsKey("persistence.mc_mysql.mcport");
			param.containsKey("persistence.mc_mysql.myhost");
			param.containsKey("persistence.mc_mysql.myport");
			param.containsKey("persistence.mc_mysql.myuser");
			param.containsKey("persistence.mc_mysql.mypassword");
			param.containsKey("persistence.mc_mysql.mydatabase");
		}
	}

}
