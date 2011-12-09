package com.xingcloud.framework.service.request;

public interface ServiceRequestAware{
	ServiceRequest getRequest();
	ServiceRequestAware setRequest(ServiceRequest request);
}