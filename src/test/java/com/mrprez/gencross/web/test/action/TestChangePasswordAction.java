package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.ChangePasswordAction;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;

@RunWith(MockitoJUnitRunner.class)
public class TestChangePasswordAction {

	@Mock
	private IAuthentificationBS authentificationBS;

	@InjectMocks
	private ChangePasswordAction changePasswordAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		changePasswordAction.setNewPassword("string_1");
		changePasswordAction.setPassword("string_2");
		changePasswordAction.setConfirm("string_3");

		// Execute
		changePasswordAction.execute();

		// Check
		Assert.assertEquals("failTest", changePasswordAction.getNewPassword());
		Assert.assertEquals("failTest", changePasswordAction.getPassword());
		Assert.assertEquals("failTest", changePasswordAction.getConfirm());
		Assert.assertEquals("failTest", changePasswordAction.getSuccessMessage());
	}


	@Test
	public void testChangePassword() throws Exception {
		// Prepare
		changePasswordAction.setNewPassword("string_1");
		changePasswordAction.setPassword("string_2");
		changePasswordAction.setConfirm("string_3");

		// Execute
		changePasswordAction.changePassword();

		// Check
		Assert.assertEquals("failTest", changePasswordAction.getNewPassword());
		Assert.assertEquals("failTest", changePasswordAction.getPassword());
		Assert.assertEquals("failTest", changePasswordAction.getConfirm());
		Assert.assertEquals("failTest", changePasswordAction.getSuccessMessage());
	}
}
