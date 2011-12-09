package com.xingcloud.framework.util.lock;

/**
 * 锁接口
 */
public interface Locker {
	/**
	 * 验证锁是否被释放。
	 * @param lockable
	 * @return
	 * @throws Exception
	 */
	boolean isLocked(Lockable lockable) throws Exception;
	
	/**
	 * 对Lockable对象加锁，时间为defaultTime秒。
	 * @param lockable
	 * @param defaultTime
	 * @throws Exception
	 */
	void lock(Lockable lockable, int defaultTime) throws Exception;
	
	/**
	 * 对Lockable对象加锁。
	 * @param lockable
	 * @throws Exception
	 */
	void lock(Lockable lockable) throws Exception;
	
	/**
	 * 为Lockable对象释放锁。
	 * @param lockable
	 * @throws Exception
	 */
	void release(Lockable lockable) throws Exception;
}
