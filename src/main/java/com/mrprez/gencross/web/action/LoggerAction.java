package com.mrprez.gencross.web.action;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.lf5.LogLevel;
import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.web.bs.face.ILoggerBS;
import com.opensymphony.xwork2.ActionSupport;

public class LoggerAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private Collection<Logger> loggers;
	private Collection<Appender> appenders;
	private InputStream inputStream;
	private String appenderName;
	private List<LogLevel> levels;
	private String loggerName;
	private String levelName;
	
	

	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		ILoggerBS loggerBS = (ILoggerBS)ContextLoader.getCurrentWebApplicationContext().getBean("loggerBS");
		loggers = loggerBS.getLoggers();
		appenders = loggerBS.getAppenders();
		levels = LogLevel.getLog4JLevels();
		
		return INPUT;
	}
	
	
	public String download() throws Exception {
		ILoggerBS loggerBS = (ILoggerBS)ContextLoader.getCurrentWebApplicationContext().getBean("loggerBS");
		inputStream = loggerBS.getFileInputStream(appenderName);
		return "download";
	}
	
	
	public String changeLevel() throws Exception {
		ILoggerBS loggerBS = (ILoggerBS)ContextLoader.getCurrentWebApplicationContext().getBean("loggerBS");
		loggerBS.changeLogLevel(loggerName, levelName);
		return SUCCESS;
	}


	public Collection<Logger> getLoggers() {
		return loggers;
	}
	public void setLoggers(Set<Logger> loggers) {
		this.loggers = loggers;
	}
	public Collection<Appender> getAppenders() {
		return appenders;
	}
	public void setAppenders(Set<Appender> appenders) {
		this.appenders = appenders;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public String getAppenderName() {
		return appenderName;
	}
	public void setAppenderName(String appenderName) {
		this.appenderName = appenderName;
	}
	public List<LogLevel> getLevels() {
		return levels;
	}
	public String getLoggerName() {
		return loggerName;
	}
	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	

}
