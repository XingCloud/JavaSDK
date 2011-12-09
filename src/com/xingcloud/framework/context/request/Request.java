package com.xingcloud.framework.context.request;

import com.xingcloud.framework.context.ContextAware;
import com.xingcloud.framework.context.session.SessionAware;
import com.xingcloud.framework.context.state.StateContainer;


/**
 * 请求的基本接口
 * 
 */
public interface Request extends StateContainer, ContextAware, SessionAware{
	void close();
}