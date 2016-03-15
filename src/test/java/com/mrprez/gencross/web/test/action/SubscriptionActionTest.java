package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.SubscriptionAction;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class SubscriptionActionTest extends AbstractActionTest {

	@Mock
	private IAuthentificationBS authentificationBS;

	@InjectMocks
	private SubscriptionAction subscriptionAction;



	@Test
	public void testExecute() throws Exception {
		// Execute
		String result = subscriptionAction.execute();

		// Check
		Assert.assertEquals("input", result);
	}


	@Test
	public void testAuthentification_Fail() throws Exception {
		// Prepare
		String username = "robin";
		String confirmPassword = "I am robin";
		String mail = "robin@gmail.com";
		
		// Execute
		subscriptionAction.setUsername(username);
		subscriptionAction.setConfirmPassword(confirmPassword);
		subscriptionAction.setMail(mail);
		String result = subscriptionAction.authentification();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(subscriptionAction.getActionErrors().contains("Ce login existe déjà"));
	}
	
	
	@Test
	public void testAuthentification_Success() throws Exception {
		// Prepare
		String username = "robin";
		String confirmPassword = "I am robin";
		String mail = "robin@gmail.com";
		UserBO user = AuthentificationBSTest.buildUser(username);
		Mockito.when(authentificationBS.createUser(username, confirmPassword, mail)).thenReturn(user);
		
		// Execute
		subscriptionAction.setUsername(username);
		subscriptionAction.setConfirmPassword(confirmPassword);
		subscriptionAction.setMail(mail);
		String result = subscriptionAction.authentification();

		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(authentificationBS).createUser(username, confirmPassword, mail);
		Assert.assertEquals(user, ActionContext.getContext().getSession().get("user"));
	}
}
