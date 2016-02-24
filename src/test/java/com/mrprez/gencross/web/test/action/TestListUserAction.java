package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.ListUserAction;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;

@RunWith(MockitoJUnitRunner.class)
public class TestListUserAction {

	@Mock
	private IAuthentificationBS authentificationBS;

	@InjectMocks
	private ListUserAction listUserAction;



	@Test
	public void testRemove() throws Exception {
		// Prepare
		listUserAction.setUsername("string_1");

		// Execute
		listUserAction.remove();

		// Check
		Assert.assertEquals("failTest", listUserAction.getUserList());
		Assert.assertEquals("failTest", listUserAction.getUsername());
	}


	@Test
	public void testExecute() throws Exception {
		// Prepare
		listUserAction.setUsername("string_1");

		// Execute
		listUserAction.execute();

		// Check
		Assert.assertEquals("failTest", listUserAction.getUserList());
		Assert.assertEquals("failTest", listUserAction.getUsername());
	}
}
