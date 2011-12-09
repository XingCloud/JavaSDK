package com.xingcloud.service
import java.util.Properties

import junit.framework.Assert

import org.junit.Before
import org.springframework.context.support.FileSystemXmlApplicationContext

import com.xingcloud.framework.context.application.XingCloudApplication
import com.xingcloud.framework.integration.spring.BeforeSpringInitiator
import com.xingcloud.framework.integration.spring.XingCloudBeforeSpringInitiator
import com.xingcloud.service.connector.IConnector
import com.xingcloud.storage.orm.session.SessionFactory

public abstract class AbstractServiceTest {

	protected static def uri
	protected static def platformAppId = null
	protected static def platformUserId
	protected static def platformUserId2
	protected static IConnector connector = null
	protected static def APP_BASE_PATH = null
	
	protected String SPRING_CFG_1 = "src/spring.xml";
	protected String SPRING_CFG_2 = "WebContent/WEB-INF/config/spring.xml";
	
	def application, persistenceSession
	static {
		loadConfig()
		def r = System.currentTimeMillis()
		platformUserId = "User_" + r
		platformUserId2 = "User_" + (r+100)
	}
	
	def prop
	
	@Before
	void setUp() throws Exception{
		if(prop == null){
			prop = new Properties()
			prop.load AbstractServiceTest.getResourceAsStream("test.properties")
			if(prop.running_in_eclipse.toBoolean()){
				def core = prop.core
				APP_BASE_PATH = "$core/$APP_BASE_PATH";
				SPRING_CFG_1 = "$core/$SPRING_CFG_1";
				SPRING_CFG_2 = "$core/$SPRING_CFG_2";
			}
		}
		
		//在Spring之前做初始化配置
		BeforeSpringInitiator beforeSpringInitiator = new XingCloudBeforeSpringInitiator();
		beforeSpringInitiator.setBasePath(APP_BASE_PATH);
		beforeSpringInitiator.init();
		
		//启动XingCloudApplication
		application = XingCloudApplication.getInstance();
		application.setApplicationContext(new FileSystemXmlApplicationContext(SPRING_CFG_1, SPRING_CFG_2))
		
		application.setBasePath(APP_BASE_PATH);
		application.start();
		SessionFactory.setConfigPath(APP_BASE_PATH + '/config/global.xml');
		persistenceSession = SessionFactory.openSession()
	}

	/**
	 * 获取用户在游戏中的ID
	 * 
	 * @return String 用户在游戏中的ID
	 * @throws IOException
	 */
	def static getGameUserId(def platformAppId, def platformUserId) {
		def obj = [
			id:1,
			info:[platformAppId:platformAppId, platformUserId:platformUserId, abtest:'abtest'],
			data:[]
		]
		
		def rlt = connector.sendAPIRequest("user.user.platformLogin", obj)
		def data = rlt.data
		if (data == null) {
			rlt = connector.sendAPIRequest("user.user.platformRegister", obj)
			data = rlt.data
		}

		return data.uid
	}

	def static loadConfig() {
		def params = [:]
		def inStream = AbstractServiceTest.class.getResourceAsStream("cfg.properties")
		def properties = new Properties()
		properties.load(inStream)
		for (def entry : properties.entrySet()) {
			def key = entry.getKey()

			params.put(key, entry.getValue())
		}

		APP_BASE_PATH=params.get("test.deploy.app_base_path")
		def connClass = params.get("test.deploy.connector")
		Assert.assertNotNull("The param(test.deploy.connector) doesn't exist in the file(cfg.properties)", connClass)
		connector = (IConnector) Class.forName(connClass).newInstance()
		connector.config(params)

		platformAppId = params.get("test.deploy.platformAppId")
		Assert.assertNotNull("The param(test.deploy.platformAppId) doesn't exist in the file(cfg.properties)", platformAppId)
		uri = params.get("test.deploy.uri")
	}
	
}
