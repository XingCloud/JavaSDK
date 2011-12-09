package com.xingcloud.framework.context.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * 
 * 属性注解,visible.equals("false")的时候该属性将不作为返回属性返回给用户
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Visible {

	String scope();

	String visible();

}
