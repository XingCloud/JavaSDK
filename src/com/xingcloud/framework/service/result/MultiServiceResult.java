package com.xingcloud.framework.service.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.xingcloud.framework.context.result.Result;


/**
 * 封装返回多个服务结果的类
 * 
 */
public class MultiServiceResult implements ServiceResult{

	private static final long serialVersionUID = 1L;
	protected String id;
	protected Integer code = 200;
	protected String message;

	public static final String ID = "id";
	public static final String CODE = "code";
	public static final String RESULTS = "data";
	
	protected List<Result<Serializable>> results = new ArrayList<Result<Serializable>>();

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Result<Serializable>> getResults() {
		return results;
	}
	
	public void setResults(List<Result<Serializable>> results) {
		this.results = results;
	}
	
	public void addResult(Result<Serializable> result) {
		results.add(result);
	}
	
	public Serializable asResult() {
		Map<String, Serializable> parameters = new HashMap<String, Serializable>();
		parameters.put(ID, id);
		parameters.put(CODE, code);
		List<Serializable> list = new ArrayList<Serializable>();
		Iterator<Result<Serializable>> iterator = results.iterator();
		while(iterator.hasNext()){
			list.add(iterator.next().asResult());
		}
		parameters.put(RESULTS, (Serializable) list);
		return (Serializable) parameters;
	}
}