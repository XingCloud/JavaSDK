package com.xingcloud.framework.util.lock;

import java.io.Serializable;

/**
 * 可被锁定的对象须实现这一接口
 */
public interface Lockable extends Serializable{
	/**
	 * 被锁定对象的唯一标识
	 * @return String
	 * @throws Exception
	 */
	String getLockId() throws Exception;
	
	/**
	 * 发生锁冲突时被调用。
	 * @param locker
	 * @throws Exception
	 */
	void onLockConflicted(Locker locker) throws Exception;
	
	/**
	 * 释放锁时被调用
	 * @param locker
	 * @throws Exception
	 */
	void onLockReleased(Locker locker) throws Exception;
}
