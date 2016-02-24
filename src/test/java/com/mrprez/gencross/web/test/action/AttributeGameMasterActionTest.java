package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.AttributeGameMasterAction;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class AttributeGameMasterActionTest extends AbstractActionTest {

	@Mock
	private IPersonnageBS personnageBS;

	@Mock
	private IAuthentificationBS authentificationBS;

	@InjectMocks
	private AttributeGameMasterAction attributeGameMasterAction;



	@Test
	public void testExecute_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		Integer personnageId = 2;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		ActionContext.getContext().getSession().put("user", user);
		Mockito.when(personnageBS.loadPersonnageAsPlayer(personnageId, user)).thenReturn(personnageWork);
		attributeGameMasterAction.setPersonnageId(personnageId);
		
		// Execute
		String result = attributeGameMasterAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals(personnageWork, attributeGameMasterAction.getPersonnageWork());
	}
	
	@Test
	public void testExecute_Fail() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		attributeGameMasterAction.setPersonnageId(2);
		
		// Execute
		String result = attributeGameMasterAction.execute();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertNull(attributeGameMasterAction.getPersonnageWork());
	}


	@Test
	public void testAttribute_Fail_NoPersonnage() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		attributeGameMasterAction.setPersonnageId(2);
		
		// Execute
		String result = attributeGameMasterAction.attribute();

		// Check
		Assert.assertEquals("error", result);
		Mockito.verify(personnageBS, Mockito.never())
				.attribute(Mockito.any(PersonnageWorkBO.class), Mockito.any(UserBO.class), Mockito.any(UserBO.class));
	}
	
	
	@Test
	public void testAttribute_Fail_NoPersonnage() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		attributeGameMasterAction.setPersonnageId(2);
		
		// Execute
		String result = attributeGameMasterAction.attribute();

		// Check
		Assert.assertEquals("error", result);
		Mockito.verify(personnageBS, Mockito.never())
				.attribute(Mockito.any(PersonnageWorkBO.class), Mockito.any(UserBO.class), Mockito.any(UserBO.class));
	}
	
}
