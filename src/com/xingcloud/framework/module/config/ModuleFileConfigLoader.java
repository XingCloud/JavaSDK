package com.xingcloud.framework.module.config;

import java.util.HashMap;
import java.util.Map;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.xingcloud.framework.bean.config.BeanConfig;
import com.xingcloud.framework.config.AbstractConfigLoader;
import com.xingcloud.framework.context.application.XingCloudApplication;

/**
 * 读取sdk-description.xml文件的配置载入类，用于载入模块信息
 * 
 */
public class ModuleFileConfigLoader extends AbstractConfigLoader {
	/**
	 * 读取sdk-description.xml文件
	 */
	public void load() throws Exception {
		// 取得XingCloudApplication单例，该类含有全局配置信息
		XingCloudApplication application = XingCloudApplication.getInstance();
		// Action的初始配置
		if (!application.hasParameter(ModuleConfig.MODULE)) {
			application.setParameter(ModuleConfig.MODULE,
					new HashMap<String, ModuleConfig>());
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(getClass().getResourceAsStream(
				"/sdk-description.xml"));
		XPath xpath = XPathFactory.newInstance().newXPath();
		String exp = "sdk-description/version/modules/*";
		NodeList modules = (NodeList) xpath.evaluate(exp, document,
				XPathConstants.NODESET);
		if (modules != null && modules.getLength() > 0) {
			Node module;
			for (int i = 0; i < modules.getLength(); i++) {
				module = modules.item(i);
				if (module.getNodeType() == Node.ELEMENT_NODE
						&& module.getNodeName().equals("module")) {
					loadModule(module);
				}
			}
		}
		// 将选取的模块的初始化类加入XingCloudApplication的模块初始化容器中
	}
	
	/**
	 *	读取模块信息
	 * @param node
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void loadModule(Node node) throws Exception {
		// String name;
		ModuleConfig config = new ModuleConfig();
		XPath xpath = XPathFactory.newInstance().newXPath();
		/*
		 * 获取node的属性
		 */
		Node attribute;
		attribute = (Node) xpath.evaluate("@name", node, XPathConstants.NODE);
		if (attribute != null)
			config.setName(attribute.getNodeValue());
		attribute = (Node) xpath.evaluate("@required", node,
				XPathConstants.NODE);
		if (attribute != null && Boolean.valueOf(attribute.getNodeValue()))
			config.setRequired(true);
		attribute = (Node) xpath.evaluate("@choose", node, XPathConstants.NODE);
		if (attribute == null || Boolean.valueOf(attribute.getNodeValue()))
			config.setRequired(true);
		if (!config.isRequired())
			return;

		/*
		 * 获取node的子元素
		 */
		Node child;
		child = (Node) xpath.evaluate("package", node, XPathConstants.NODE);
		if (child != null) {
			String packageNames = child.getTextContent();
			String[] packageName = packageNames
					.split("(\\s*,\\s*|\\s*;\\s*|\\s+)");
			for (String pn : packageName) {
				config.addPackageName(pn);
			}
		}
		child = (Node) xpath.evaluate("resource", node, XPathConstants.NODE);
		if (child != null)
			config.setResource(child.getTextContent());
		child = (Node) xpath.evaluate("config", node, XPathConstants.NODE);
		if (child != null)
			config.setConfig(child.getTextContent());
		child = (Node) xpath.evaluate("dependency", node, XPathConstants.NODE);
		if (child != null) {
			String dependencyNames = child.getTextContent();
			String[] dependencyName = dependencyNames
					.split("(\\s*,\\s*|\\s*;\\s*|\\s+)");
			for (String dn : dependencyName) {
				config.addDependency(dn);
			}
		}
		child = (Node) xpath.evaluate("component", node, XPathConstants.NODE);
		if (child != null) {
			String componentNames = child.getTextContent();
			String[] componentName = componentNames
					.split("(\\s*,\\s*|\\s*;\\s*|\\s+)");
			for (String cn : componentName) {
				try{
					Class.forName(cn);
					config.addComponent(cn);
				}catch(Exception e){
					Logger.getLogger(getClass()).info(config.getName()+ " module does not exist. Skip.");
				}
			}
		}
		NodeList childlistener = (NodeList) xpath.evaluate("listener", node,
				XPathConstants.NODESET);
		if (childlistener != null && childlistener.getLength() > 0) {
			Node listener;
			for (int i = 0; i < childlistener.getLength(); i++) {
				listener = childlistener.item(i);
				BeanConfig listenerbean = BeanConfig.fromXML(listener);
				config.addListener(listenerbean);
			}
		}
		((Map<String, ModuleConfig>) XingCloudApplication.getInstance()
				.getParameter(ModuleConfig.MODULE)).put(config.getName(),
				config);
	}
}