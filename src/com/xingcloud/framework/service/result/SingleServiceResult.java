package com.xingcloud.framework.service.result;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.xingcloud.framework.context.exception.ClientException;
import com.xingcloud.framework.context.result.Result;


/**
 * 封装返回单个服务结果的类
 * 
 */
public class SingleServiceResult implements ServiceResult{	

	private static final long serialVersionUID = 1L;

	protected String id = null;

	protected Integer code = 200;	
	
	protected Object data = null;
	
	protected String message = "";
	
	/**
	 * 用于储存客户端异常信息的结果
	 * @param message
	 * @return
	 */
	public static SingleServiceResult clientError(String message){
		return new SingleServiceResult(400, new HashMap<String, Serializable>(), message);
	}
	
	/**
	 * 用于储存服务器端异常信息的结果
	 * @param message
	 * @return
	 */
	public static SingleServiceResult serverError(String message){
		return new SingleServiceResult(500, new HashMap<String, Serializable>(), message);
	}
	
	public static SingleServiceResult fromException(Exception e){
		Throwable cause = e.getCause();
		if(cause == null){
			return fromCause(e);
		}
		if(!Exception.class.isAssignableFrom(cause.getClass())){
			return fromCause(cause);
		}
		return fromException((Exception) cause);
	}
	
	private static SingleServiceResult fromCause(Throwable e){
		if(e instanceof ClientException){
			return new SingleServiceResult(400, new HashMap<String, Object>(), e.getMessage());
		}
		return new SingleServiceResult(500, new HashMap<String, Object>(), e.getMessage());
	}
	
	/**
	 * 用于储存服务执行成功返回信息的结果
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static SingleServiceResult success(Object data){
		if(data == null){
			return new SingleServiceResult(200, null, null);
		}
		if(Result.class.isAssignableFrom(data.getClass())){
			return SingleServiceResult.success((Result<Serializable>) data);
		}
		return new SingleServiceResult(data);
	}
	
	/**
	 * 用于储存服务执行成功返回信息的结果
	 * @param data
	 * @return
	 */
	public static SingleServiceResult success(Result<Serializable> data){
		if(data == null){
			return new SingleServiceResult(200, null, "");
		}
		return new SingleServiceResult((Serializable) data.asResult());
	}
	
	public SingleServiceResult(Integer code, Serializable data, String message){
		this.code = code;
		this.data = data;
		this.message = message;
	}
	
	public SingleServiceResult(Integer code, Result<Serializable> data, String message){
		this.code = code;
		this.message = message;
		if(data != null){
			this.data = (Serializable) data.asResult();
		}
	}
	
	public SingleServiceResult(Object data){
		this.data = data;
	}
	
	public SingleServiceResult(Result<Serializable> data){
		this.data = (Serializable) data.asResult();
	}
	
	/**
	 * 得到请求的id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 请求的id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 注入data
	 * @param data
	 */
	public void setData(Object data) {
		this.data = data;
	}	
	
	/**
	 * 得到code
	 * @return
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * 得到data
	 * @return
	 */
	public Object getData() {
		return data;
	}

	/**
	 * 得到message
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 设置code
	 * @param code
	 */
	public void setCode(Integer code) {
		this.code = code;
	}

	/**
	 * 设置data
	 * @param data
	 */
	public void setData(Serializable data) {
		this.data = data;
	}

	/**
	 * 设置data
	 * @param data
	 */
	public void setData(Result<Serializable> data) {
		this.data = data.asResult();
	}
	
	/**
	 * 设置message
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * 将结果序列化并返回
	 */
	@SuppressWarnings("unchecked")
	public Serializable asResult(){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", id);
		parameters.put("code", code);
		parameters.put("message", message);
		if(data == null){
			parameters.put("data", null);
		}else if(Result.class.isAssignableFrom(data.getClass())){
			parameters.put("data", ((Result<Serializable>) data).asResult());
		}else {
			parameters.put("data", data);
		}
		return (Serializable) parameters;
	}
}