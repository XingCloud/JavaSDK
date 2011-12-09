package com.xingcloud.framework.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.xingcloud.framework.bean.BeanFactory;
import com.xingcloud.framework.context.Context;
import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.context.exception.ServerException;
import com.xingcloud.framework.event.XingCloudEventPublisher;
import com.xingcloud.framework.service.config.ServiceConfig;
import com.xingcloud.framework.service.event.ServiceErrorEvent;
import com.xingcloud.framework.service.event.ServiceFinishedEvent;
import com.xingcloud.framework.service.event.ServiceStartedEvent;
import com.xingcloud.framework.service.exception.ServiceException;
import com.xingcloud.framework.service.protocol.ServiceProtocol;
import com.xingcloud.framework.service.request.ServiceRequest;
import com.xingcloud.framework.service.response.ServiceResponse;
import com.xingcloud.framework.service.result.NullServiceResult;
import com.xingcloud.framework.service.result.ServiceResult;
import com.xingcloud.framework.service.result.SingleServiceResult;
import com.xingcloud.util.json.JSONUtil;
import com.xingcloud.util.string.StringUtil;

/**
 * 服务调用的上下文
 * 
 * @author luotianwei
 * 
 */
@Scope("prototype")
@Component
public class ServiceContext implements Context {
	public static final Logger LOGGER = Logger.getLogger(ServiceContext.class);
	protected ServiceProtocol serviceProtocol;

	public ServiceProtocol getServiceProtocol() {
		return serviceProtocol;
	}

	public void setServiceProtocol(ServiceProtocol serviceProtocol) {
		this.serviceProtocol = serviceProtocol;
	}

	/**
	 * 查找service方法
	 * 
	 * @param serviceClass
	 * @param methodName
	 * @return
	 * @throws ServiceException
	 */
	protected Method findServiceMethod(Class<?> serviceClass, String methodName)
			throws ServiceException {
		Method[] serviceMethods = serviceClass.getMethods();
		Method correspondMethod = null;
		for (Method serviceMethod : serviceMethods) {
			if (serviceMethod.getName().equals(methodName)) {
				if(correspondMethod != null)
					throw new ServiceException(
							ServiceException.SERVICE_METHOD_DUPLICATE,
							"found two or more" + methodName + "in"
									+ serviceClass.getSimpleName());
					
				correspondMethod = serviceMethod;
			}
		}
		if (correspondMethod != null) {
			return correspondMethod;
		} else {
			throw new ServiceException(
					ServiceException.SERVICE_METHOD_NOT_FOUND,
					"not found the method: " + methodName);
		}

	}

	/**
	 * 执行service方法
	 * 
	 * @param service
	 * @param method
	 * @param methodParameters
	 *            参数list
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public ServiceResult execute(Object service, Method method,
			ServiceRequest serviceRequest, ServiceResponse serviceResponse)
			throws Exception {
		// 本处方法提供多个参数，方便aop传入参数
		Map<Class, Object> methodParameters = new HashMap<Class, Object>();
		methodParameters.put(serviceRequest.getClass(), serviceRequest);
		methodParameters.put(serviceResponse.getClass(), serviceResponse);

		try {
			// 查找这个method需要的参数，从参数list中查找相应内容
			Class<?>[] methodParams = method.getParameterTypes();
			Object rlt = null;
			if (methodParams.length > 0) {
				Object[] injectParams = new Object[methodParams.length];
				for (int i = 0; i < methodParams.length; i++) {
					for (Class parameter : methodParameters.keySet()) {
						if (methodParams[i].isAssignableFrom(parameter)) {
							injectParams[i] = methodParameters.get(parameter);
							break;
						}
					}
				}
				rlt = method.invoke(service, injectParams);
			} else {
				rlt = method.invoke(service);
			}
			return (ServiceResult) (ServiceResult.class.isInstance(rlt) ? rlt
					: SingleServiceResult.success(rlt));
		} catch (InvocationTargetException e) {
			if (!ServiceException.class.isInstance(e.getCause()))
				throw e;
			return new SingleServiceResult(
					((ServiceException) e.getCause()).getCode(),
					new HashMap<String, Object>(), e.getCause().getMessage());
		}
	}

	/**
	 * 设置serviceRespon的seresult，并把result返回
	 */
	public ServiceResult executeContext(Object service, Method method,
			ServiceRequest serviceRequest, ServiceResponse serviceResponse)
			throws Exception {
		XingCloudEventPublisher.getInstance().publishEvent(
				new ServiceStartedEvent(serviceRequest));
		ServiceResult result = null;
		result = execute(service, method, serviceRequest, serviceResponse);
		if (result != null) {
			result.setId(serviceRequest.getId());
		}
		serviceResponse.setResult(result);

		XingCloudEventPublisher.getInstance().publishEvent(
				new ServiceFinishedEvent(serviceRequest));
		return result;
	}

