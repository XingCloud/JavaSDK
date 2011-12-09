package com.xingcloud.framework.integration.spring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.module.config.ModuleConfig;
import com.xingcloud.util.string.StringUtil;

/**
 * spring扫描并注入容器时提供bean名称的生成器类
 * @author YQL
 *
 */
public class XingCloudBeanNameGenerator implements BeanNameGenerator {

	/**
	 * 生成bean名称的方法
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String generateBeanName(BeanDefinition bean,
			BeanDefinitionRegistry reg) {
		//得到准备注入的bean的类名，并得到简单类名，如果没有自定义名称，则使用该名字
		String beanClassName = bean.getBeanClassName();
		String beanClassSimpleName = beanClassName.substring(beanClassName
				.lastIndexOf(".") + 1);
		beanClassSimpleName = StringUtil.lowerCaseFirst(beanClassSimpleName);
		//从模块中寻找可以提供bean名称的潜在annotation
		Map<String, ModuleConfig> map = (Map<String, ModuleConfig>) XingCloudApplication
				.getInstance().getParameter(ModuleConfig.MODULE);
		if (map == null || map.isEmpty()) {
			return beanClassSimpleName;
		}
		// 遍历模块配置信息，寻找是否注册了组件注解
		for (ModuleConfig config : map.values()) {
			List<String> components = config.getComponent();
			if (components.isEmpty() || components.size() == 0) {
				continue;
			}
			// 如果注册了组件注解，则判断目标类是否声明该组件注解
			for (String className : components) {
				try {
					Class<? extends Annotation> annotation = (Class<? extends Annotation>) Class
							.forName(className);
					if (Class.forName(beanClassName).isAnnotationPresent(
							annotation)) {
						Method valueMethod = annotation.getMethod("value");
						String value = (String) valueMethod.invoke(Class
								.forName(beanClassName).getAnnotation(
										annotation));
						if (value.length() > 0) {
							//如果搜索到使用了组件注解且有非空的value值则返回这个value作为beam名称
							return value;
						}
					}
				} catch (Exception e) {
					return beanClassSimpleName;
				}
			}
		}
		return beanClassSimpleName;

	}
}
