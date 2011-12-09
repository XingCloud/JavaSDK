package com.xingcloud.service.connector

import java.util.Map

public interface IHttpConnector extends IConnector {
	/**
	 * 发送包含相对地址的服务请求，并返回结果
	 * 
	 * @param relativeURL
	 *            相对地址
	 * @param req
	 *            请求参数
	 * @return 返回请求结果
	 * @author xsy
	 */
	public def sendURLRequest(def relativeURL, def req)
	/**
	 * 发送包含相对地址的服务请求，并返回字节数组的请求结果
	 * 
	 * @param relativeURL
	 *            相对地址
	 * @param req
	 *            请求参数
	 * @return 返回字节数组的请求结果
	 * @author xsy
	 */
	public def sendURLRequestAndGetBytes(def relativeURL, def req)
	/**
	 * 请求服务器文件
	 * 
	 * @param relativeURL
	 *            文件在服务器上的相对地址
	 * @param req
	 *            请求参数
	 * @return 返回文件包含的字符串
	 * @author xsy
	 */
	public def getFile(def relativeURL, def req)
}
