package com.xingcloud.framework.context.session;

public interface SessionAware{
	Session getSession();
	SessionAware setSession(Session session);	
}