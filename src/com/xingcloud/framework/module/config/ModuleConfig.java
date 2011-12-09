package com.xingcloud.framework.module.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.xingcloud.framework.bean.config.BeanConfig;
/**
 * 模块配置类，用于储存模块的信息
 */
public class ModuleConfig implements Serializable{

	private static final long serialVersionUID = 1L;
	public static final String MODULE = "module";
	
	protected String name;
	protected boolean required;
	protected List<String> packageName = new ArrayList<String>();
	protected String resource;
	protected String config;
	protected List<String> component = new ArrayList<String>();
	protected List<BeanConfig> listener = new ArrayList<BeanConfig>();
	protected List<String> dependency = new ArrayList<String>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public List<String> getPackageName() {
		return packageName;
	}
	public void setPackageName(List<String> packageName) {
		this.packageName = packageName;
	}
	public void addPackageName(String packageName) {
		this.packageName.add(packageName);
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public List<String> getComponent() {
		return component;
	}
	public void setComponent(List<String> component) {
		this.component = component;
	}
	public void addComponent(String component) {
		this.component.add(component);
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public List<BeanConfig> getListener() {
		return listener;
	}
	public void setListener(List<BeanConfig> listener) {
		this.listener = listener;
	}
	public void addListener(BeanConfig listener) {
		this.listener.add(listener);
	}
	public List<String> getDependency() {
		return dependency;
	}
	public void setDependency(List<String> dependency) {
		this.dependency = dependency;
	}
	public void addDependency(String dependency) {
		this.dependency.add(dependency);
	}
}