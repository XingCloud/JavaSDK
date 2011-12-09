package com.xingcloud.framework.util.stax;

import org.codehaus.staxmate.in.SMInputCursor;

import com.xingcloud.framework.context.Destroyable;

/**
 * 使用stax以游标的方式进行扫描的接口
 * 
 */
public interface StaxCursorVisitor extends Destroyable{
	void visit(SMInputCursor cursor) throws Exception;
}