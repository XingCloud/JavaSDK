package com.xingcloud.framework.service.request;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xingcloud.framework.context.Context;
import com.xingcloud.framework.context.ContextAware;
import com.xingcloud.framework.context.exception.ClientException;
import com.xingcloud.framework.context.request.AbstractRequest;
import com.xingcloud.framework.context.session.Session;
import com.xingcloud.framework.context.session.SessionAware;
import com.xingcloud.framework.context.stream.input.ResultInputStream;
import com.xingcloud.util.json.JSONUtil;

/**
 * 服务请求封装类，封装了请求的内容，提供了调用请求参数和相关信息的方法
 * 
 */
public class ServiceRequest extends AbstractRequest{
	public static final Logger LOGGER = Logger.getLogger(ServiceRequest.class);
	
	public static final String INFO = "info";
	public static final String USERID = "gameUserId";
	public static final String PLATFORM_APP_ID = "platformAppId";
	public static final String PLATFORM_USER_ID = "platformUserId";
	
	protected String service;
	protected String method;
	protected ResultInputStream<Object> inputStream;
	protected String address;	//the request ip address
	protected Context context;
	protected Session session;
	
	/**
	 * 获得session
	 */
	@Override
	public Session getSession() {
		return this.session;
	}

	/**
	 * 注入session
	 */
	@Override
	public SessionAware setSession(Session session) {
		this.session = session;
		return this;
	}
	
	
	/**
	 * 获得当前请求的上下文
	 */
	@Override
	public Context getContext() {
		return this.context;
	}
	
	/**
	 * 注入上下文
	 */
	@Override
	public ContextAware setContext(Context context) {
		this.context = context;
		return this;
	}
	
	/**
	 * 获得请求的地址
	 * 
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 注入请求的地址
	 * 
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	public ServiceRequest(ResultInputStream<Object> inputStream) throws IOException {
		this.inputStream = inputStream;
	}
	
	/**
	 * 获得请求的方法
	 * 
	 */
	public String getMethod() {
		return method;
	}
	
	/**
	 * 获得请求的服务
	 * 
	 */
	public String getService(){
		return service;
	}
	
	/**
	 * 注入请求的方法
	 * @param method
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	
	/**
	 * 注入请求的服务
	 * @param service
	 */
	public void setService(String service) {
		this.service = service;
	}	
	
	/**
	 * 检查传入的参数是否存在platformAppId或者platform_uid参数
	 * @return
	 */
	public boolean hasPlatformUID()	{
		if(hasParameter("platform_uid")){
			return true;
		}
		return getInfo() != null
		&& getInfo().containsKey(PLATFORM_APP_ID);
	}
	
	/**
	 * 获得platformAppId或者platform_uid参数
	 * @return
	 * @throws ClientException
	 */
	public String getPlatformUID() throws ClientException{
		if(!hasPlatformUID()){
			throw new ClientException("invalid param " + PLATFORM_APP_ID + " in info");
		}
		if(hasParameter("platform_uid")){
			return getParameter("platform_uid").toString();
		}
		Map<String, Object> info = getInfo();
		if(info == null
			|| !info.containsKey(PLATFORM_APP_ID)
			|| info.get(PLATFORM_APP_ID) == null){
			return null;
		}
		return info.get(PLATFORM_APP_ID).toString();
	}
	
	/**
	 * 检查传入的参数是否存在platformUserId或者platform_user_uid参数
	 * @return
	 */
	public boolean hasPlatformUserUID()	{
		if(hasParameter("platform_user_uid")){
			return true;
		}
		return getInfo() != null
			&& getInfo().containsKey(PLATFORM_USER_ID);
	}
	
