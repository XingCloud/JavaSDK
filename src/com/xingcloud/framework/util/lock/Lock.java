package com.xingcloud.framework.util.lock;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 锁机制入口，默认使用基于memcached实现的分布式锁
 *
 */
public class Lock {
	private static Lock instance = new Lock();
	
	protected Map<String, Locker> lockers = null;
	public static final String MEMCACHED_LOCK = "memcached";
	
	protected Lock(){
		
	}
	
	/**
	 * 获取所有可被使用的锁机制
	 * @return
	 */
	public Map<String, Locker> getLockers(){
		if(lockers != null) return lockers;
		
		//由于spring在启动之初检查所有的类，此时memcached配置尚未被载入，所以lockers不应在构造函数中被初始化。
		lockers = new HashMap<String, Locker>();
		if(MemcachedLocker.isAvailable())
			lockers.put(MEMCACHED_LOCK, new MemcachedLocker());
		return lockers;
	}
	
	/**
	 * 获取Lock实例。
	 * @return Lock
	 */
	public static Lock getInstance(){
		if(instance == null) instance = new Lock();
		return instance;
	}
	
	/**
	 * 获取锁对象。
	 * @return Lock
	 */
	public static Locker get(String region){
		return region == null ? getInstance().getLockers().get(MEMCACHED_LOCK) : getInstance().getLockers().get(region);
	}
	
	/**
	 * 获取锁对象。
	 * @return Lock
	 */
	public static Locker get(){
		return get(null);
	}
}
