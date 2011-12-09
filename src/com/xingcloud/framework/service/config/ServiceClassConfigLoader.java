package com.xingcloud.framework.service.config;

import java.util.HashMap;
import java.util.Map;

import com.xingcloud.framework.bean.annotation.CloudBean;
import com.xingcloud.framework.config.clazz.AbstractClassConfigLoader;
import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.service.annotation.CloudService;
import com.xingcloud.util.string.StringUtil;

/**
 * Service配置类注解信息加载类
 * 该类对Service注解配置进行加载，并存入到全局配置信息中
 * <p>要在程序中得到Service配置，使用</p>
 * {@code XingCloudApplication.getInstance().getParameter(ServiceConfigLoader.SERVICE);}
 * 
 * @author tianwei
 */
@CloudBean("serviceClassConfigLoader")
public class ServiceClassConfigLoader extends AbstractClassConfigLoader {
	@SuppressWarnings("unchecked")
	public void load(){
		if(objects == null
			|| objects.isEmpty()){
			return;
		}
		//取得XingCloudApplication单例，该类含有全局配置信息
		XingCloudApplication application = XingCloudApplication.getInstance();		
		//bean的初始配置
		if (!application.hasParameter(ServiceConfig.SERVICE+"byApi")) {
			application.setParameter(ServiceConfig.SERVICE+"byApi",
					new HashMap<String, ServiceConfig>());
		}
		if(!application.hasParameter(ServiceConfig.SERVICE+"byId")) {
			application.setParameter(ServiceConfig.SERVICE+"byId", new HashMap<String, ServiceConfig>());
		}
		Class<?> clazz;
		CloudService annotation;
		for(Object object : objects.values()){
			clazz = object.getClass();
			annotation = clazz.getAnnotation(CloudService.class);
			ServiceConfig config = new ServiceConfig();
			config.setClassName(clazz.getName());
			String api = null;
			String id = null;
			if(annotation != null){
				api = annotation.api();
				id = annotation.value();
			}
			if(api == null
				|| api.length() == 0) {
				api = clazz.getSimpleName();
			}
			if(id == null
				|| id.length() == 0) {
				id = clazz.getSimpleName();
			}
			api = StringUtil.lowerCaseFirst(api);
			config.setApi(api);
			id = StringUtil.lowerCaseFirst(id);
			config.setId(id);
			((Map<String, ServiceConfig>) XingCloudApplication.getInstance()
					.getParameter(ServiceConfig.SERVICE+"byApi")).put(api, config);
			((Map<String, ServiceConfig>) XingCloudApplication.getInstance()
					.getParameter(ServiceConfig.SERVICE+"byId")).put(id, config);
		}
	}
}