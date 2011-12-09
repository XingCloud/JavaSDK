package com.xingcloud.framework.integration.http.protocol;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xingcloud.framework.service.protocol.ServiceProtocol;


/**
 * 协议工场的方法，通过识别请求的url识别请求的协议类型
 * 
 */

public class HTTPProtocolFactory {
	public HTTPProtocolFactory() {
	}
	/**
	 * 获取请求的协议
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public ServiceProtocol getProtocol(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HTTPProtocol serviceProtocol = null;
		String uri = request.getRequestURI()
				.replaceFirst(request.getContextPath(), "").replace("\\", "/")
				.replaceFirst("^/back", "");
		// rest服务
		if (uri.matches("^/rest(/\\w*)*")) {
			serviceProtocol = new RESTServiceProtocol();
			// amf传输协议
		} else if (uri.matches("^/amf(/\\w*)*")) {
			serviceProtocol = new AMFServiceProtocol();
			// 文件协议
		} else if (uri.matches("^/file(/\\w*)*")) {
			serviceProtocol = new FileServiceProtocol();
			// 状态协议
		} else if (uri.matches("^/status(/\\w*)*")) {
			serviceProtocol = new StatusServiceProtocol();
			// 查找服务的协议
		} else if (uri.matches("^/discovery(/\\w*)*")) {
			serviceProtocol = new DiscoveryServiceProtocol();
			// 调用admin服务的协议
		} else if (uri.matches("^/admin(/\\w*)*")) {
			serviceProtocol = new AdminServiceProtocol();
			// 调用test服务的协议
		} else if (uri.matches("^/test(/\\w*)*")) {
			serviceProtocol = new TestServiceProtocol();
		}
		if (serviceProtocol != null) {
			serviceProtocol.setUri(uri);
			serviceProtocol.setServletRequest(request);
			serviceProtocol.setServletResponse(response);
		}
		return (ServiceProtocol) serviceProtocol;
	}
}