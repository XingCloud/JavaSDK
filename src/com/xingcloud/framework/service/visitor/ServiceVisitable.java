package com.xingcloud.framework.service.visitor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.service.config.ServiceConfig;
import com.xingcloud.util.reflection.ReflectionUtil;

/**
 * 服务访问类，通过调用accept方法传入响应的扫描方法类可以对服务进行扫描
 * 
 */
public class ServiceVisitable{
	@SuppressWarnings("rawtypes")
	private static Map<String, Class> services = null;
	private static Map<String, Map<String, Method>> methods = null;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static void load(){
		services = new HashMap<String, Class>();
		methods = new HashMap<String, Map<String, Method>>();
		Map<String, ServiceConfig> serviceConfigs = (Map<String, ServiceConfig>) XingCloudApplication.getInstance().getParameter(ServiceConfig.SERVICE+"byApi");
		for(ServiceConfig serviceConfig : serviceConfigs.values()){
			try {
				Class clazz = Class.forName(serviceConfig.getClassName());
				services.put(serviceConfig.getApi(), clazz);
				Map<String, Method> serviceMethods = ReflectionUtil.getAllMethods(clazz, "^do[A-Z].*$");
				methods.put(serviceConfig.getApi(), serviceMethods);
			} catch (ClassNotFoundException e) {
				Logger.getLogger(ServiceVisitable.class).error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * 通过传入服务配置扫描类对服务进行特点方式扫描的方法
	 * @param visitor 服务配置扫描类
	 * @throws Exception
	 */
	public static void accept(ServiceConfigVisitor visitor) throws Exception{
		if(services == null || methods == null) load();
		
		@SuppressWarnings("unchecked")
		Map<String, ServiceConfig> serviceConfigs = (Map<String, ServiceConfig>) XingCloudApplication.getInstance().getParameter(ServiceConfig.SERVICE+"byApi");
		if(services == null
			|| services.isEmpty()){
			return;
		}
		for(ServiceConfig serviceConfig : serviceConfigs.values()){
			visitor.visit(serviceConfig);
		}
	}
	
	/**
	 * 通过传入服务类扫描类对服务进行特点方式扫描的方法
	 * @param visitor 服务类扫描类
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void accept(ServiceClassVisitor visitor) throws Exception{
		if(services == null || methods == null) load();
		
		for(Entry<String, Class> entry : services.entrySet()){
			visitor.visit(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * 通过传入服务方法扫描类对服务进行特点方式扫描的方法
	 * @param visitor 服务方法扫描类
	 * @throws Exception
	 */
	public static void accept(ServiceMethodVisitor visitor) throws Exception{
		if(services == null || methods == null) load();
		
		for(Entry<String, Map<String, Method>> entry : methods.entrySet()){
			for(Entry<String, Method> methodEntry : entry.getValue().entrySet()){
				visitor.visit(entry.getKey(), methodEntry.getValue());
			}
		}
	}
}