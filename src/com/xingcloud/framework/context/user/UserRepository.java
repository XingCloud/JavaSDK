package com.xingcloud.framework.context.user;

/**
 * 通过数据库会话和游戏用户UID获得游戏用户对象类的基本接口
 */
public interface UserRepository <T extends User>{
	
	public T get(String uid) throws Exception;
	public void put(T user) throws Exception;
}
