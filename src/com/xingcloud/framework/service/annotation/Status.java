package com.xingcloud.framework.service.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用于标识可以被status服务检测的方法
 * 详细内容请查看
 * @see com.xingcloud.framework.service.status.StatusServiceImpl
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Status {
	String file();
}