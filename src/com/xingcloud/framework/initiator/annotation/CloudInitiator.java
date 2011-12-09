package com.xingcloud.framework.initiator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 使用这个注解可以将类定义为一个bean，value为bean的id，如果value为空则使用类名作为bean的id
 * * <p>要在程序中得到Bean/POJO配置，使用</p>
 * {@code XingCloudApplication.getInstance().getParameter(BeanConfigLoader.BEAN);}
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CloudInitiator {
	String value() default "";
}