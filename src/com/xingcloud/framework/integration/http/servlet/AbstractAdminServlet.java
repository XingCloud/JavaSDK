package com.xingcloud.framework.integration.http.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.integration.http.protocol.AdminServiceProtocol;
import com.xingcloud.framework.security.AdminAuthenticationProvider;
import com.xingcloud.framework.security.AuthenticationException;
import com.xingcloud.framework.security.AuthenticationProvider;

/**
 * 
 * 自定义GM用户界面可扩展此类，提供了匹配redmine的用户验证功能。 
 * @author wanglu
 *
 */
public abstract class AbstractAdminServlet extends HttpServlet {

	private static final long serialVersionUID = -6387744730336112879L;

	@Override
	final protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp);
	}

	@Override
	final protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp);
	}
	
	final protected void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			authenticate(req, resp);
			doExecute(req, resp);
		} catch (AuthenticationException e) {
			Logger.getLogger(this.getClass()).error(e.getMessage(), e);
			onFailure(req, resp);
		}
	}
	
	/**
	 * 处理用户请求
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	abstract public void doExecute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
	
	/**
	 * 处理认证失败的请求
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	abstract public void onFailure(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
	
	/**
	 * 进行安全验证
	 * @param req
	 * @param resp
	 * @throws AuthenticationException
	 */
	final public void authenticate(HttpServletRequest req, HttpServletResponse resp) throws AuthenticationException{
		String config = (String)XingCloudApplication.getInstance().getParameter("security.admin.default");
		//如果security.admin.default的值是false则返回
		if (config == null || !config.equalsIgnoreCase("false")) {
			AdminServiceProtocol protocol = new AdminServiceProtocol();
			protocol.setServletRequest((HttpServletRequest) req);
			protocol.setServletResponse((HttpServletResponse) resp);
			AuthenticationProvider provider = new AdminAuthenticationProvider();
			provider.authenticate(protocol);
		}
	}
}
