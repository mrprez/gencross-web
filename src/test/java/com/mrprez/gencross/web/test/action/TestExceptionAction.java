package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.ExceptionAction;

@RunWith(MockitoJUnitRunner.class)
public class TestExceptionAction {

	@InjectMocks
	private ExceptionAction exceptionAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare

		// Execute
		exceptionAction.execute();

		// Check
	}
}
