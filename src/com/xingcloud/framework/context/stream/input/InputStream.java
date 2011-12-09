package com.xingcloud.framework.context.stream.input;

import java.io.IOException;

public interface InputStream<T> {
	String getRawData() throws IOException;
	void close();
}