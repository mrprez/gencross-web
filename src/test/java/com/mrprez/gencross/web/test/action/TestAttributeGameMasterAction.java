package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.AttributeGameMasterAction;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;

@RunWith(MockitoJUnitRunner.class)
public class TestAttributeGameMasterAction {

	@Mock
	private IAuthentificationBS authentificationBS;

	@Mock
	private IPersonnageBS personnageBS;

	@InjectMocks
	private AttributeGameMasterAction attributeGameMasterAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		attributeGameMasterAction.setNewGameMasterName("string_1");
		attributeGameMasterAction.setPersonnageId(2);
		attributeGameMasterAction.setSuccessMessage("string_3");

		// Execute
		attributeGameMasterAction.execute();

		// Check
		Assert.assertEquals("failTest", attributeGameMasterAction.getSuccessMessage());
		Assert.assertEquals("failTest", attributeGameMasterAction.getUserList());
		Assert.assertEquals("failTest", attributeGameMasterAction.getPersonnageWork());
		Assert.assertEquals("failTest", attributeGameMasterAction.getPersonnageId());
		Assert.assertEquals("failTest", attributeGameMasterAction.getNoGmKey());
		Assert.assertEquals("failTest", attributeGameMasterAction.getNewGameMasterName());
	}


	@Test
	public void testAttribute() throws Exception {
		// Prepare
		attributeGameMasterAction.setNewGameMasterName("string_1");
		attributeGameMasterAction.setPersonnageId(2);
		attributeGameMasterAction.setSuccessMessage("string_3");

		// Execute
		attributeGameMasterAction.attribute();

		// Check
		Assert.assertEquals("failTest", attributeGameMasterAction.getSuccessMessage());
		Assert.assertEquals("failTest", attributeGameMasterAction.getUserList());
		Assert.assertEquals("failTest", attributeGameMasterAction.getPersonnageWork());
		Assert.assertEquals("failTest", attributeGameMasterAction.getPersonnageId());
		Assert.assertEquals("failTest", attributeGameMasterAction.getNoGmKey());
		Assert.assertEquals("failTest", attributeGameMasterAction.getNewGameMasterName());
	}
}
