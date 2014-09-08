package com.mrprez.gencross.web.action;

import java.io.InputStream;

import com.opensymphony.xwork2.ActionSupport;

public class DownloadAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private static final String GENCROSS_UI_SETUP = "GencrossUI.zip";
	
	private String fileName;
	private InputStream inputStream;
	
	
	public String execute(){
		return INPUT;
	}
	
	public String getGenCrossUI() throws Exception{
		inputStream = getClass().getClassLoader().getResourceAsStream(GENCROSS_UI_SETUP);
		fileName = GENCROSS_UI_SETUP;
		return SUCCESS;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	
	
	
	

}
