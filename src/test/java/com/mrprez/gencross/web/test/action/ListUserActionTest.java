package com.mrprez.gencross.web.test.action;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.ListUserAction;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;

@RunWith(MockitoJUnitRunner.class)
public class ListUserActionTest extends AbstractActionTest {

	@Mock
	private IAuthentificationBS authentificationBS;

	@InjectMocks
	private ListUserAction listUserAction;



	@Test
	public void testRemove() throws Exception {
		// Prepare
		String username = "robin";
		
		// Execute
		listUserAction.setUsername(username);
		String result = listUserAction.remove();

		// Check
		Assert.assertEquals("input", result);
		Mockito.verify(authentificationBS).removeUser(username);
	}


	@Test
	public void testExecute() throws Exception {
		// Prepare
		List<UserBO> userList = Arrays.asList(
				AuthentificationBSTest.buildUser("batman"),
				AuthentificationBSTest.buildUser("robin"),
				AuthentificationBSTest.buildUser("alfred"));
		Mockito.when(authentificationBS.getUserList()).thenReturn(userList);

		// Execute
		String result = listUserAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals(userList, listUserAction.getUserList());
	}
}
