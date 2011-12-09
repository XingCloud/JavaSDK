package com.xingcloud.framework.bean.config;

import java.util.HashMap;
import java.util.Map;

import com.xingcloud.framework.bean.annotation.CloudBean;
import com.xingcloud.framework.config.clazz.AbstractClassConfigLoader;
import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.util.string.StringUtil;

/**
 * Bean/POJO配置类注解信息加载类
 * 该类对Bean/POJO注解配置进行加载，并存入到全局配置信息中
 * <p>要在程序中得到Bean/POJO配置，使用</p>
 * {@code XingCloudApplication.getInstance().getParameter(BeanConfigLoader.BEAN);}
 *  
 * @author tianwei
 */
public class BeanClassConfigLoader extends AbstractClassConfigLoader {
	
	/**
	 * 从spring扫描到的类中加载添加了CloudBean注解的类到全局信息中
	 * <p>要在程序中得到Bean/POJO配置，使用</p>
	 * {@code XingCloudApplication.getInstance().getParameter(BeanConfigLoader.BEAN);}
	 */
	@SuppressWarnings("unchecked")
	public void load(){
		if(objects == null
			|| objects.isEmpty()){
			return;
		}
		//取得XingCloudApplication单例，该类含有全局配置信息
		XingCloudApplication application = XingCloudApplication.getInstance();		
		//bean的初始配置
		if (!application.hasParameter(BeanConfig.BEAN)) {
			application.setParameter(BeanConfig.BEAN,
					new HashMap<String, BeanConfig>());
		}
		Class<?> clazz;
		CloudBean annotation;
		for(Object object : objects.values()){
			clazz = object.getClass();
			annotation = clazz.getAnnotation(CloudBean.class);
			BeanConfig config = new BeanConfig();
			config.setClassName(clazz.getName());
			String id = null;
			if(annotation != null){
				id = annotation.value();
			}
			if(id == null
				|| id.length() == 0) {
				id = clazz.getSimpleName();
			}
			id = StringUtil.lowerCaseFirst(id);
			config.setId(id);
			((Map<String, BeanConfig>) XingCloudApplication.getInstance()
					.getParameter(BeanConfig.BEAN)).put(config.getId(), config);
		}
	}
}