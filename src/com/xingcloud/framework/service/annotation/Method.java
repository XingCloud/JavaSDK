package com.xingcloud.framework.service.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用于标注服务使用http协议何种方法传入参数
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Method {
	String allow();
	String deny();
}