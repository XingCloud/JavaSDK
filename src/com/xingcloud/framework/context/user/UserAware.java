package com.xingcloud.framework.context.user;


/**
 * 获取游戏用户需要实现的接口
 * 
 */
public interface UserAware<T extends User>{
	/**
	 * 获得游戏用户对象
	 */		
	T getUser();
	/**
	 * 注入游戏用户对象 
	 */		
	void setUser(T user);
}
