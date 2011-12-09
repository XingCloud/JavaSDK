package com.xingcloud.framework.service.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * 标识admin模块的status类型的服务，如ItemAdminService.doXml()
 * @author wanglu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminStatus {
	String file();
}