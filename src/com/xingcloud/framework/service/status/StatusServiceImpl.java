package com.xingcloud.framework.service.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;

import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.context.request.Request;
import com.xingcloud.framework.service.annotation.AdminService;
import com.xingcloud.framework.service.annotation.AdminStatus;
import com.xingcloud.framework.service.annotation.CloudService;
import com.xingcloud.framework.service.annotation.Description;
import com.xingcloud.framework.service.annotation.ServiceParam;
import com.xingcloud.framework.service.annotation.Status;
import com.xingcloud.framework.service.config.ServiceConfig;
import com.xingcloud.framework.service.exception.ServiceException;
import com.xingcloud.util.file.FileUtil;
import com.xingcloud.util.security.MD5;

/**
 * status服务
 * <p>调用这个服务可以收集标注了status注解的方法关联文件的版本信息</p>
 * <p>调用该方法检查一般方法的api是:服务器地址/项目名称/back/rest/status/status</p>
 * <p>调用该方法检查管理员方法的api是:服务器地址/项目名称/back/rest/status/admin</p>
 * <p>调用该方法需要传入一个lang参数标识语言信息，如cn</p>
 */
@CloudService(value="statusService", api="status")
public class StatusServiceImpl implements StatusService {

	public static final String PATH = new StringBuffer()
		.append(File.separator)
		.append("resource")
		.append(File.separator)
		.toString();
	
	/**
	 * 普通服务方法的status服务调用方法
	 */
	@Description(content="收集status方法关联文件的版本信息")
	@ServiceParam(name="lang",type="string",remark="语言名称，如en，cn等")			
	public Object status(Request req) throws Exception{
		return findStatus(Status.class, req);
	}
	
	/**
	 * GM模块的status服务调用方法
	 */
	@AdminService
	@Description(content="管理员收集status方法关联文件的版本信息")
	@ServiceParam(name="lang",type="string",remark="语言名称，如en，cn等")		
	public Object admin(Request req) throws Exception{
		return findStatus(AdminStatus.class, req);
	}
	
	private String getAnnotationFile(Method method, Class<? extends Annotation> annotationClass){
		if(Status.class.equals(annotationClass))
			return method.getAnnotation(Status.class).file();
		if(AdminStatus.class.equals(annotationClass))
			return method.getAnnotation(AdminStatus.class).file();
		return null;
	}
	
	/**
	 * 收集status方法关联文件的版本信息
	 * @param annotationClass
	 * @return
	 * @throws Exception
	 */
	protected Object findStatus(Class<? extends Annotation> annotationClass, Request req) throws Exception{
		if(!req.hasParameter("lang")){
			throw new ServiceException("param lang not valid");
		}
		String lang = req.getParameter("lang").toString();
		@SuppressWarnings("unchecked")
		Map<String, ServiceConfig> services = (Map<String, ServiceConfig>) XingCloudApplication.getInstance().getParameter("servicebyApi");
		if(services == null){
			return new HashMap<String, String>();
		}
		Map<String, Serializable> result = new HashMap<String, Serializable>();
		for(ServiceConfig serviceConfig : services.values()){
			Class<?> serviceClass = (Class<?>) Class.forName(serviceConfig.getClassName());
			Method[] methods = serviceClass.getMethods();
			Map<String, String> methodPathMap = new HashMap<String, String>();
			for(Method method : methods){
				if(!method.isAnnotationPresent(annotationClass)){
					continue;
				}
				//对符合条件的service方法，找到对应文件的path
				String path = new StringBuffer()
					.append(getAnnotationFile(method, annotationClass).replaceAll("#lang#", lang))
					.toString();
				methodPathMap.put(method.getName(), path);
			}
			if(methodPathMap.size() > 0){
				Map<String, Map<String, String>> api = loadAPI(methodPathMap);
				if(api.size() > 0){
					StringBuffer stringBuffer = new StringBuffer(serviceConfig.getApi());
					stringBuffer.setCharAt(0, Character.toLowerCase(stringBuffer.charAt(0)));
					result.put(stringBuffer.toString(), (Serializable) api);
				}
			}
			result.put("server_time",  System.currentTimeMillis() / 1000);
		}
		return result;
	}
	
	/**
	 * 对每个service方法，取出对应文件，记录文件的更新时间，计算文件内容的md5值
	 * @param methodPathMap
	 * @return
	 * @throws Exception
	 */
	protected Map<String, Map<String, String>> loadAPI(Map<String, String> methodPathMap) throws Exception {
		Map<String, Map<String, String>> api = new HashMap<String, Map<String, String>>();
		for(String method : methodPathMap.keySet()){
			String[] paths = methodPathMap.get(method).split(";");
			boolean hasPath = false;
			for(String anyPath : paths){
				String basePath = XingCloudApplication.getInstance().getBasePath();
				File file = new File(basePath + anyPath);
				Map<String, String> methodStatus = new HashMap<String, String>();
				byte[] bytes;
				if(file.exists()){
					hasPath = true;
					methodStatus.put("timestamp", new Long(file.lastModified()).toString());
					bytes = FileUtil.read(new FileInputStream(file));
				}else{
					URL url = getClass().getResource(anyPath);
					if(url == null){
						continue;
					}
					if(url.toString().startsWith("jar")){
						JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
						JarEntry jarEntry = jarURLConnection.getJarEntry();
						if(jarEntry == null){
							continue;
						}
						methodStatus.put("timestamp", new Long(jarEntry.getTime()).toString());
						bytes = FileUtil.read(getClass().getResourceAsStream(anyPath));
					}else{
						file = new File(url.toURI());
						if(!file.exists()){
							continue;
						}
						methodStatus.put("timestamp", new Long(file.lastModified()).toString());
						bytes = FileUtil.read(new FileInputStream(file));
					}						
				}
				//计算文件的MD5
				methodStatus.put("md5", MD5.encode(bytes));			
				
				StringBuffer methodName = new StringBuffer(method);
				if(methodName.toString().startsWith("do")){
					methodName.delete(0, 2);
				}
				methodName.setCharAt(0, Character.toLowerCase(methodName.charAt(0)));
				api.put(methodName.toString(), methodStatus);		
				if(hasPath){
					break;
				}
			}
		}
		return api;
	}
}