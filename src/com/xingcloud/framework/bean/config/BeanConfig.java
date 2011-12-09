package com.xingcloud.framework.bean.config;

import java.io.Serializable;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
/**
 * Bean/POJO配置类
 * 该类对Bean/POJO配置进行简单抽象
 * 可以对应Bean/POJO配置xml中的Bean/POJO条目 
 * <p>Bean/POJO至少包含id和className属性，其中id必须确保唯一，className必须为带有包名的正确类名</p>
 *  
 * @author tianwei
 */

public class BeanConfig implements Serializable{

	private static final long serialVersionUID = 1L;
	protected String id;
	protected String className;
	public static final String BEAN = "bean";
	
	/**
	 * 返回Bean/POJO id
	 * @return String
	 */	
	public String getId() {
		return id;
	}
	
	/**
	 * 注入Bean/POJO id
	 * @param String id Bean/POJO id
	 */		
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回Bean/POJO className
	 * @return String
	 */	
	public String getClassName() {
		return className;
	}
	
	/**
	 * 注入Bean/POJO className
	 * @param String className Bean/POJO className
	 */	
	public void setClassName(String className) {
		this.className = className;
	}
	
	/**
	 * 由XML节点生成BeanConfig
	 * <p>该XML节点必须包含名为id和class的属性，将分别映射为BeanConfig的id和className</p>
	 * 
	 * @param Node node XML节点
	 * @return BeanConfig
	 */
	public static BeanConfig fromXML(Node node){
		String name;
		BeanConfig config = new BeanConfig();
		NamedNodeMap attributes = node.getAttributes();
		int length = attributes.getLength();
		if(length > 0){
			for (int i = 0; i < attributes.getLength(); i++) {   
				Node attribute = attributes.item(i);  
				name = attribute.getNodeName();
				if(name.equalsIgnoreCase("id")){
					config.setId(attribute.getNodeValue());
				}else if(name.equalsIgnoreCase("class")){
					config.setClassName(attribute.getNodeValue());
				}
			}
		}
		return config;
	}
}