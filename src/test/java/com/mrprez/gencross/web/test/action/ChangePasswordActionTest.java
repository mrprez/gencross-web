package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.ChangePasswordAction;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class ChangePasswordActionTest extends AbstractActionTest {

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
		String result = changePasswordAction.execute();

		// Check
		Assert.assertEquals("input", result);
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
		String result = changePasswordAction.changePassword();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", changePasswordAction.getNewPassword());
		Assert.assertEquals("failTest", changePasswordAction.getPassword());
		Assert.assertEquals("failTest", changePasswordAction.getConfirm());
		Assert.assertEquals("failTest", changePasswordAction.getSuccessMessage());
	}
}
