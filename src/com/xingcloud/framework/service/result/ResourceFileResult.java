package com.xingcloud.framework.service.result;

import java.io.IOException;


/**
 * 封装返回资源文件的结果的类
 *
 */
public class ResourceFileResult extends FileResult {

	private static final long serialVersionUID = 1L;

	public ResourceFileResult(String fileUrl) throws IOException {
		super(fileUrl);
	}
	
	public ResourceFileResult(String fileUrl, boolean resultAsJSON) throws IOException {
		super(fileUrl, resultAsJSON);
	}
}