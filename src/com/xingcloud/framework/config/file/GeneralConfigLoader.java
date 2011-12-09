package com.xingcloud.framework.config.file;

import java.io.File;
import java.util.Map;

import javax.xml.stream.events.StartElement;

import org.apache.log4j.Logger;

import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.util.stax.StaxAttributeMapVisitor;
import com.xingcloud.framework.util.stax.StaxVisitable;
import com.xingcloud.util.string.Charset;

import flex.messaging.util.URLDecoder;

/**
 * 先载入config.xml，然后根据其中内容载入其它的配置文件
 * @author luotianwei
 * @deprecated 本版本不再支持配置文件的形式读取服务等信息
 *
 */
public class GeneralConfigLoader extends AbstractFileConfigLoader{	
	/**
	 * 读取config.xml文件，然后根据每个配置项读取相应的文件并加载
	 * @deprecated 本版本不再支持配置文件的形式读取服务等信息
	 */
	public void load() throws Exception{
		if(!new File(getFilePath()).exists())
			throw new Exception("cannot find the configuration file: " + getFilePath());
		
		StaxVisitable visitable = new StaxVisitable(getFilePath());
		visitable.accept(new StaxAttributeMapVisitor(){
			public void visit(StartElement element, Map<String, String> map) throws Exception{
				if(!map.containsKey("fileName")){
					return;
				}
				String fileName = map.get("fileName");
				String filePath;
				if(fileName.indexOf("/") == 0){
					filePath = URLDecoder.decode(new StringBuffer()
						.append(XingCloudApplication.getInstance().getBasePath())
						.append(fileName)
						.toString(), Charset.UTF8);
				}else{
					filePath = URLDecoder.decode(new StringBuffer()
						.append(XingCloudApplication.getInstance().getBasePath())
						.append(File.separator)
						.append("config")
						.append(File.separator)
						.append(fileName)
						.toString(), Charset.UTF8);
				}
				String loaderClassName = map.get("loader");
				try {
					String name = element.getName().toString();
					FileConfigLoader configLoader = null;
					if(loaderClassName != null){
						configLoader = (FileConfigLoader) Class.forName(loaderClassName).newInstance();
					}else if(name.equalsIgnoreCase("xml")){
						configLoader = new SimpleXMLConfigLoader();
					}else if(name.equalsIgnoreCase("ini")
						|| name.equalsIgnoreCase("properties")){
						configLoader = new SimpleINIConfigLoader();
					}
					if(configLoader != null){
						configLoader.setFilePath(filePath);
						configLoader.load();
					}
				} catch (Exception e) {
					Logger.getLogger(this.getClass()).error(e.getMessage(), e);
				}
			}
		});
	}
}