	/**
	 * 获得platformUserId或者platform_user_uid参数
	 * @return
	 * @throws ClientException
	 */
	public String getPlatformUserUID() throws ClientException{
		if(!hasPlatformUserUID()){
			throw new ClientException("invalid param " + PLATFORM_USER_ID + " in info");
		}		
		if(hasParameter("platform_user_uid")){
			return getParameter("platform_user_uid").toString();
		}
		Map<String, Object> info = getInfo();
		if(info == null
			|| !info.containsKey(PLATFORM_USER_ID)
			|| info.get(PLATFORM_USER_ID) == null){
			return null;
		}
		return info.get(PLATFORM_USER_ID).toString();
	}
	
	/**
	 * 检查传入的参数是否存在gameUserId或者user_uid参数
	 * @return
	 */
	public boolean hasUserUID()	{
		if(hasParameter("user_uid")){
			return true;
		}
		Map<String, Object> info = getInfo();
		return info != null
			&& (info.containsKey("userID")
				|| info.containsKey(USERID));
	}
	
	/**
	 * 获得gameUserId或者user_uid参数
	 * @return
	 * @throws ClientException
	 */
	public String getUserUID() throws ClientException{
		if(!hasUserUID()){
			throw new ClientException("invalid param " + USERID + " in info");
		}
		if(hasParameter("user_uid")){
			return getParameter("user_uid").toString();
		}		
		Map<String, Object> info = getInfo();
		if(info == null){
			return null;
		}
		if(info.containsKey("userID")
			&& info.get("userID") != null){
			return info.get("userID").toString();
		}
		if(info.containsKey(USERID)
			&& info.get(USERID) != null){
			return info.get(USERID).toString();
		}
		return null;
	}
	
	/**
	 * 获得PlatformAddress，PlatformAddress由PlatformUserUID+“_”+PlatformUID组成
	 * @return
	 * @throws ClientException
	 */
	public String getPlatformAddress() throws ClientException{
		return new StringBuffer()
			.append(getPlatformUserUID())
			.append("_")
			.append(getPlatformUID())
			.toString();
	}
	
	/**
	 * 获得参数中的id
	 * @return
	 */
	public String getId(){
		if(!parameters.containsKey("id")){
			return null;
		}
		return parameters.get("id").toString();
	}
	
	/**
	 * 获得参数中的info参数内容
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getInfo(){
		if(!parameters.containsKey("info")){
			return null;
		}
		return (Map<String, Object>) parameters.get("info");
	}
	
	/**
	 * 获得参数中的data参数内容
	 * @return
	 */
	public Object getData(){
		if(!parameters.containsKey("data")){
			return null;
		}
		return parameters.get("data");
	}
	
	/**
	 * 检查参数中是否存在以key为键的值
	 */
	public boolean hasParameter(String key) {
		if(getParameters() == null){
			return false;
		}
		return getParameters().containsKey(key);
	}
	
	/**
	 * 获取参数中以key为键的值
	 */
	public Object getParameter(String key) {
		if(hasParameter(key)){
			return getParameters().get(key);
		}
		return null;
	}

	/**
	 * 获取请求的参数
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getParameters(){
		return parameters!=null && parameters.containsKey("data") && parameters.get("data") != null && Map.class.isAssignableFrom(parameters.get("data").getClass())
			? (Map<String, Object>)parameters.get("data") : parameters;
	}
	
	/**
	 * 关闭请求，释放请求的资源
	 */
	public void close(){
		if(inputStream != null){
			inputStream.close();
			inputStream = null;
		}
		service = null;
		method = null;
	}
	
	/**
	 * 获取请求的输入流
	 * @return
	 */
	public ResultInputStream<Object> getInputStream(){
		return inputStream;
	}
	
	/**
	 * 将传入参数转化为json格式的字符串并返回
	 * @return
	 */
	public String toJSON(){
		try {
			return JSONUtil.toJSON(parameters);
		} catch (Exception e) {
			try {
				Logger.getLogger(getClass()).error("userId: " + this.getUserUID() + "\t" + e.getMessage(), e);
			} catch (Exception e2) {
				Logger.getLogger(getClass()).error(e.getMessage(), e);
			}
			return null;
		}
	}
	
}