	/**
	 * 调用服务
	 */

	public void start() throws Exception {
		ServiceRequest serviceRequest = null;
		ServiceResponse serviceResponse = null;
		try {
			serviceRequest = serviceProtocol.getRequest();
			if (serviceRequest == null) {
				throw new ServerException("service request not inited");
			}
			serviceRequest.setContext(this);
			try {
				serviceRequest.setSession(serviceProtocol.getSession());
			} catch (Exception e) {
				serviceRequest.setSession(null);
			}

			serviceResponse = serviceProtocol.getResponse();
			if (serviceResponse == null) {
				throw new ServerException("service response not inited");
			}

			if (!XingCloudApplication.getInstance()
					.hasParameter("servicebyApi")) {
				throw new ServerException("service config not exists");
			}
			String serviceClassName = StringUtil.lowerCaseFirst(serviceRequest
					.getService());
			if (serviceClassName == null) {
				serviceResponse.setResult((NullServiceResult) NullServiceResult
						.getInstance());
				serviceResponse.output();
				serviceRequest.close();
				serviceResponse.close();
				return;
			}
			@SuppressWarnings("unchecked")
			Map<String, ServiceConfig> serviceConfigMap = (Map<String, ServiceConfig>) XingCloudApplication
					.getInstance().getParameter("servicebyApi");
			if (serviceConfigMap == null || serviceConfigMap.isEmpty()) {
				throw new ServerException("service config not exists");
			}
			ServiceConfig serviceConfig = (ServiceConfig) serviceConfigMap
					.get(serviceClassName);
			if (serviceConfig == null) {
				throw new ServerException("service " + serviceClassName
						+ " not exists");
			}

			Class<?> serviceClass = (Class<?>) Class.forName(serviceConfig
					.getClassName());
			Method method = findServiceMethod(serviceClass,
					serviceRequest.getMethod());

			Object service = BeanFactory.getInstance().getBean(
					serviceConfig.getId());
			LOGGER.info("service: " + serviceRequest.getService()
					+ "\tmethod: " + serviceRequest.getMethod() + "\tparams: "
					+ serviceRequest.toJSON());
			ServiceResult result = null;
			try {
				int slowRequestWarningTime = Integer
						.parseInt(XingCloudApplication.getInstance()
								.getParameter("service.slow_request_warning")
								.toString());
				if (slowRequestWarningTime <= 0) {
					result = executeContext(service, method, serviceRequest,
							serviceResponse);
				} else {
					Date begin = new Date();
					result = executeContext(service, method, serviceRequest,
							serviceResponse);
					Date end = new Date();
					if (end.getTime() - begin.getTime() > slowRequestWarningTime * 1000) {
						LOGGER.warn("service:" + serviceRequest.getService()
								+ " takes " + (end.getTime() - begin.getTime())
								+ " milliseconds");
					}
				}

			} catch (Exception e) {
				result = SingleServiceResult.fromException(e);
				result.setId(serviceRequest.getId());
				serviceResponse.setResult(result);
				try {
					LOGGER.error("UserUID: " + serviceRequest.getUserUID()
							+ "   " + e.getMessage(), e);
				} catch (Exception e1) {
					LOGGER.error(e.getMessage(), e);
				}
				XingCloudEventPublisher.getInstance().publishEvent(
						new ServiceErrorEvent(serviceRequest,e));
			} finally {
				if (result != null) {
					try {
						LOGGER.info("UserUID: " + serviceRequest.getUserUID()
								+ "\tservice: " + serviceRequest.getService()
								+ "\tmethod: " + serviceRequest.getMethod()
								+ "\tresult: "
								+ JSONUtil.toJSON(result.asResult()));
					} catch (Exception e) {
						LOGGER.info("\tservice: " + serviceRequest.getService()
								+ "\tmethod: " + serviceRequest.getMethod()
								+ "\tresult: "
								+ JSONUtil.toJSON(result.asResult()));
					}
				} else {
					LOGGER.info("service: " + serviceRequest.getService()
							+ "\tmethod: " + serviceRequest.getMethod());
				}
				serviceResponse.output();
				serviceResponse.close();
				serviceRequest.close();
			}
		} catch (Exception e) {
			try {
				LOGGER.error("UserUID: " + serviceRequest.getUserUID() + "   "
						+ e.getMessage(), e);
			} catch (Exception e1) {
				LOGGER.error(e.getMessage(), e);
			}
			throw e;
		}
	}

	public void close() {
		serviceProtocol.close();
		serviceProtocol = null;
	}
}