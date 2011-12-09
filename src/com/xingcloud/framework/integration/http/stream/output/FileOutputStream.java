package com.xingcloud.framework.integration.http.stream.output;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletResponse;

import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.context.result.Result;
import com.xingcloud.framework.context.stream.output.ResultOutputStream;
import com.xingcloud.framework.service.result.FileResult;
import com.xingcloud.framework.service.result.NullServiceResult;
import com.xingcloud.framework.service.result.ResourceFileResult;

/**
 * 文件输出流类，如果结果返回结果是文件，这使用该类进行输出
 * 
 */
public class FileOutputStream extends HTTPOutputStream implements ResultOutputStream<Serializable>{
	public FileOutputStream(HttpServletResponse httpServletResponse) {
		super(httpServletResponse);
	}

	/**
	 * 输出结果
	 */
	public void output(Result<Serializable> result) throws Exception{
		if(result != null
			&& FileResult.class.isAssignableFrom(result.getClass())){
			output((FileResult) result);
		}
	}
	
	/**
	 * 输出结果
	 */
	public void output(FileResult result) throws Exception{
		//设定Servlet响应的类型
		httpServletResponse.setHeader("Content-Type", result.getContentType());
		String fileName = result.getFileName();
		if(fileName != null){
			httpServletResponse.setHeader("Content-Disposition", result.getContentDisposition() + "; filename=\"" + fileName + "\"");
		}else{
			httpServletResponse.setHeader("Content-Disposition", result.getContentDisposition());
		}
		InputStream inputStream = null;
		OutputStream outputStream = null;
		//设定文件的压缩格式
		if(isGZipRequired())
			outputStream = new DeflaterOutputStream(httpServletResponse.getOutputStream());
		else{
			outputStream = new GZIPOutputStream(httpServletResponse.getOutputStream());
			httpServletResponse.setHeader("Content-Encoding", "gzip");
		}
		//输出
		try{
			inputStream = getInputStream(result);
			if(inputStream != null){
				byte[] bytes = new byte[1024];
				int length = inputStream.read(bytes);
				while(length != -1){
					outputStream.write(bytes, 0, length);
					length = inputStream.read(bytes);
				}
			}
		}catch(Exception e){
			throw e;
		}finally{
			if(inputStream != null){
				inputStream.close();
			}
			if(outputStream != null){
				outputStream.flush();
				outputStream.close();
			}
		}
	}
	/**
	 * 获取输出文件的文件流
	 * @param result 要输出的文件类
	 * @return 输出文件的流
	 * @throws Exception
	 */
	private InputStream getInputStream(FileResult result) throws Exception{
		//获取文件类中的文件目录，查看有无备选路径，查找存在的文件，打开后以流的形式返回
		String basePath = XingCloudApplication.getInstance().getBasePath();
		List<String> fileUrls = new ArrayList<String>();
		fileUrls.add(result.getFileUrl());
		if(result.getAlternatives() != null){
			fileUrls.addAll(result.getAlternatives());
		}
		InputStream inputStream = null;
		File file = null;
		Boolean isResource = ResourceFileResult.class.isAssignableFrom(result.getClass());
		for(String fileUrl : fileUrls){
			file = new File(basePath + fileUrl);
			if(file.exists()){
				inputStream = new FileInputStream(file);
			}else if(isResource){
				inputStream = FileOutputStream.class.getResourceAsStream(fileUrl);
			}
			if(inputStream != null){
				return inputStream;
			}
		}
		return inputStream;
	}

	/**
	 * 输出结果
	 */
	public void output(NullServiceResult result) throws Exception {
		OutputStream outputStream = httpServletResponse.getOutputStream();
		outputStream.flush();
		outputStream.close();
	}
}