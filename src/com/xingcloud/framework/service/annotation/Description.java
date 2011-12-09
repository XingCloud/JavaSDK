package com.xingcloud.framework.service.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * 对服务的方法描述
 * content:服务的描述信息
 * @Description(content = "用户注册服务，返回用户信息。参数要提供用户名和密码。")
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {
	String content();
}