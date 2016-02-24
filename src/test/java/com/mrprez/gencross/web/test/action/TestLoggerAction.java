package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.LoggerAction;
import com.mrprez.gencross.web.bs.face.ILoggerBS;

@RunWith(MockitoJUnitRunner.class)
public class TestLoggerAction {

	@Mock
	private ILoggerBS loggerBS;

	@InjectMocks
	private LoggerAction loggerAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		loggerAction.setAppenderName("string_1");
		loggerAction.setLoggerName("string_2");
		loggerAction.setLevelName("string_3");

		// Execute
		loggerAction.execute();

		// Check
		Assert.assertEquals("failTest", loggerAction.getInputStream());
		Assert.assertEquals("failTest", loggerAction.getAppenderName());
		Assert.assertEquals("failTest", loggerAction.getAppenders());
		Assert.assertEquals("failTest", loggerAction.getLoggers());
		Assert.assertEquals("failTest", loggerAction.getLevelName());
		Assert.assertEquals("failTest", loggerAction.getLevels());
		Assert.assertEquals("failTest", loggerAction.getLoggerName());
	}


	@Test
	public void testDownload() throws Exception {
		// Prepare
		loggerAction.setAppenderName("string_1");
		loggerAction.setLoggerName("string_2");
		loggerAction.setLevelName("string_3");

		// Execute
		loggerAction.download();

		// Check
		Assert.assertEquals("failTest", loggerAction.getInputStream());
		Assert.assertEquals("failTest", loggerAction.getAppenderName());
		Assert.assertEquals("failTest", loggerAction.getAppenders());
		Assert.assertEquals("failTest", loggerAction.getLoggers());
		Assert.assertEquals("failTest", loggerAction.getLevelName());
		Assert.assertEquals("failTest", loggerAction.getLevels());
		Assert.assertEquals("failTest", loggerAction.getLoggerName());
	}


	@Test
	public void testChangeLevel() throws Exception {
		// Prepare
		loggerAction.setAppenderName("string_1");
		loggerAction.setLoggerName("string_2");
		loggerAction.setLevelName("string_3");

		// Execute
		loggerAction.changeLevel();

		// Check
		Assert.assertEquals("failTest", loggerAction.getInputStream());
		Assert.assertEquals("failTest", loggerAction.getAppenderName());
		Assert.assertEquals("failTest", loggerAction.getAppenders());
		Assert.assertEquals("failTest", loggerAction.getLoggers());
		Assert.assertEquals("failTest", loggerAction.getLevelName());
		Assert.assertEquals("failTest", loggerAction.getLevels());
		Assert.assertEquals("failTest", loggerAction.getLoggerName());
	}
}
