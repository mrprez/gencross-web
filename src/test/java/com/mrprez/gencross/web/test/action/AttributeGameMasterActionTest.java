package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.AttributeGameMasterAction;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.opensymphony.xwork2.ActionContext;

@Ignore
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
	public void testAttribute_Fail_BadGmName() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		Integer personnageId = 2;
		String newGameMasterName = "superman";
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		ActionContext.getContext().getSession().put("user", user);
		Mockito.when(personnageBS.loadPersonnageAsPlayer(personnageId, user)).thenReturn(personnageWork);
		attributeGameMasterAction.setPersonnageId(personnageId);
		attributeGameMasterAction.setNewGameMasterName(newGameMasterName);
		
		// Execute
		String result = attributeGameMasterAction.attribute();

		// Check
		Assert.assertEquals("error", result);
		Mockito.verify(personnageBS, Mockito.never())
				.attribute(Mockito.any(PersonnageWorkBO.class), Mockito.any(UserBO.class), Mockito.any(UserBO.class));
		Mockito.verify(authentificationBS).getUser(newGameMasterName);
	}
	
	
	@Test
	public void testAttribute_Success_NoMoreGm() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		Integer personnageId = 2;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(user);
		
		ActionContext.getContext().getSession().put("user", user);
		Mockito.when(personnageBS.loadPersonnageAsPlayer(personnageId, user)).thenReturn(personnageWork);
		attributeGameMasterAction.setPersonnageId(personnageId);
		attributeGameMasterAction.setNewGameMasterName("_no_gm_");
		
		// Execute
		String result = attributeGameMasterAction.attribute();
		
		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(personnageBS).attribute(personnageWork, user, null);
		Assert.assertEquals("Le personnage n'a plus de MJ.", attributeGameMasterAction.getSuccessMessage());
	}
	
	
	@Test
	public void testAttribute_Success_NewGm() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		String newGameMasterName = "superman";
		UserBO newGameMaster = AuthentificationBSTest.buildUser(newGameMasterName);
		Integer personnageId = 2;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(user);
		
		ActionContext.getContext().getSession().put("user", user);
		Mockito.when(personnageBS.loadPersonnageAsPlayer(personnageId, user)).thenReturn(personnageWork);
		Mockito.when(authentificationBS.getUser(newGameMasterName)).thenReturn(newGameMaster);
		attributeGameMasterAction.setPersonnageId(personnageId);
		attributeGameMasterAction.setNewGameMasterName(newGameMasterName);
		
		// Execute
		String result = attributeGameMasterAction.attribute();
		
		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(personnageBS).attribute(personnageWork, user, newGameMaster);
		Assert.assertEquals("Le personnage a un nouveau MJ: superman.", attributeGameMasterAction.getSuccessMessage());
	}
	
	
	
}
