package com.xingcloud.framework.service.discovery;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.context.request.Request;
import com.xingcloud.framework.service.annotation.CloudService;
import com.xingcloud.framework.service.annotation.Description;
import com.xingcloud.framework.service.annotation.Protocol;
import com.xingcloud.framework.service.annotation.ServiceParam;
import com.xingcloud.framework.service.config.ServiceConfig;
import com.xingcloud.util.reflection.ReflectionUtil;
import com.xingcloud.util.string.Charset;
import com.xingcloud.util.string.URLEncoder;

/**
 * 服务查询的service
 * <p>使用该Service可以查找现有的服务的相关信息，如使用何种协议，允许http传入参数的方法，服务描述等，这些都需要使用注解在服务方法前进行标注</p>
 *
 */
@CloudService(value = "discoveryService", api = "discovery")
public class DiscoveryServiceImpl implements DiscoveryService {
	/**
	 * 查询现有的所有服务
	 * 
	 * @return Object 返回服务的列表
	 */
	@Description(content = "查找现在有的所有服务")
	public Object getServices() {
		@SuppressWarnings("unchecked")
		Map<String, ServiceConfig> services = (Map<String, ServiceConfig>) XingCloudApplication
				.getInstance().getParameter(ServiceConfig.SERVICE + "byApi");
		Class<?> clazz;
		Method[] methods;
		Map<String, Serializable> result = new HashMap<String, Serializable>();
		for (ServiceConfig serviceConfig : services.values()) {
			try {
				clazz = Class.forName(serviceConfig.getClassName());
				if (!clazz.isAnnotationPresent(CloudService.class)) {
					continue;
				}
				methods = clazz.getMethods();
				Map<String, Serializable> api = new HashMap<String, Serializable>();
				for (Method method : methods) {
					if (method.getModifiers() == Modifier.PUBLIC && method.isAnnotationPresent(Description.class)) {
						Map<String, Serializable> methodMap = new HashMap<String, Serializable>();
						if (method.isAnnotationPresent(Protocol.class)) {
							Protocol annotation = method
									.getAnnotation(Protocol.class);
							Map<String, String> annotationMap = new HashMap<String, String>();
							annotationMap.put("allow", annotation.allow());
							annotationMap.put("deny", annotation.deny());
							methodMap.put("Protocol",
									(Serializable) annotationMap);
						}
						if (method
								.isAnnotationPresent(com.xingcloud.framework.service.annotation.Method.class)) {
							com.xingcloud.framework.service.annotation.Method annotation = method
									.getAnnotation(com.xingcloud.framework.service.annotation.Method.class);
							Map<String, String> annotationMap = new HashMap<String, String>();
							annotationMap.put("allow", annotation.allow());
							annotationMap.put("deny", annotation.deny());
							methodMap.put("Method",
									(Serializable) annotationMap);
						}
						StringBuffer methodName = new StringBuffer(
								method.getName());
						methodName.setCharAt(0,
								Character.toLowerCase(methodName.charAt(0)));
						api.put(methodName.toString(), (Serializable) methodMap);
					}
					if(api.size()>=1){
						result.put(serviceConfig.getApi(), (Serializable) api);					
					}
				}
			} catch (Exception e) {
				Logger.getLogger(getClass()).error(e.getMessage(), e);
			}
		}
		return result;
	}

	/**
	 * 查找单个服务的信息
	 * @param req 对这个服务的请求
	 * @return Object 返回这个服务的信息
	 */
	@Description(content = "查找单个服务")
	@ServiceParam(name = "serviceID,methodName", type = "string,string", remark = "服务名,方法名")
	public Object getSingleService(Request req) {
		@SuppressWarnings("unchecked")
		Map<String, ServiceConfig> services = (Map<String, ServiceConfig>) XingCloudApplication
				.getInstance().getParameter(ServiceConfig.SERVICE + "byApi");
		Class<?> clazz;
		Method[] methods;
		Map<String, Serializable> result = new HashMap<String, Serializable>();
		String inputMethodName;
		String serviceID;
		if (req.getParameter("methodName") != null
				&& req.getParameter("serviceID") != null) {
			inputMethodName = (String) req.getParameter("methodName");
			serviceID = (String) req.getParameter("serviceID");
		} else {
			return null;
		}
		for (ServiceConfig serviceConfig : services.values()) {
			try {
				clazz = Class.forName(serviceConfig.getClassName());
				if (!clazz.isAnnotationPresent(CloudService.class)) {
					continue;
				}
				if (serviceConfig.getApi().equals(serviceID.toString())) {
					methods = clazz.getMethods();
					for (Method method : methods) {
						StringBuffer methodName = new StringBuffer(
								method.getName());
						if (inputMethodName.equals(methodName.toString())) {
							Map<String, Serializable> methodMap = new HashMap<String, Serializable>();
							if (method.isAnnotationPresent(Protocol.class)) {
								Protocol annotation = method
										.getAnnotation(Protocol.class);
								Map<String, String> annotationMap = new HashMap<String, String>();
								annotationMap.put("allow", annotation.allow());
								annotationMap.put("deny", annotation.deny());
								methodMap.put("Protocol",
										(Serializable) annotationMap);
							}
							if (method
									.isAnnotationPresent(com.xingcloud.framework.service.annotation.Method.class)) {
								com.xingcloud.framework.service.annotation.Method annotation = method
										.getAnnotation(com.xingcloud.framework.service.annotation.Method.class);
								Map<String, String> annotationMap = new HashMap<String, String>();
								annotationMap.put("allow", annotation.allow());
								annotationMap.put("deny", annotation.deny());
								methodMap.put("Method",
										(Serializable) annotationMap);
							}
							if (method.isAnnotationPresent(Description.class)) {
								Description descriptionAnnotion = method
										.getAnnotation(Description.class);
								String description = descriptionAnnotion
										.content();
								description = URLEncoder.encode(description,
										Charset.UTF8);
								Map<String, String> annotationMap = new HashMap<String, String>();
								annotationMap.put("description", description);
								methodMap.put("Description",
										(Serializable) annotationMap);
							}
							if (method.isAnnotationPresent(ServiceParam.class)) {
								ServiceParam serviceParam = method
										.getAnnotation(ServiceParam.class);

								String type = serviceParam.type();
								String name = serviceParam.name();
								String remark = serviceParam.remark();
								String[] types = type.split(",");
								String[] names = name.split(",");
								String[] remarks = remark.split(",");
								Map<String, Serializable> paramMap = new HashMap<String, Serializable>();
								for (int i = 0; i < types.length; i++) {
									Map<String, String> annotationMap = new HashMap<String, String>();
									remarks[i] = URLEncoder.encode(remarks[i],
											Charset.UTF8);
									annotationMap.put("type", types[i]);
									annotationMap.put("name", names[i]);
									annotationMap.put("remark", remarks[i]);
									paramMap.put(String.valueOf(i),
											(Serializable) annotationMap);
								}
								methodMap.put("Serviceparam",
										(Serializable) paramMap);
							}
							result.put(inputMethodName,
									(Serializable) methodMap);
							break;
						}
					}
					break;
				}
			} catch (Exception e) {
				Logger.getLogger(getClass()).error(e.getMessage(), e);
			}
		}
		return result;
	}
}