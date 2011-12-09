package com.xingcloud.service.connector;

import java.util.Map;

public interface IConnector {

	/**
	 * 配置连接参数
	 * @param params 连接参数
	 */
	public void config(def params)
	/**
	 * 发送基于行云API的服务请求，并返回结果
	 * 
	 * @param api
	 *            符合行云规范的API
	 * @param req
	 *            请求参数
	 * @return 返回请求结果
	 * @author xsy
	 */
	public def sendAPIRequest(def api, def req)
	
	/**
	 * 返回连接器的类型
	 */
	public def getConnectorType()
}
