package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.CreatePersonnageAction;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;

@RunWith(MockitoJUnitRunner.class)
public class TestCreatePersonnageAction {

	@Mock
	private IAuthentificationBS authentificationBS;

	@Mock
	private IPersonnageBS personnageBS;

	@InjectMocks
	private CreatePersonnageAction createPersonnageAction;



	@Test
	public void testCreate() throws Exception {
		// Prepare
		createPersonnageAction.setSelectedPersonnageTypeName("string_1");
		createPersonnageAction.setPersonnageName("string_2");
		createPersonnageAction.setGameMasterName("string_3");
		createPersonnageAction.setPlayerName("string_4");
		createPersonnageAction.setRole("string_5");

		// Execute
		createPersonnageAction.create();

		// Check
		Assert.assertEquals("failTest", createPersonnageAction.getSelectedPersonnageTypeName());
		Assert.assertEquals("failTest", createPersonnageAction.getNoGmKey());
		Assert.assertEquals("failTest", createPersonnageAction.getPlayerName());
		Assert.assertEquals("failTest", createPersonnageAction.getRole());
		Assert.assertEquals("failTest", createPersonnageAction.getPersonnageId());
		Assert.assertEquals("failTest", createPersonnageAction.getGameMasterName());
		Assert.assertEquals("failTest", createPersonnageAction.getRoleList());
		Assert.assertEquals("failTest", createPersonnageAction.getPersonnageName());
		Assert.assertEquals("failTest", createPersonnageAction.getUserList());
		Assert.assertEquals("failTest", createPersonnageAction.getPersonnageTypeList());
	}


	@Test
	public void testExecute() throws Exception {
		// Prepare
		createPersonnageAction.setSelectedPersonnageTypeName("string_1");
		createPersonnageAction.setPersonnageName("string_2");
		createPersonnageAction.setGameMasterName("string_3");
		createPersonnageAction.setPlayerName("string_4");
		createPersonnageAction.setRole("string_5");

		// Execute
		createPersonnageAction.execute();

		// Check
		Assert.assertEquals("failTest", createPersonnageAction.getSelectedPersonnageTypeName());
		Assert.assertEquals("failTest", createPersonnageAction.getNoGmKey());
		Assert.assertEquals("failTest", createPersonnageAction.getPlayerName());
		Assert.assertEquals("failTest", createPersonnageAction.getRole());
		Assert.assertEquals("failTest", createPersonnageAction.getPersonnageId());
		Assert.assertEquals("failTest", createPersonnageAction.getGameMasterName());
		Assert.assertEquals("failTest", createPersonnageAction.getRoleList());
		Assert.assertEquals("failTest", createPersonnageAction.getPersonnageName());
		Assert.assertEquals("failTest", createPersonnageAction.getUserList());
		Assert.assertEquals("failTest", createPersonnageAction.getPersonnageTypeList());
	}
}