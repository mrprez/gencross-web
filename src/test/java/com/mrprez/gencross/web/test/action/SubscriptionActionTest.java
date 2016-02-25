package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.SubscriptionAction;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class SubscriptionActionTest extends AbstractActionTest {

	@Mock
	private IAuthentificationBS authentificationBS;

	@InjectMocks
	private SubscriptionAction subscriptionAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		subscriptionAction.setConfirmPassword("string_1");
		subscriptionAction.setPassword("string_2");
		subscriptionAction.setUsername("string_3");
		subscriptionAction.setMail("string_4");

		// Execute
		String result = subscriptionAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", subscriptionAction.getConfirmPassword());
		Assert.assertEquals("failTest", subscriptionAction.getPassword());
		Assert.assertEquals("failTest", subscriptionAction.getUsername());
		Assert.assertEquals("failTest", subscriptionAction.getMail());
	}


	@Test
	public void testAuthentification() throws Exception {
		// Prepare
		subscriptionAction.setConfirmPassword("string_1");
		subscriptionAction.setPassword("string_2");
		subscriptionAction.setUsername("string_3");
		subscriptionAction.setMail("string_4");

		// Execute
		String result = subscriptionAction.authentification();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", subscriptionAction.getConfirmPassword());
		Assert.assertEquals("failTest", subscriptionAction.getPassword());
		Assert.assertEquals("failTest", subscriptionAction.getUsername());
		Assert.assertEquals("failTest", subscriptionAction.getMail());
	}
}
