package com.mrprez.gencross.web.bs;

import java.io.FileInputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.TreeSet;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.mrprez.gencross.web.bs.face.ILoggerBS;

public class LoggerBS implements ILoggerBS {

	@Override
	public Collection<Logger> getLoggers() throws Exception {
		Collection<Logger> result = new TreeSet<Logger>(new Comparator<Logger>() {
			@Override
			public int compare(Logger arg0, Logger arg1) {
				return arg0.getName().compareTo(arg1.getName());
			}
		});
		Enumeration<?> loggersEnum = Logger.getRootLogger().getLoggerRepository().getCurrentLoggers();
		while(loggersEnum.hasMoreElements()){
			Logger logger = (Logger) loggersEnum.nextElement();
			if(logger.getLevel()!=null){
				result.add(logger);
			}
		}
		return result;
	}
	
	
	@Override
	public Collection<Appender> getAppenders() throws Exception {
		Collection<Appender> result = new HashSet<Appender>();
		Enumeration<?> loggersEnum = Logger.getRootLogger().getLoggerRepository().getCurrentLoggers();
		while(loggersEnum.hasMoreElements()){
			Logger logger = (Logger) loggersEnum.nextElement();
			Enumeration<?> appendersEnum = logger.getAllAppenders();
			while(appendersEnum.hasMoreElements()){
				Appender appender = (Appender) appendersEnum.nextElement();
				result.add(appender);
			}
		}
		return result;
	}
	
	@Override
	public FileInputStream getFileInputStream(String appenderName) throws Exception {
		Appender appender = getAppender(appenderName);
		if(appender instanceof FileAppender){
			return new FileInputStream(((FileAppender)appender).getFile());
		}
		return null;
	}
	
	@Override
	public void changeLogLevel(String loggerName, String levelName) throws Exception {
		Logger logger = Logger.getLogger(loggerName);
		logger.setLevel(Level.toLevel(levelName));
	}
	
	@Override
	public Appender getAppender(String appenderName) throws Exception {
		for(Appender appender : getAppenders()){
			if(appender.getName().equals(appenderName)){
				return appender;
			}
		}
		return null;
	}
	
}
