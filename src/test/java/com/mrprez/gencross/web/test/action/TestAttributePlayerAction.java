package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.AttributePlayerAction;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;

@RunWith(MockitoJUnitRunner.class)
public class TestAttributePlayerAction {

	@Mock
	private IAuthentificationBS authentificationBS;

	@Mock
	private IPersonnageBS personnageBS;

	@InjectMocks
	private AttributePlayerAction attributePlayerAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		attributePlayerAction.setTableId(1);
		attributePlayerAction.setSuccessLinkLabel("string_2");
		attributePlayerAction.setSuccessMessage("string_3");
		attributePlayerAction.setPersonnageId(4);
		attributePlayerAction.setNewPlayerName("string_5");
		attributePlayerAction.setSuccessLink("string_6");

		// Execute
		attributePlayerAction.execute();

		// Check
		Assert.assertEquals("failTest", attributePlayerAction.getPersonnageId());
		Assert.assertEquals("failTest", attributePlayerAction.getTableId());
		Assert.assertEquals("failTest", attributePlayerAction.getNewPlayerName());
		Assert.assertEquals("failTest", attributePlayerAction.getNoPlayerKey());
		Assert.assertEquals("failTest", attributePlayerAction.getSuccessLinkLabel());
		Assert.assertEquals("failTest", attributePlayerAction.getUserList());
		Assert.assertEquals("failTest", attributePlayerAction.getPersonnageWork());
		Assert.assertEquals("failTest", attributePlayerAction.getSuccessMessage());
		Assert.assertEquals("failTest", attributePlayerAction.getSuccessLink());
	}


	@Test
	public void testAttribute() throws Exception {
		// Prepare
		attributePlayerAction.setTableId(1);
		attributePlayerAction.setSuccessLinkLabel("string_2");
		attributePlayerAction.setSuccessMessage("string_3");
		attributePlayerAction.setPersonnageId(4);
		attributePlayerAction.setNewPlayerName("string_5");
		attributePlayerAction.setSuccessLink("string_6");

		// Execute
		attributePlayerAction.attribute();

		// Check
		Assert.assertEquals("failTest", attributePlayerAction.getPersonnageId());
		Assert.assertEquals("failTest", attributePlayerAction.getTableId());
		Assert.assertEquals("failTest", attributePlayerAction.getNewPlayerName());
		Assert.assertEquals("failTest", attributePlayerAction.getNoPlayerKey());
		Assert.assertEquals("failTest", attributePlayerAction.getSuccessLinkLabel());
		Assert.assertEquals("failTest", attributePlayerAction.getUserList());
		Assert.assertEquals("failTest", attributePlayerAction.getPersonnageWork());
		Assert.assertEquals("failTest", attributePlayerAction.getSuccessMessage());
		Assert.assertEquals("failTest", attributePlayerAction.getSuccessLink());
	}
}
