package com.mrprez.gencross.web.bs.face;

import java.io.FileInputStream;
import java.util.Collection;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;

public interface ILoggerBS {

	Collection<Logger> getLoggers() throws Exception;

	Collection<Appender> getAppenders() throws Exception;

	FileInputStream getFileInputStream(String appenderName) throws Exception;

	void changeLogLevel(String loggerName, String levelName) throws Exception;

	Appender getAppender(String appenderName) throws Exception;

}
