package com.xingcloud.framework.service.discovery;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.context.request.Request;
import com.xingcloud.framework.service.annotation.CloudService;
import com.xingcloud.framework.service.annotation.Description;
import com.xingcloud.framework.service.annotation.ServiceParam;
import com.xingcloud.framework.service.config.ServiceConfig;
import com.xingcloud.util.reflection.ReflectionUtil;
import com.xingcloud.util.string.Charset;
import com.xingcloud.util.string.URLEncoder;


/**
 * AMF协议的服务发现类，用于查找AMF协议的服务
 * 
 */
@CloudService(value = "amfDiscoveryService", api = "amf.discovery")
public class AMFDiscoveryService implements DiscoveryService {
	/**
	 * 查找现在有的所有服务
	 * 
	 * @return Object 返回服务
	 * @author hsx
	 */
	@SuppressWarnings({ "unchecked" })
	@Description(content = "AMF协议查找现在有的所有服务")
	public Object getServices() {
		// 获得所有的服务map
		Map<String, ServiceConfig> services = (Map<String, ServiceConfig>) XingCloudApplication
				.getInstance().getParameter(ServiceConfig.SERVICE + "byApi");
		Map<String, Serializable> resultTemp = new HashMap<String, Serializable>();
		for (ServiceConfig serviceConfig : services.values()) {
			try {
				String id = serviceConfig.getApi();
				int index = id.indexOf(".");
				String label = id.substring(0, index);
				// 首字母大写转小写
				label = (new StringBuilder())
						.append(Character.toLowerCase(label.charAt(0)))
						.append(label.substring(1)).toString();
				String data = id.substring(index + 1, id.length());
				Map<String, String> map = new HashMap<String, String>();
				// map内的设置data为label，label为data
				map.put("label", data);
				map.put("data", label + "/");
				Map<String, Serializable> serviceMap;
				List<Map<String, String>> list = null;
				if (!resultTemp.containsKey(label)) {
					serviceMap = new HashMap<String, Serializable>();
					serviceMap.put("label", label);
					serviceMap.put("open", true);
					list = new ArrayList<Map<String, String>>();
					list.add(map);
				} else {
					serviceMap = (Map<String, Serializable>) resultTemp
							.get(label);
					list = (List<Map<String, String>>) serviceMap
							.get("children");
					list.add(map);
				}
				serviceMap.put("children", (Serializable) list);
				resultTemp.put(label, (Serializable) serviceMap);
			} catch (Exception e) {
				Logger.getLogger(getClass()).error(e.getMessage(), e);
			}
		}

		// 为保持格式一致，children数据格式的转换
		Map<String, Serializable> result = new HashMap<String, Serializable>();
		for (String key : resultTemp.keySet()) {
			Map<String, Serializable> serviceMap = (Map<String, Serializable>) resultTemp
					.get(key);
			List<Map<String, String>> list = (List<Map<String, String>>) serviceMap
					.get("children");
			serviceMap.remove("children");
			serviceMap.put("children", (Serializable) list.toArray());
			result.put(key, (Serializable) serviceMap);
		}
		return new ArrayList<Serializable>(result.values());
	}

	/**
	 * 描述指定服务的所有方法
	 * 
	 * @param req 该方法的请求
	 * @return Object 返回服务
	 * @author hsx
	 */
	@SuppressWarnings("unchecked")
	@Description(content = "AMF协议查找单个服务")
	@ServiceParam(name = "data,label", type = "string,String", remark = "服务名,方法名")
	public Object doDescribeService(Request req) {
		// 获得现有的服务
		Map<String, ServiceConfig> services = (Map<String, ServiceConfig>) XingCloudApplication
				.getInstance().getParameter(ServiceConfig.SERVICE + "byApi");
		Map<String, Serializable> resultTemp = new HashMap<String, Serializable>();
		Map<String, Serializable> result = new HashMap<String, Serializable>();
		String data = null;
		String label = null;
		String id = null;
		String className = null;
		Map<String, Method> methods;
		if (req.getParameter("data") != null
				&& req.getParameter("label") != null) {
			data = (String) req.getParameter("data");
			label = (String) req.getParameter("label");
			data = data.replaceAll("\\/", "");
			id = data + "." + label;
			// 首字母小写转大写
			id = (new StringBuilder())
					.append(Character.toUpperCase(id.charAt(0)))
					.append(id.substring(1)).toString();
		} else {
			return null;
		}
		for (ServiceConfig serviceConfig : services.values()) {
			try {
				if (serviceConfig.getApi().equals(id)) {
					className = serviceConfig.getClassName();
					@SuppressWarnings("rawtypes")
					Class service = Class.forName(className);
					// 获得所有的方法
					methods = ReflectionUtil.getAllMethods(service,
							"^do[A-Z].*$");
					for (Method method : methods.values()) {
						if (method.getModifiers() == Modifier.PUBLIC) {
							String description = "";
							Map<String, Serializable> serviceMap = new HashMap<String, Serializable>();
							if (method.isAnnotationPresent(Description.class)) {
								Description descriptionAnnotion = method
										.getAnnotation(Description.class);
								description = descriptionAnnotion.content();
								description = URLEncoder.encode(description,
										Charset.UTF8);
							}
							@SuppressWarnings("rawtypes")
							List arguments = new ArrayList();
							arguments
									.add("request - XServiceRequest request 服务请求");
							String methodName = method.getName();
							methodName = methodName.substring(2);
							// 首字母大写转小写
							methodName = (new StringBuilder())
									.append(Character.toLowerCase(methodName
											.charAt(0)))
									.append(methodName.substring(1)).toString();
							serviceMap.put("access", (Serializable) "private");
							serviceMap.put("arguments",
									(Serializable) arguments.toArray());
							serviceMap.put("description",
									(Serializable) description);
							resultTemp.put(methodName,
									(Serializable) serviceMap);
						}
					}
				}
			} catch (Exception e) {
				Logger.getLogger(getClass()).error(e.getMessage(), e);
			}
		}
		// 模仿php的写法
		result.put("0", (Serializable) "");
		result.put("1", (Serializable) resultTemp);
		return new ArrayList<Serializable>(result.values());
	}
}