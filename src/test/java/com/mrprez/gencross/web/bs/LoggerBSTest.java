package com.mrprez.gencross.web.bs;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class LoggerBSTest {
	
	@Test
	public void testGetLoggers() throws Exception{
		LoggerBS loggerBS = new LoggerBS();
		
		Collection<Logger> result = loggerBS.getLoggers();
		
		Set<String> loggerNames = new HashSet<String>(Arrays.asList(
				"com.mrprez.gencross", "com.opensymphony", "org.apache.struts2", "org.hibernate",
				"org.quartz", "org.springframework", "quartz", "responseDelay", "senderJob"));
		for(Logger logger : result){
			Assert.assertTrue(loggerNames.contains(logger.getName()));
			loggerNames.remove(logger.getName());
		}
		Assert.assertTrue(loggerNames.isEmpty());
	}
	
	
	@Test
	public void testGetAppenders() throws Exception{
		LoggerBS loggerBS = new LoggerBS();
		
		Collection<Appender> appenderList = loggerBS.getAppenders();
		
		Set<String> appenderNames = new HashSet<String>(Arrays.asList("file", "console", "quartzFile", "delayFile"));
		for(Appender appender : appenderList){
			Assert.assertTrue(appenderNames.contains(appender.getName()));
			appenderNames.remove(appender.getName());
		}
		Assert.assertTrue(appenderNames.isEmpty());
	}
	
	@Test
	public void testGetAppender_Success() throws Exception{
		LoggerBS loggerBS = new LoggerBS();
		
		Appender appender = loggerBS.getAppender("console");
		
		Assert.assertNotNull(appender);
		Assert.assertEquals("console", appender.getName());
	}
	
	@Test
	public void testGetAppender_Fail() throws Exception{
		LoggerBS loggerBS = new LoggerBS();
		
		Appender appender = loggerBS.getAppender("NoOne");
		
		Assert.assertNull(appender);
	}
	
	
	@Test
	public void testGetFileInputStream_Success() throws Exception{
		LoggerBS loggerBS = new LoggerBS();
		
		FileInputStream result = loggerBS.getFileInputStream("quartzFile");
		
		Assert.assertNotNull(result);
	}
	
	
	@Test
	public void testGetFileInputStream_Fail() throws Exception{
		LoggerBS loggerBS = new LoggerBS();
		
		FileInputStream result = loggerBS.getFileInputStream("console");
		
		Assert.assertNull(result);
	}

	@Test
	public void testChangeLogLevel() throws Exception{
		LoggerBS loggerBS = new LoggerBS();
		
		loggerBS.changeLogLevel("senderJob", "TRACE");
		
		boolean loggerFound = false;
		for(Logger logger : loggerBS.getLoggers()){
			if(logger.getName().equals("senderJob")){
				Assert.assertEquals("TRACE", logger.getLevel().toString());
				loggerFound = true;
			}
		}
		Assert.assertTrue(loggerFound);
	}
	
	
	
}
