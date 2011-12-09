package com.xingcloud.framework.module.config;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.xingcloud.framework.bean.config.BeanConfig
import com.xingcloud.framework.config.file.FileConfigLoader;
import com.xingcloud.framework.context.application.XingCloudApplication;

public class ModuleFileConfigLoaderTest {
	@Test
	public void testLoad() {
		new ModuleFileConfigLoader().load(); 
		def m = XingCloudApplication.getInstance().getParameter(ModuleConfig.MODULE);
		def i = XingCloudApplication.getInstance().initiators
		assertTrue m.size()>0
		def ms = m.Core
		assertEquals 'Core', ms.name
		assertEquals true, ms.required
		assertEquals 'com.xingcloud.framework.service', ms.packageName[0]
		assertEquals  null, ms.resource
		assertEquals 'global.xml', ms.config
		assertEquals 'com.xingcloud.framework.service.annotation.CloudService', ms.component[0] 
		assertEquals 0, ms.listener.size()
	}
	
}
