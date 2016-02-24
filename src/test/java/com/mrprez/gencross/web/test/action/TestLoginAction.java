package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.LoginAction;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;

@RunWith(MockitoJUnitRunner.class)
public class TestLoginAction {

	@Mock
	private IAuthentificationBS authentificationBS;

	@InjectMocks
	private LoginAction loginAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		loginAction.setPassword("string_1");
		loginAction.setUsername("string_2");

		// Execute
		loginAction.execute();

		// Check
		Assert.assertEquals("failTest", loginAction.getPassword());
		Assert.assertEquals("failTest", loginAction.getUsername());
	}


	@Test
	public void testDisconnect() throws Exception {
		// Prepare
		loginAction.setPassword("string_1");
		loginAction.setUsername("string_2");

		// Execute
		loginAction.disconnect();

		// Check
		Assert.assertEquals("failTest", loginAction.getPassword());
		Assert.assertEquals("failTest", loginAction.getUsername());
	}
}
