package com.xingcloud.framework.integration.spring;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import com.xingcloud.framework.context.annotation.CloudInterface;
import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.module.annotation.CloudModule;
import com.xingcloud.framework.module.config.ModuleConfig;

/**
 * 利用spring实现类的加载，凡是应用CloudInterface或CloudModule注解，或者各模块的注解，需要载入到application context中。
 * @author wanglu
 *
 */
public class XingCloudTypeFilter implements TypeFilter {
	@Override
	public boolean match(MetadataReader metadataReader,
		MetadataReaderFactory metadataReaderFactory) throws IOException {
		//取得目标类的类型
		ClassMetadata metadata = metadataReader.getClassMetadata();
		Class<?> clazz = null;
		try {
			clazz = Class.forName(metadata.getClassName());
		} catch (Exception e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
			return false;
		}
		//如果目标类声明有CloudBean或CloudInterface或CloudModule或CloudService的注解信息，则返回true
		if(clazz.isAnnotationPresent(CloudInterface.class)
			|| clazz.isAnnotationPresent(CloudModule.class)){
			return true;
		}
		
		return isModuleClass(clazz);
	}
	
	/**
	 * 检查参数类是否应用类某一模块所对应的注解
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected boolean isModuleClass(Class<?> clazz){
		//取得模块配置信息
		Map<String, ModuleConfig> map = (Map<String, ModuleConfig>) XingCloudApplication.getInstance().getParameter(ModuleConfig.MODULE);
		if(map == null
			|| map.isEmpty()){
			return false;
		}
		//遍历模块配置信息，寻找是否注册了组件注解
		for(ModuleConfig config : map.values()){
			List<String> components = config.getComponent();
			if(components.isEmpty()
				|| components.size() == 0){
				continue;
			}
			//如果注册了组件注解，则判断目标类是否声明该组件注解
			for(String className : components){
				try {
					Class<? extends Annotation> annotation = (Class<? extends Annotation>) Class.forName(className);
					if(clazz.isAnnotationPresent(annotation)){
						return true;
					}
				} catch (Exception e) {
					Logger.getLogger(getClass()).error(e.getMessage(), e);
				}
			}
		}
		return false;
	}
}