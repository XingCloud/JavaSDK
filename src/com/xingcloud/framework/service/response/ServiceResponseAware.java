package com.xingcloud.framework.service.response;

public interface ServiceResponseAware{
	ServiceResponse getResponse();
	ServiceResponseAware setResponse(ServiceResponse response);
}