package com.xingcloud.framework.context.application;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xingcloud.framework.bean.BeanFactory;
import com.xingcloud.framework.config.file.FileConfigLoader;
import com.xingcloud.framework.config.file.GlobalXMLConfigLoader;
import com.xingcloud.framework.context.Context;
import com.xingcloud.framework.context.Destroyable;
import com.xingcloud.framework.context.application.event.XingCloudClosedEvent;
import com.xingcloud.framework.context.application.event.XingCloudErrorEvent;
import com.xingcloud.framework.context.application.event.XingCloudStartedEvent;
import com.xingcloud.framework.event.XingCloudEventPublisher;
import com.xingcloud.framework.initiator.Initiator;
import com.xingcloud.framework.initiator.annotation.CloudInitiator;
import com.xingcloud.framework.initiator.config.InitiatorClassConfigLoader;
import com.xingcloud.framework.integration.log4j.Log4jConfigLoader;
import com.xingcloud.util.json.JSONUtil;
import com.xingcloud.util.reflection.ReflectionUtil;
import com.xingcloud.util.string.Charset;

import flex.messaging.util.URLDecoder;

/**
 * 行云应用类，用于储存全局信息
 * 
 */
public class XingCloudApplication extends AbstractApplication implements Application, Context {
	protected static final XingCloudApplication instance = new XingCloudApplication();	
	protected static List<Object> singletons = new CopyOnWriteArrayList<Object>();
	protected static List<Initiator> initiators = new CopyOnWriteArrayList<Initiator>();
	protected ApplicationContext applicationContext = null;
	protected static final ReentrantLock lock = new ReentrantLock(false);

	protected String basePath = "";
	protected boolean loaded = false;

	/**
	 * 获取行云应用类的一个实例
	 * 
	 */
	public synchronized static XingCloudApplication getInstance() {
		return instance;
	}

	private XingCloudApplication() {
	}
	
	/**
	 * 获取spring的ApplicationContext
	 * 
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 设置spring的ApplicationContext
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * 行云应用类的初始化方法
	 */
	public void start() {
		lock.lock();
		try{
			load();
		}finally{
			lock.unlock();
		}
		XingCloudEventPublisher.getInstance().publishEvent(new XingCloudStartedEvent(this));
	}

	/**
	 * 行云应用类的关闭方法，释放全局资源
	 */
	public void close() {
		XingCloudEventPublisher.getInstance().publishEvent(new XingCloudClosedEvent(this));
		lock.lock();
		try{
			for (Object object : singletons) {
				try {
					if (object instanceof Destroyable) {
						((Destroyable) object).destroy();
					}
				} catch (Exception e) {
					Logger.getLogger(getClass()).error(e.getMessage(), e);
					XingCloudEventPublisher.getInstance().publishEvent(new XingCloudErrorEvent(this,e));
				}
			}
			singletons.clear();
			singletons = null;
			basePath = null;
			loaded = false;
			ReflectionUtil.clearAll();
			JSONUtil.clearAll();
			super.close();
		}finally{
			lock.unlock();
		}
	}

	/**
	 * 加载配置的方法
	 */
	public void load() {
		if (loaded) {
			return;
		}
		try {
			//配置BeanFactory
			BeanFactory.getInstance().setApplicationContext(applicationContext);
			
			//加载global配置信息
			FileConfigLoader loader = new GlobalXMLConfigLoader();
			loader.setFilePath(URLDecoder.decode(
					new StringBuffer().append(getBasePath())
					.append(File.separator).append("config")
					.append(File.separator).append("global.xml")
					.toString(), Charset.UTF8));
			loader.load();
			
			//加载log4j配置信息
			loader = new Log4jConfigLoader();
			loader.setFilePath(URLDecoder.decode(
					new StringBuffer().append(getBasePath())
					.append(File.separator).append("config")
					.append(File.separator).append("log4j.properties")
					.toString(), Charset.UTF8));
			loader.load();
					
			// 加载所有模块的初始化类
			InitiatorClassConfigLoader initiatorloader = new InitiatorClassConfigLoader();
			initiatorloader.setObjects(this.applicationContext.getBeansWithAnnotation(CloudInitiator.class));
			initiatorloader.load();
			for(Initiator moduleInitiator : initiators){
				moduleInitiator.init();
			}
			loaded = true;
		} catch (Exception e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
			XingCloudEventPublisher.getInstance().publishEvent(new XingCloudErrorEvent(this,e));
		}
	}

	/**
	 * 设置行云后台的路径
	 * @param basePath
	 */
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	/**
	 * 获取行云后台的路径
	 * @return
	 */
	public String getBasePath() {
		return basePath;
	}

	/**
	 * 添加向行云应用类注册的单例模式类实例
	 * @param object
	 */
	public void addSingleton(Object object) {
		singletons.add(object);
	}

	/**
	 * 添加初始化类
	 * @param initiator
	 */
	public void addInitiator(Initiator initiator) {
		initiators.add(initiator);
	}
}