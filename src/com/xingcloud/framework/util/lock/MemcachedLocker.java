package com.xingcloud.framework.util.lock;

import net.spy.memcached.MemcachedClient;

import com.xingcloud.framework.cache.CacheServiceFactory;

/**
 * 
 * 基于memcached实现分布式锁机制
 *
 */
public class MemcachedLocker implements Locker {
	protected int MAX_EXP = 60*10;
	protected MemcachedClient client = null;
	

	/**
	 * MemcachedLocker是否可以被使用。只有在memcached能被正确访问时才可以使用MemcachedLocker。
	 * @return boolean
	 */
	public static boolean isAvailable(){
		return CacheServiceFactory.getCacheService().isMemcachedAvailable();
	}
	
	/**
	 * 验证锁是否被释放。
	 * @param lockable
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean isLocked(Lockable lockable) throws Exception {
		return null != CacheServiceFactory.getCacheService().getClient().get(lockable.getLockId());
	}

	/**
	 * 对Lockable对象加锁，时间为defaultTime秒。
	 * @param lockable
	 * @param defaultTime
	 * @throws Exception
	 */
	@Override
	public void lock(Lockable lockable, int defaultTime) throws Exception {
		if(isLocked(lockable)){
			lockable.onLockConflicted(this);
			throw new Exception("conflicted");
		}
		
		boolean rlt = CacheServiceFactory.getCacheService().getClient().add(lockable.getLockId(), defaultTime, lockable).get();
		if(!rlt) throw new Exception("fail to add a lock to " + lockable);
	}

	/**
	 * 对Lockable对象加锁。
	 * @param lockable
	 * @throws Exception
	 */
	@Override
	public void lock(Lockable lockable) throws Exception {
		if(isLocked(lockable)){
			lockable.onLockConflicted(this);
			throw new Exception("conflicted");
		}
		
		boolean rlt = CacheServiceFactory.getCacheService().getClient().add(lockable.getLockId(), MAX_EXP, lockable).get();
		if(!rlt) throw new Exception("fail to add a lock to " + lockable);
	}

	/**
	 * 为Lockable对象释放锁。
	 * @param lockable
	 * @throws Exception
	 */
	@Override
	public void release(Lockable lockable) throws Exception {
		CacheServiceFactory.getCacheService().getClient().delete(lockable.getLockId());
		lockable.onLockReleased(this);
	}

}
