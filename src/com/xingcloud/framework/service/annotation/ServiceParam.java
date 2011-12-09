package com.xingcloud.framework.service.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * 
 * 对于服务传入参数的描述，方便Discovery所有服务时获取相关服务的传入参数
 * 
 * type:参数类型
 * name:参数名
 * remark:参数注解
 * 
 * 如果有多个参数则以,隔开
 * @ServiceParam(name = "username,password", type = "string,string", remark = "用户名,密码")
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceParam {
	String type();
	String name();
	String remark();
}