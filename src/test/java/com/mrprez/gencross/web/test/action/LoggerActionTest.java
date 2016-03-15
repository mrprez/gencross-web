package com.mrprez.gencross.web.test.action;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.lf5.LogLevel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.LoggerAction;
import com.mrprez.gencross.web.bs.face.ILoggerBS;

@RunWith(MockitoJUnitRunner.class)
public class LoggerActionTest extends AbstractActionTest {

	@Mock
	private ILoggerBS loggerBS;

	@InjectMocks
	private LoggerAction loggerAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		Collection<Logger> loggerList = Arrays.asList(Mockito.mock(Logger.class));
		Mockito.when(loggerBS.getLoggers()).thenReturn(loggerList);
		Collection<Appender> appenderList = Arrays.asList(Mockito.mock(Appender.class));
		Mockito.when(loggerBS.getAppenders()).thenReturn(appenderList);

		// Execute
		String result = loggerAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals(appenderList, loggerAction.getAppenders());
		Assert.assertEquals(loggerList, loggerAction.getLoggers());
		Assert.assertEquals(LogLevel.getLog4JLevels(), loggerAction.getLevels());
	}


	@Test
	public void testDownload() throws Exception {
		// Prepare
		String appenderName = "appender_1";
		FileInputStream inputStream = Mockito.mock(FileInputStream.class);
		Mockito.when(loggerBS.getFileInputStream(appenderName)).thenReturn(inputStream);
		
		// Execute
		loggerAction.setAppenderName(appenderName);
		String result = loggerAction.download();

		// Check
		Assert.assertEquals("download", result);
		Assert.assertEquals(inputStream, loggerAction.getInputStream());
	}


	@Test
	public void testChangeLevel() throws Exception {
		// Prepare
		String loggerName = "appender_1";
		String levelName = "level_3";
		
		// Execute
		loggerAction.setLoggerName(loggerName);
		loggerAction.setLevelName(levelName);
		String result = loggerAction.changeLevel();

		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(loggerBS).changeLogLevel(loggerName, levelName);
	}
}
