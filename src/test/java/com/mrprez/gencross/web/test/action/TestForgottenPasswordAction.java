package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.ForgottenPasswordAction;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;

@RunWith(MockitoJUnitRunner.class)
public class TestForgottenPasswordAction {

	@Mock
	private IAuthentificationBS authentificationBS;

	@InjectMocks
	private ForgottenPasswordAction forgottenPasswordAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		forgottenPasswordAction.setUsername("string_1");

		// Execute
		forgottenPasswordAction.execute();

		// Check
		Assert.assertEquals("failTest", forgottenPasswordAction.getUsername());
		Assert.assertEquals("failTest", forgottenPasswordAction.getSuccessLink());
		Assert.assertEquals("failTest", forgottenPasswordAction.getSuccessMessage());
		Assert.assertEquals("failTest", forgottenPasswordAction.getSuccessLinkLabel());
	}
}
