package com.mrprez.gencross.web.action;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ExceptionAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	
	public String execute() throws Exception {
		Throwable exception = (Throwable) ActionContext.getContext().getValueStack().findValue("exception");
		logException(exception);
		
		return SUCCESS;
	}
	
	private void logException(Throwable exception){
		Logger.getLogger(this.getClass()).error(exception.getMessage());
		StackTraceElement stackTrace[] = exception.getStackTrace();
		for(int i=0; i<stackTrace.length; i++){
			Logger.getLogger(this.getClass()).error("\t"+stackTrace[i].toString());
		}
		if(exception.getCause()!=exception){
			Logger.getLogger(this.getClass()).error("Caused by:");
			logException(exception.getCause());
		}
		
	}

}
