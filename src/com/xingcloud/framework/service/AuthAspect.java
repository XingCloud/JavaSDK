package com.xingcloud.framework.service;

import java.lang.reflect.Method;
import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.security.Authentication;
import com.xingcloud.framework.security.AuthenticationProvider;
import com.xingcloud.framework.service.annotation.Auth;
import com.xingcloud.framework.service.annotation.CloudService;
import com.xingcloud.framework.service.request.ServiceRequest;

/**
 * 安全验证切面类，在切入点提供进行安全验证的具体方法 具体的切面实现方法请查看spring关于aop的文档
 * 
 */
@Aspect
@Component
public class AuthAspect {

	/**
	 * 确定切入点和切入的方法
	 */
	@Pointcut("execution(* com.xingcloud.framework.service.ServiceContext.execute(..))")
	public void serviceExecution() {
	}

	/**
	 * 切入的安全验证
	 * 
	 * @param service
	 * @param method
	 * @param serviceRequest
	 * @throws Exception
	 */
	@Before("serviceExecution() && args(service, method, serviceRequest, ..)")
	public void checkAuthentication(Object service, Method method, ServiceRequest serviceRequest) throws Exception {
		// 如果注入对象或者方法为空，直接返回
		// 如果注入方法没有声明Auth注解，直接返回
		// 如果注入对象不是Service，直接返回
		// 如果没有配置provider，直接返回
		if (service == null || method == null || !method.isAnnotationPresent(Auth.class)
				|| !service.getClass().isAnnotationPresent(CloudService.class)
				|| !XingCloudApplication.getInstance().hasParameter("security.service.provider")) {
			return;
		}

		String serviceName = ((ServiceRequest) serviceRequest).getService() + "." + method.getName();

		if (!checkProvider(serviceName))
			return;

		// 实例化provider类，进行安全认证
		String providerClassName = (String) XingCloudApplication.getInstance()
				.getParameter("security.service.provider");
		AuthenticationProvider provider = (AuthenticationProvider) Class.forName(providerClassName).newInstance();
		if (service.getClass().isAnnotationPresent(CloudService.class)) {
			provider.authenticate((Authentication) (((ServiceContext) serviceRequest.getContext()).getServiceProtocol()));
		} else {
			provider.authenticate(service);
		}
	}

	/**
	 * 根据security.oauth.default开关和enabled,disabled名单确认是否验证
	 * 
	 * @param service
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected Boolean checkProvider(String service) {
		Boolean flag;
		Object obj;
		// 获得开关
		String config = (String) XingCloudApplication.getInstance().getParameter("security.oauth.default");
		// 如果明确定义开关为关则检验enable的情况
		if (config != null && config.equalsIgnoreCase("false")) {
			flag = false;
			obj = XingCloudApplication.getInstance().getParameter("security.oauth.service.enabled");
		// 其余情况均视为开关为开，则检查disable的情况
		} else {
			flag = true;
			obj = XingCloudApplication.getInstance().getParameter("security.oauth.service.disabled");
		}
		if(obj != null){
			//如果配置中有这个service则flag初始值反向
			if (obj.getClass().isAssignableFrom(String.class) && ((String) obj).equals(service)) {
				flag = flag.equals(false);
			} else if (List.class.isAssignableFrom(obj.getClass()) && ((List) obj).contains(service)) {
				flag = flag.equals(false);
			}			
		}
		return flag;
	}
}
