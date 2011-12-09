package com.xingcloud.framework.util.lock;

/**
 * 简单实现的可锁对象
 */
public class SimpleLockable implements Lockable {

	private static final long serialVersionUID = -5402489360150263243L;
	protected String lockId;
	
	public SimpleLockable(String lockId) {
		super();
		this.lockId = lockId;
	}

	/**
	 * 被锁定对象的唯一标识
	 * @return String
	 * @throws Exception
	 */
	@Override
	public String getLockId() throws Exception {
		return lockId;
	}

	/**
	 * 未实现
	 * @param locker
	 * @throws Exception
	 */
	@Override
	public void onLockConflicted(Locker locker) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 未实现
	 * @param locker
	 * @throws Exception
	 */
	@Override
	public void onLockReleased(Locker locker) throws Exception {
		// TODO Auto-generated method stub

	}

}
