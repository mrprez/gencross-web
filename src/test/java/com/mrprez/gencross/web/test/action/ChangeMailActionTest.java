package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.ChangeMailAction;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class ChangeMailActionTest extends AbstractActionTest {

	@Mock
	private IAuthentificationBS authentificationBS;

	@InjectMocks
	private ChangeMailAction changeMailAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		changeMailAction.setMail("string_1");

		// Execute
		String result = changeMailAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", changeMailAction.getMail());
	}


	@Test
	public void testChangeMail() throws Exception {
		// Prepare
		changeMailAction.setMail("string_1");

		// Execute
		String result = changeMailAction.changeMail();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", changeMailAction.getMail());
	}
}
