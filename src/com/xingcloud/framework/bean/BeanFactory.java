package com.xingcloud.framework.bean;

import java.util.Map;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.xingcloud.framework.bean.config.BeanConfig;
import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.context.exception.ServerException;
import com.xingcloud.util.string.StringUtil;
/**
 * Bean/POJO工厂类
 * 该类对Bean/POJO提供简单的工厂方法
 * <p>通过该方法可以得到一个bean实例，得到bean定义，向spring中注册一个无依赖的bean，为bean添加和删除别名</p>
 *  
 * @author tianwei
 */
public class BeanFactory {
	protected static final BeanFactory instance = new BeanFactory();
	protected ApplicationContext applicationContext = null;
	
	/**
	 * 通过该方法返回该工厂类的实例
	 * 
	 * @return BeanFactory
	 */
	public synchronized static BeanFactory getInstance(){
		return BeanFactory.instance;
	}
	
	protected BeanFactory(){
	}
	
	/**
	 * 返回srping的ApplicationContext类
	 * @return ApplicationContext
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 设置该工场的ApplicationContext类，工场使用该类从spring获取相应的bean信息
	 * @param applicationContext
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}	
	
	/**
	 * 使用id获得Bean/POJO配置
	 * @param String id 要获得的bean的beanid
	 * @return BeanConfig 返回一个bean的配置信息
	 * @throws Exception
	 */		
	public BeanConfig getBeanConfig(String id) throws Exception{
		String beanId = StringUtil.lowerCaseFirst(id);
		@SuppressWarnings("unchecked")
		Map<String, BeanConfig> map = (Map<String, BeanConfig>) XingCloudApplication.getInstance().getParameter("bean");
		if(map == null
			|| map.isEmpty()
			|| !map.containsKey(beanId)){
			throw new ServerException("bean id " + id + " has no config");
		}
		BeanConfig beanConfig = map.get(beanId);
		if(beanConfig == null
			|| beanConfig.getClassName()  == null){
			throw new ServerException("bean id " + id + " has no config");
		}
		return beanConfig;
	}
	
	/**
	 * 使用id获得Bean/POJO Class
	 * @param String id 要获得的bean的beanid
	 * @return Class 返回该bean的类名
	 * @throws Exception
	 */
	public Class<?> getBeanClass(String id) throws Exception{
		return applicationContext.getType(StringUtil.lowerCaseFirst(id));
	}
	
	/**
	 * 使用id获得Bean/POJO新的实例
	 * @param String id 要获得的bean的beanid
	 * @return Object 获得这个bean的一个实例
	 * @throws Exception
	 */	
	public Object getBean(String id) throws Exception{
		return applicationContext.getBean(StringUtil.lowerCaseFirst(id));
	}
	
	/**
	 * 使用Class获得Bean/POJO新的实例
	 * @param Class clazz 要获得的bean的类名
	 * @return Object 获得这个bean的一个实例
	 * @throws Exception
	 */	
	public Object getBean(Class<?> clazz) throws Exception{
		return applicationContext.getBean(clazz);
	}	
	

	/**
	 * 向spring容器中注册一个无依赖的bean
	 * @param beanId 要注册的bean的唯一标识
	 * @param registerClass 要注册为bean的类名
	 * @param allowOverride 如果容器中已经存在beanid，是否覆盖这个beanid指向的类
	 * @return 返回是否注册成功
	 */
	public boolean registerRootBean(String beanId, Class<?> registerClass, boolean allowOverride) {
		try{	
			DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)((AbstractApplicationContext)applicationContext).getBeanFactory();
			beanFactory.setAllowBeanDefinitionOverriding(allowOverride);
			beanFactory.registerBeanDefinition(beanId, BeanDefinitionBuilder.rootBeanDefinition(registerClass).getBeanDefinition());
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 对spring容器中的bean设置别名
	 * @param beanName  要设置别名的bean的名称
	 * @param beanAlias 给bean设置的别名
	 */
	public void setBeanAlias(String beanName, String beanAlias) {
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)((AbstractApplicationContext)applicationContext).getBeanFactory();
		beanFactory.registerAlias(beanName, beanAlias);
	}
	
	/**
	 * 删除spring容器中的bean的别名
	 * @param beanAlias 要删除的别名
	 */
	public void removeBeanAlias(String beanAlias) {
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)((AbstractApplicationContext)applicationContext).getBeanFactory();
		beanFactory.removeAlias(beanAlias);
	}
}