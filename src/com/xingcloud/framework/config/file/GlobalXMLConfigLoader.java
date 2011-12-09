package com.xingcloud.framework.config.file;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.util.file.SimpleXMLLoader;

/**
 * 读取global.xml配置文件的XML文件读取类
 * <p>
 * 读取配置文件并载入全局信息中
 * </p>
 * <p>
 * 要在程序中读取这些信息，使用
 * </p> {@code XingCloudApplication.getInstance().getParameter(String key);}
 */

public class GlobalXMLConfigLoader extends AbstractFileConfigLoader {
	/**
	 * 读取global.xml配置文件的具体读取方法
	 */
	public void load() throws Exception {
		File file = new File(this.getFilePath());
		if (file.exists()) {
			SimpleXMLLoader.setGLOBALXMLPATH(this.getFilePath());
		} else {
			throw new Exception("file not found: " + this.getFilePath());
		}
		Map<String, Object> map = SimpleXMLLoader.load(SimpleXMLLoader.getGLOBALXMLPATH());
		for (Entry<String, Object> entry : map.entrySet()) {
			XingCloudApplication.getInstance().setParameter(entry.getKey(), entry.getValue());
		}
	}
}