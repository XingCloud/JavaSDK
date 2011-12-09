package com.xingcloud.framework.context.session;

import com.xingcloud.framework.context.state.StateContainer;

/**
 * session基本接口
 * 
 */
public interface Session extends StateContainer{
	void close();
}