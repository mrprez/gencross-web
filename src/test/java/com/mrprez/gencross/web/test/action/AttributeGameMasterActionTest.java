package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
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

@RunWith(MockitoJUnitRunner.class)
public class AttributeGameMasterActionTest extends AbstractActionTest {

	@Mock
	private IAuthentificationBS authentificationBS;
	
	@Mock
	private IPersonnageBS personnageBS;
	
	@InjectMocks
	private AttributeGameMasterAction attributeGameMasterAction;
	
	
	@Test
	public void testExecute_Fail() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		
		// Execute
		attributeGameMasterAction.setPersonnageId(personnageId);
		String result = attributeGameMasterAction.execute();
		
		// Check
		Assert.assertEquals("error", result);
	}
	
	
	@Test
	public void testExecute_Success() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageBS.loadPersonnageAsPlayer(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		attributeGameMasterAction.setPersonnageId(personnageId);
		String result = attributeGameMasterAction.execute();
		
		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals(personnageWork, attributeGameMasterAction.getPersonnageWork());
	}


	@Test
	public void testAttribute_Fail_NoPersonnage() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		
		// Execute
		attributeGameMasterAction.setPersonnageId(personnageId);
		String result = attributeGameMasterAction.attribute();
		
		// Check
		Assert.assertEquals("error", result);
		Mockito.verify(personnageBS, Mockito.never()).attribute(
				Mockito.any(PersonnageWorkBO.class), Mockito.any(UserBO.class), Mockito.any(UserBO.class));	
	}
	
	
	@Test
	public void testAttribute_Success_NoGm() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		String newGameMasterName = AttributeGameMasterAction.NO_GM_KEY;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(user);
		Mockito.when(personnageBS.loadPersonnageAsPlayer(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		attributeGameMasterAction.setNewGameMasterName(newGameMasterName);
		attributeGameMasterAction.setPersonnageId(personnageId);
		String result = attributeGameMasterAction.attribute();
		
		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(personnageBS).attribute(personnageWork, user, null);
	}
	
	
	@Test
	public void testAttribute_Success_BadGm() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		String newGameMasterName = "Alfred";
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageBS.loadPersonnageAsPlayer(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		attributeGameMasterAction.setNewGameMasterName(newGameMasterName);
		attributeGameMasterAction.setPersonnageId(personnageId);
		String result = attributeGameMasterAction.attribute();
		
		// Check
		Assert.assertEquals("error", result);
		Mockito.verify(personnageBS, Mockito.never()).attribute(
				Mockito.any(PersonnageWorkBO.class), Mockito.any(UserBO.class), Mockito.any(UserBO.class));	
	}
	
	
	@Test
	public void testAttribute_Success_NewGm() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(user);
		Mockito.when(personnageBS.loadPersonnageAsPlayer(personnageId, user)).thenReturn(personnageWork);
		
		String newGameMasterName = "Alfred";
		UserBO newGameMaster = AuthentificationBSTest.buildUser(newGameMasterName);
		Mockito.when(authentificationBS.getUser(newGameMasterName)).thenReturn(newGameMaster);
		
		// Execute
		attributeGameMasterAction.setNewGameMasterName(newGameMasterName);
		attributeGameMasterAction.setPersonnageId(personnageId);
		String result = attributeGameMasterAction.attribute();
		
		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(personnageBS).attribute(personnageWork, user, newGameMaster);
	}
	
}
