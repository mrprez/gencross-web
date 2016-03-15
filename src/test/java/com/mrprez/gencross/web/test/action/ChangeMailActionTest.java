package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.ChangeMailAction;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class ChangeMailActionTest extends AbstractActionTest {

	@Mock
	private IAuthentificationBS authentificationBS;

	@InjectMocks
	private ChangeMailAction changeMailAction;



	@Test
	public void testExecute() throws Exception {
		// Execute
		String result = changeMailAction.execute();

		// Check
		Assert.assertEquals("input", result);
	}


	@Test
	public void testChangeMail() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		String mail = "batman_gotham@gmail.com";
		
		// Execute
		changeMailAction.setMail(mail);
		String result = changeMailAction.changeMail();

		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(authentificationBS).changeMail(user, mail);
	}
}
