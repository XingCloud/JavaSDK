package com.xingcloud.framework.integration.http.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.xingcloud.framework.bean.BeanFactory;
import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.integration.http.protocol.HTTPProtocolFactory;
import com.xingcloud.framework.service.ServiceContext;
import com.xingcloud.framework.service.protocol.ServiceProtocol;
import com.xingcloud.util.string.Charset;

import flex.messaging.util.URLDecoder;

/**
 * 行云servlet
 * @author wanglu
 *
 */
public class XingCloudServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		start(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		start(request, response);
	}
	
	/**
	 * 行云servlet初始化方法
	 */
	@Override
	public void init() throws ServletException { 
		super.init();
		try {
			String path = getServletContext().getRealPath(File.separator);
			if(path == null 
				|| path.length() == 0){
				File file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toString());
				path = file.getParent().replaceAll("file\\:\\\\", "").replaceAll("[\\/]", File.separator);
				//打包为jar后游戏开发者使用jetty
				if (path.endsWith("lib")) {
					path = path.substring(0, path.length()-3-File.separator.length());
				}
			}else{
				path = path + "/WEB-INF";
			}
			
			//启动application实例
			XingCloudApplication application = XingCloudApplication.getInstance();
			application.setBasePath(URLDecoder.decode(path, Charset.UTF8));
			application.start();
			if(!application.hasParameter("security.service.provider")){
				application.setParameter("security.service.provider", "com.xingcloud.framework.security.OAuthAuthenticationProvider");
			}
		} catch (Exception e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
		}
	}
	
	public void start(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServiceProtocol serviceProtocol = new HTTPProtocolFactory().getProtocol(request, response);
		if(serviceProtocol == null){
			return;
		}
		ServiceContext serviceContext = null;
		try {
			serviceContext = (ServiceContext) BeanFactory.getInstance().getBean("serviceContext");
		} catch (Exception e1) {
			Logger.getLogger(getClass()).error(e1.getMessage(), e1);
		}
		serviceContext.setServiceProtocol(serviceProtocol);
		try {
			serviceContext.start();
		} catch (Exception e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
		} finally{
			serviceContext.close();
		}
	}
	
	public void destroy() {
		XingCloudApplication.getInstance().close();
	}
}