package com.mrprez.gencross.web.test.bs;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.bs.LoggerBS;

@RunWith(MockitoJUnitRunner.class)
public class LoggerBSTest {
	
	@InjectMocks
	private LoggerBS loggerBS;
	
	
	@Test
	public void testGetLoggers() throws Exception{
		// Execute
		Collection<Logger> result = loggerBS.getLoggers();
		
		// Check
		Set<String> loggerNames = new HashSet<String>(Arrays.asList(
				"com.mrprez.gencross", "com.opensymphony", "org.apache.struts2", "org.directwebremoting",
				"org.hibernate", "org.quartz", "org.springframework", "quartz", "responseDelay", "senderJob"));
		for(Logger logger : result){
			Assert.assertTrue("Logger "+logger.getName()+" not expected", loggerNames.contains(logger.getName()));
			loggerNames.remove(logger.getName());
		}
		Assert.assertTrue(loggerNames.isEmpty());
	}
	
	
	@Test
	public void testGetAppenders() throws Exception{
		// Execute
		Collection<Appender> appenderList = loggerBS.getAppenders();
		
		// Check
		Set<String> appenderNames = new HashSet<String>(Arrays.asList("file", "console", "quartzFile", "delayFile"));
		for(Appender appender : appenderList){
			Assert.assertTrue(appenderNames.contains(appender.getName()));
			appenderNames.remove(appender.getName());
		}
		Assert.assertTrue(appenderNames.isEmpty());
	}
	
	@Test
	public void testGetAppender_Success() throws Exception{
		// Execute
		Appender appender = loggerBS.getAppender("console");
		
		// Check
		Assert.assertNotNull(appender);
		Assert.assertEquals("console", appender.getName());
	}
	
	@Test
	public void testGetAppender_Fail() throws Exception{
		// Execute
		Appender appender = loggerBS.getAppender("NoOne");
		
		// Check
		Assert.assertNull(appender);
	}
	
	
	@Test
	public void testGetFileInputStream_Fail() throws Exception{
		// Execute
		FileInputStream result = loggerBS.getFileInputStream("console");
		
		// Check
		Assert.assertNull(result);
	}

	@Test
	public void testChangeLogLevel() throws Exception{
		// Execute
		loggerBS.changeLogLevel("senderJob", "TRACE");
		
		// Check
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
