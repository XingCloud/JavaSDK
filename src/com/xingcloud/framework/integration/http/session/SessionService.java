package com.xingcloud.framework.integration.http.session;

import java.util.HashMap;
import java.util.Map;

import com.xingcloud.framework.cache.CacheServiceFactory;

/**
 * 
 * 对memcached客户端进行封装，用于实现分布式session。
 *
 */
public class SessionService {

	private static SessionService instance = null;

	protected int exp = 3600*2;

	/**
	 * 获得SessionService实例。
	 * @return SessionService
	 */
	public static synchronized SessionService getInstance() {
		if (instance == null) {
			instance = new SessionService();
		}
		return instance;
	}

	private SessionService() {}
	
	
	/**
	 * 根据session id，获取session的参数
	 * @param id
	 * @return Map<String, Object>
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSession(String id) {
		Map<String, Object> session = (Map<String, Object>) CacheServiceFactory.getCacheService().getClient().get(id);
		if(session == null){
			session = new HashMap<String, Object>();
			CacheServiceFactory.getCacheService().getClient().set(id, exp, session);
		}
		return session;
	}

	/**
	 * 保存对session信息的更新
	 * @param id
	 * @param session
	 */
	public void saveSession(String id, Map<String, Object> session) {
		CacheServiceFactory.getCacheService().getClient().replace(id, exp, session);
	}

	/**
	 * 删除session信息
	 * @param id
	 */
	public void removeSession(String id) {
		CacheServiceFactory.getCacheService().getClient().delete(id);
	}

	/**
	 * 关闭SessionService
	 * @param id
	 */
	protected void finalize() {
		CacheServiceFactory.getCacheService().getClient().shutdown();
	}

}
