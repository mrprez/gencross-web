package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.ChangePasswordAction;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class ChangePasswordActionTest extends AbstractActionTest {

	@Mock
	private IAuthentificationBS authentificationBS;

	@InjectMocks
	private ChangePasswordAction changePasswordAction;



	@Test
	public void testExecute() throws Exception {
		// Execute
		String result = changePasswordAction.execute();

		// Check
		Assert.assertEquals("input", result);
	}


	@Test
	public void testChangePassword_Fail() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		String password = "I'am batman";
		String newPassword = "Boum";
		
		// Execute
		changePasswordAction.setPassword(password);
		changePasswordAction.setNewPassword(newPassword);
		String result = changePasswordAction.changePassword();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(changePasswordAction.getActionErrors().contains("Ancien mot de passe incorrect"));
	}
	
	
	@Test
	public void testChangePassword_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		String password = "I'am batman";
		String newPassword = "Boum";
		Mockito.when(authentificationBS.authentificateUser(user.getUsername(), password)).thenReturn(AuthentificationBSTest.buildUser("batman"));
		
		// Execute
		changePasswordAction.setPassword(password);
		changePasswordAction.setNewPassword(newPassword);
		String result = changePasswordAction.changePassword();

		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(authentificationBS).changePassword(user, newPassword);
	}
}
