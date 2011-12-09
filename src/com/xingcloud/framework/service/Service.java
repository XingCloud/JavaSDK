package com.xingcloud.framework.service;

import com.xingcloud.framework.context.ContextAware;
import com.xingcloud.framework.context.application.ApplicationAware;
import com.xingcloud.framework.service.request.ServiceRequestAware;
import com.xingcloud.framework.service.response.ServiceResponseAware;

/**
 * 服务接口，用户自定义的服务
 * <p>在1.3版本以后，为了简化用户自定义service，我们已经废弃了这个接口，我们将使用注解的方式和传递参数的方式来提供参数</p>
 * 在新版本中为了简化自定义服务的限制没有实现该接口
 * 用户自定义服务需要在类前面标注CloudService注解，并配置该服务的beanid和调用的api
 * 自定义服务示例如下：
 * 
 * <p>@CloudService(value="helloWordService" api="example.hellword")</p>
 * <p>public class HelloWorldService {</p>
 * 			<p>public void doSay() {</p>
 * <p/>			
 * 			<p>	}</p>
 * <p>}</p>
 * <p>}</p>
 * <p>则通过调用URL：http:\\127.0.0.1:8080\game\back\rest\example\hellword\say可以调用该服务，具体的写法可以参照文档</p>
 * 文档地址：http://doc.xingcloud.com/pages/viewpage.action?pageId=3408110#JavaWeb%E6%B8%B8%E6%88%8F%E5%BC%95%E6%93%8E-%E5%BF%AB%E9%80%9F%E5%BC%80%E5%8F%91
 * @deprecated
 */
public interface Service extends  ContextAware, ServiceRequestAware, ApplicationAware, ServiceResponseAware{
	void close();
}