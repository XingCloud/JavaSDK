package com.xingcloud.framework.config.file;

import static org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

import com.xingcloud.framework.context.application.XingCloudApplication

class GlobalXMLConfigLoaderTest {

	def gxl, app

	@Before
	public void setUp() throws Exception {
		gxl = new GlobalXMLConfigLoader()
		app = XingCloudApplication.getInstance()
	}

	@Test
	public void testLoadPath() {
		gxl.setFilePath('WebContent/WEB-INF/config/global.xml')
		gxl.load()
		assertEquals 'xingcloud-game', app.getParameter('project.name')
	}

	@After
	public void tearDown() throws Exception {
	}
}
