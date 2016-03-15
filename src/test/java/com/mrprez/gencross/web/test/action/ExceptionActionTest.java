package com.mrprez.gencross.web.test.action;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.ExceptionAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

@RunWith(MockitoJUnitRunner.class)
public class ExceptionActionTest extends AbstractActionTest {

	@InjectMocks
	private ExceptionAction exceptionAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		Exception cause = new Exception("Why so serious?");
		final Exception exception = new Exception("Because I am mad", cause);
		ValueStack stack = Mockito.mock(ValueStack.class);
		Mockito.when(stack.findValue("exception")).thenReturn(exception);
		ActionContext.getContext().setValueStack(stack);
		
		BufferAppender appender = new BufferAppender();
		Logger.getRootLogger().addAppender(appender);
		
		// Execute
		String result = exceptionAction.execute();

		// Check
		Assert.assertEquals("success", result);
		Assert.assertTrue(appender.hasLogged("Why so serious?"));
		Assert.assertTrue(appender.hasLogged("Caused by:"));
		Assert.assertTrue(appender.hasLogged("Because I am mad"));
	}
	
	
	public static class BufferAppender extends AppenderSkeleton{
		private StringBuffer buffer = new StringBuffer();
		
		@Override
		public void close() {}

		@Override
		public boolean requiresLayout() {
			return true;
		}

		@Override
		protected void append(LoggingEvent event) {
			buffer.append(event.getRenderedMessage());
			buffer.append("\n");
		}
		
		public boolean hasLogged(String text){
			return buffer.toString().contains(text);
		}
	}
	
	
	
}
