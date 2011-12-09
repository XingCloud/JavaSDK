package com.xingcloud.framework.service.result;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 封装服务结果为文件的类，包含了文件的相关信息
 * 
 */
public class FileResult implements ServiceResult {

	private static final long serialVersionUID = 1L;
	protected String id = null;
	protected String fileUrl;
	protected String fileName;
	protected String contentType;
	protected String contentDisposition = "inline";
	protected boolean resultAsJSON = false;
	protected List<String> alternatives = new ArrayList<String>();
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public FileResult(String fileUrl) throws IOException {
		this.fileUrl = fileUrl;
	}
	
	public FileResult(String fileUrl, boolean resultAsJSON) throws IOException {
		this(fileUrl);
		this.resultAsJSON = resultAsJSON;
	}
	
	public Serializable asResult(){
		return null;
	}

	public String getContentType() {
		return contentType;
	}
	
	public String getContentDisposition(){
		return contentDisposition;
	}
	
	public boolean isResultAsJSON(){
		return resultAsJSON;
	}
	
	public void setContentType(String contentType){
		this.contentType = contentType;
	}
	
	public void setContentDisposition(String contentDisposition){
		this.contentDisposition = contentDisposition;
	}
	
	public void setResultAsJSON(boolean resultAsJSON){
		this.resultAsJSON = resultAsJSON;
	}

	public List<String> getAlternatives() {
		return alternatives;
	}
	
	public void addAlternative(String alternative) {
		alternatives.add(alternative);
	}

	public void setAlternatives(List<String> alternatives) {
		this.alternatives = alternatives;
	}
}