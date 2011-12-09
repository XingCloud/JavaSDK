package com.xingcloud.framework.service.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用于安全验证标识的注解
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {
	String type();
}