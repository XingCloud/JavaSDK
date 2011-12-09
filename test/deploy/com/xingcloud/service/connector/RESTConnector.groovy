package com.xingcloud.service.connector

import java.io.ByteArrayInputStream
import java.io.OutputStreamWriter
import java.net.URL
import java.util.zip.GZIPInputStream

import junit.framework.Assert

import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.HttpState
import org.apache.commons.httpclient.cookie.CookiePolicy
import org.apache.commons.httpclient.methods.PostMethod

import com.xingcloud.util.json.JSONUtil

public class RESTConnector implements IHttpConnector {

	private static def uri
	def verbose = true

	/**
	 * 配置RESTConnector所需使用的参数
	 */
	public void config(def params) {
		uri = params["test.deploy.uri"]
		Assert.assertNotNull(
				"The param(test.deploy.uri) doesn't exist in the file(cfg.properties)",
				uri)
		
		if (uri.charAt(uri.length() - 1) == '/') {
			uri = uri.substring(0, uri.length() - 1)
		}
	}
	
	/**
	 * 发送基于行云API的服务请求，并返回结果
	 * 
	 * @param api
	 *            符合行云规范的API
	 * @param req
	 *            请求参数
	 * @return 返回请求结果,Map类型
	 * @author xsy
	 */
	public def sendAPIRequest(def api, def req) {
		def relativeUrl = "/rest/" + api.replaceAll("\\.", "/")
		
		sendURLRequest(relativeUrl, req)
	}

	public def sendURLRequest(def relativeURL, def req) {
		def strRlt = new String(sendURLRequestAndGetBytes(relativeURL, req) ,"utf-8")
		def mapRlt = JSONUtil.toMap(strRlt)
		
		// **************************************************************
		if(verbose){
			println ''
			println "REQ :${relativeURL}\t${req}"
			println "RESP:${relativeURL}\t${mapRlt}"
		}
		// **************************************************************
		
		mapRlt
	}
	
	def sendAPIRequestWithHTTPClient(def api, def req, def cookies){
		def relativeURL = "/rest/" + api.replaceAll("\\.", "/")
		
		def initialState = new HttpState()
		if(cookies != null) cookies.each{initialState.addCookie(it)}
		def httpclient = new HttpClient();
		httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
		httpclient.setState(initialState);
		httpclient.getParams().setCookiePolicy(CookiePolicy.NETSCAPE)
		
		def url = uri
		if (relativeURL[0] == '/') url += relativeURL
		else url += '/' + relativeURL
		
		def post = new PostMethod(url)
		post.setRequestBody JSONUtil.toJSON(req)
		int result = httpclient.executeMethod(post);
		def newCookies = httpclient.getState().getCookies();
		[code:result, cookies:newCookies, result:JSONUtil.toMap(post.getResponseBodyAsString())]
	}

	public def sendURLRequestAndGetBytes(def relativeURL, def req) {
		def url = uri
		if (relativeURL.charAt(0) == '/') url += relativeURL
		else url += '/' + relativeURL

		def conn = new URL(url).openConnection()
		conn.doOutput = true
		def writer = new OutputStreamWriter(conn.outputStream)
		writer.write(JSONUtil.toJSON(req))
		writer.flush()
		writer.close()
		
		conn.inputStream.getBytes()
	}

	public def getFile(def relativeURL, def req) {
		def b = this.sendURLRequestAndGetBytes(relativeURL, req)
		def input = new GZIPInputStream(new ByteArrayInputStream(b))
		def content = new String(input.getBytes(), "utf-8")
		
		//******************************************
		if(verbose){
			println ''
			println "GET FILE:${req}"
		}
		//System.out.println("FILE    :" + content)
		//******************************************
		
		content
	}
	
	public def getConnectorType() {
		return 'rest'
	}
}
