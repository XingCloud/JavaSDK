package com.xingcloud.framework.context.user;

/**
 * 游戏用户的基本接口
 * 
 */
public interface User{
	/**
	 * 获得平台地址
	 */	
	String getPlatformAddress();
	/**
	 * 设置平台地址
	 */	
	void setPlatformAddress(String platformAddress);
	
	/**
	 * 获得游戏用户UID
	 */	
	String getUid();	
	/**
	 * 设置游戏用户UID
	 */	
	void setUid(String uid);
}
