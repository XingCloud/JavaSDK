package com.xingcloud.framework.config.file;

import java.util.Map;
import java.util.Map.Entry;

import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.util.file.SimpleINILoader;

/**
 * 默认的ini后缀名的配置文件读取类，读取这个配置文件并存入全局信息中
 * <p>要在程序中读取这些信息，使用</p>
 * {@code XingCloudApplication.getInstance().getParameter(String key);}
 */
public class SimpleINIConfigLoader extends AbstractFileConfigLoader {
	
	/**
	 * 具体的文件读取方法，读取这个配置文件并存入全局信息中
	 */
	public void load() throws Exception {
		Map<String, Object> map = SimpleINILoader.load(this.getFilePath());
		for (Entry<String, Object> entry : map.entrySet()) {
			XingCloudApplication.getInstance().setParameter(entry.getKey(), entry.getValue());
		}
	}
}