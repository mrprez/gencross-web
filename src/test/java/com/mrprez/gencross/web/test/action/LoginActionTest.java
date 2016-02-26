package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.LoginAction;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class LoginActionTest extends AbstractActionTest {

	@Mock
	private IAuthentificationBS authentificationBS;

	@InjectMocks
	private LoginAction loginAction;



	@Test
	public void testExecute_Success_Input() throws Exception {
		// Execute
		String result = loginAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertFalse(ActionContext.getContext().getSession().containsKey("user"));
		Assert.assertFalse(ActionContext.getContext().getSession().containsKey("loginDate"));
	}

	
	@Test
	public void testExecute_Fail() throws Exception {
		// Execute
		loginAction.setUsername("username");
		loginAction.setPassword("password");
		String result = loginAction.execute();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(loginAction.getActionErrors().contains("Login ou mot de passe incorrect"));
		Assert.assertFalse(ActionContext.getContext().getSession().containsKey("user"));
		Assert.assertFalse(ActionContext.getContext().getSession().containsKey("loginDate"));
	}
	
	
	@Test
	public void testExecute_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("username");
		Mockito.when(authentificationBS.authentificateUser("username", "password")).thenReturn(user);
		
		// Execute
		loginAction.setUsername("username");
		loginAction.setPassword("password");
		String result = loginAction.execute();

		// Check
		Assert.assertEquals("success", result);
		Assert.assertTrue(loginAction.getActionErrors().isEmpty());
		Assert.assertEquals(user, ActionContext.getContext().getSession().get("user"));
		Assert.assertNotNull(ActionContext.getContext().getSession().get("loginDate"));
	}
	

	@Test
	public void testDisconnect() throws Exception {
		// Prepare
		ActionContext.getContext().getSession().put("user", new UserBO());

		// Execute
		String result = loginAction.disconnect();
		
		// Check
		Assert.assertEquals("success", result);
		Assert.assertTrue(ActionContext.getContext().getSession().isEmpty());
	}
}
