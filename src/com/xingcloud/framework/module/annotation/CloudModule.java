package com.xingcloud.framework.module.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * 使用这个注解可以将类定义为一个module，value为bean的id，如果value为空则使用类名作为bean的id
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface CloudModule {
	String config() default "";
	String initiator() default "";
	String value() default "";
	String resource() default "";
}