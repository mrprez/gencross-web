package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.ForgottenPasswordAction;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;

@RunWith(MockitoJUnitRunner.class)
public class ForgottenPasswordActionTest extends AbstractActionTest {

	@Mock
	private IAuthentificationBS authentificationBS;

	@InjectMocks
	private ForgottenPasswordAction forgottenPasswordAction;


	@Test
	public void testExecute_Fail_NoUsername() throws Exception {
		// Execute
		String result = forgottenPasswordAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Mockito.verifyZeroInteractions(authentificationBS);
	}
	
	
	@Test
	public void testExecute_Fail_BadUsername() throws Exception {
		// Prepare
		String username = "balman";
		
		// Execute
		forgottenPasswordAction.setUsername(username);
		String result = forgottenPasswordAction.execute();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(forgottenPasswordAction.getActionErrors().contains("Login incorrect"));
	}
	
	
	@Test
	public void testExecute_Success() throws Exception {
		// Prepare
		String username = "batman";
		Mockito.when(authentificationBS.sendPassword(username)).thenReturn(AuthentificationBSTest.buildUser("batman"));
		
		// Execute
		forgottenPasswordAction.setUsername(username);
		String result = forgottenPasswordAction.execute();

		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(authentificationBS).sendPassword(username);
		Assert.assertEquals("Un nouveau mot de passe vous a été envoyé à l'adresse: batman@gmail.com", forgottenPasswordAction.getSuccessMessage());
		Assert.assertEquals("Login", forgottenPasswordAction.getSuccessLink());
		Assert.assertEquals("Retour", forgottenPasswordAction.getSuccessLinkLabel());
	}
}
