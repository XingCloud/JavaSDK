package com.xingcloud.framework.context.stream.input;

import java.io.IOException;
import java.util.Map;

import com.xingcloud.framework.security.Authentication;

public interface ResultInputStream<T> extends InputStream<T>, Authentication{
	Map<String, T> getParameters() throws IOException;
}