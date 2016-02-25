package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.LoggerAction;
import com.mrprez.gencross.web.bs.face.ILoggerBS;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class LoggerActionTest extends AbstractActionTest {

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
		String result = loggerAction.execute();

		// Check
		Assert.assertEquals("input", result);
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
		String result = loggerAction.download();

		// Check
		Assert.assertEquals("input", result);
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
		String result = loggerAction.changeLevel();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", loggerAction.getInputStream());
		Assert.assertEquals("failTest", loggerAction.getAppenderName());
		Assert.assertEquals("failTest", loggerAction.getAppenders());
		Assert.assertEquals("failTest", loggerAction.getLoggers());
		Assert.assertEquals("failTest", loggerAction.getLevelName());
		Assert.assertEquals("failTest", loggerAction.getLevels());
		Assert.assertEquals("failTest", loggerAction.getLoggerName());
	}
}
