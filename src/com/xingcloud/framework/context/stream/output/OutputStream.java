package com.xingcloud.framework.context.stream.output;

import java.io.IOException;
import java.io.Serializable;

public interface OutputStream<T extends Serializable> {
	void output(String rawData) throws IOException;
	void close();
}