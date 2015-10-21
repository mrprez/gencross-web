package com.mrprez.gencross.web.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.mrprez.gencross.web.bs.face.IGencrossUiPackagerBS;
import com.opensymphony.xwork2.ActionSupport;

public class DownloadAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private static final String GENCROSS_UI_SETUP = "GencrossUI.zip";
	
	private IGencrossUiPackagerBS gencrossUiPackagerBS;
	
	private String fileName;
	private InputStream inputStream;
	
	
	public String execute(){
		return INPUT;
	}
	
	public String getGenCrossUI() throws Exception{
		inputStream = new ByteArrayInputStream(gencrossUiPackagerBS.buildGencrossUiPackage());
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

	public IGencrossUiPackagerBS getGencrossUiPackagerBS() {
		return gencrossUiPackagerBS;
	}

	public void setGencrossUiPackagerBS(IGencrossUiPackagerBS gencrossUiPackagerBS) {
		this.gencrossUiPackagerBS = gencrossUiPackagerBS;
	}
	
	
	

}
