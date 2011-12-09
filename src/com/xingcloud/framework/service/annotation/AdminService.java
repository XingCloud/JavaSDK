package com.xingcloud.framework.service.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * 标识提供GM功能的服务，如ItemAdminService.doAdd()。
 * 对于GM功能服务的请求，不对请求用户的信息进行验证。
 * @author wanglu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminService {

}
