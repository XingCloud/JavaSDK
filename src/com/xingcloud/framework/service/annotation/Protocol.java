package com.xingcloud.framework.service.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用于标识服务使用的协议的注解
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Protocol {
	String allow();
	String deny();
}