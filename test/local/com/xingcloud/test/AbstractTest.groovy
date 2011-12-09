package com.xingcloud.test;

import org.junit.After
import org.junit.Before
import org.springframework.context.support.FileSystemXmlApplicationContext

import com.xingcloud.framework.context.application.XingCloudApplication
import com.xingcloud.framework.integration.spring.BeforeSpringInitiator
import com.xingcloud.framework.integration.spring.XingCloudBeforeSpringInitiator
import com.xingcloud.storage.orm.session.PersistenceSession
import com.xingcloud.storage.orm.session.SessionFactory


public abstract class AbstractTest {
	def APP_BASE_PATH = "WebContent/WEB-INF";
	def SPRING_CFG_1 = "src/spring.xml";
	def SPRING_CFG_2 = "WebContent/WEB-INF/config/spring.xml";

	protected String username ="name" + (int)(Math.random()*100000), password = "pass";
	protected String platformUID = "testPlatform1", platformUserUID = "555";
	protected String platformAddress = platformUserUID + "_" + platformUID;

	protected static PersistenceSession persistenceSession = null;
	protected static XingCloudApplication application;
	public static boolean flag = false;
	protected static BeforeSpringInitiator beforeSpringInitiator;

	def getPersistenceSession(){
		persistenceSession
	}

	def prop
	@Before
	public void setUp() throws Exception {
		if(prop == null){
			prop = new Properties()
			prop.load AbstractTest.getResourceAsStream("test.properties")
			if(prop.running_in_eclipse.toBoolean()){
				def core = prop.core
				APP_BASE_PATH = "$core/$APP_BASE_PATH";
				SPRING_CFG_1 = "$core/$SPRING_CFG_1";
				SPRING_CFG_2 = "$core/$SPRING_CFG_2";
			}
		}

		if(!flag){
			// 在Spring之前做初始化配置
			beforeSpringInitiator = new XingCloudBeforeSpringInitiator();
			beforeSpringInitiator.setBasePath(APP_BASE_PATH);
			beforeSpringInitiator.init();

			// 启动XingCloudApplication
			application = XingCloudApplication.getInstance();
			application.setApplicationContext(new FileSystemXmlApplicationContext(SPRING_CFG_1, SPRING_CFG_2));
			application.setBasePath(APP_BASE_PATH);
			application.start();
			SessionFactory.setConfigPath(APP_BASE_PATH + '/config/global.xml')
			persistenceSession = SessionFactory.openSession();
			flag = true;
		}
	}

	@After
	public void tearDown() throws Exception {
		if (persistenceSession != null)
			persistenceSession.flush();
	}
}