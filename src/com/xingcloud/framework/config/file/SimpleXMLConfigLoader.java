package com.xingcloud.framework.config.file;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.util.file.SimpleXMLLoader;

/**
 * 默认的XML文件读取类
 * <p>读取文件并将信息载入全局信息中</p>
 * <p>要在程序中读取这些信息，使用</p>
 * {@code XingCloudApplication.getInstance().getParameter(String key);}
 */

public class SimpleXMLConfigLoader extends AbstractFileConfigLoader {
	/**
	 * 具体的文件读取方法，读取这个配置文件并存入全局信息中
	 */
	public void load() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.getStream() == null) {
			File file = new File(this.getFilePath());
			if (file.exists()) {
				map = SimpleXMLLoader.load(this.getFilePath());
			} else {
				throw new Exception("file not found: " + this.getFilePath());
			}
		} else {
			map = SimpleXMLLoader.load(this.getStream());
		}

		for (Entry<String, Object> entry : map.entrySet()) {
			XingCloudApplication.getInstance().setParameter(entry.getKey(), entry.getValue());
		}
	}
}