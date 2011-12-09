package com.xingcloud.framework.integration.http.servlet;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
 * 为自定义GM界面提供安全认证。开发者若选择不扩展AbstractAdminServlet类，而是自己编写新的servlet，须配置本类作为filter。
 * 
 * @author wanglu
 * 
 */
public class AdminAuthenticationFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
			ServletException {
		try {
			authenticate(req, resp);
			chain.doFilter(req, resp);
		} catch (AuthenticationException e) {
			Logger.getLogger(this.getClass()).error(e.getMessage(), e);
			((HttpServletResponse) resp).sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * 进行安全验证
	 * @param req
	 * @param resp
	 * @throws AuthenticationException
	 */
	final public void authenticate(ServletRequest req, ServletResponse resp) throws AuthenticationException{
		String config = (String) XingCloudApplication.getInstance().getParameter("security.admin.default");
		// 如果security.admin.default没有配置或者配置不是false则进行安全认证
		if (config == null || !config.equalsIgnoreCase("false")) {
			AuthenticationProvider provider = new AdminAuthenticationProvider();
			AdminServiceProtocol protocol = new AdminServiceProtocol();
			protocol.setServletRequest((HttpServletRequest) req);
			protocol.setServletResponse((HttpServletResponse) resp);
			provider.authenticate(protocol);
		}
	}
}
