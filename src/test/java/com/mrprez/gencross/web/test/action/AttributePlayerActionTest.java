package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.AttributePlayerAction;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class AttributePlayerActionTest extends AbstractActionTest {

	@Mock
	private IAuthentificationBS authentificationBS;
	
	@Mock
	private IPersonnageBS personnageBS;
	
	@InjectMocks
	private AttributePlayerAction attributePlayerAction;
	
	
	@Test
	public void testExecute_Fail() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		
		// Execute
		attributePlayerAction.setPersonnageId(personnageId);
		String result = attributePlayerAction.execute();
		
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
		Mockito.when(personnageBS.loadPersonnageAsGameMaster(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		attributePlayerAction.setPersonnageId(personnageId);
		String result = attributePlayerAction.execute();
		
		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals(personnageWork, attributePlayerAction.getPersonnageWork());
	}


	@Test
	public void testAttribute_Fail_NoPersonnage() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		
		// Execute
		attributePlayerAction.setPersonnageId(personnageId);
		String result = attributePlayerAction.attribute();
		
		// Check
		Assert.assertEquals("error", result);
		Mockito.verify(personnageBS, Mockito.never()).attribute(
				Mockito.any(PersonnageWorkBO.class), Mockito.any(UserBO.class), Mockito.any(UserBO.class));	
	}
	
	
	@Test
	public void testAttribute_Success_NoPlayer() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		String newPlayerName = AttributePlayerAction.NO_PLAYER_KEY;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(user);
		Mockito.when(personnageBS.loadPersonnageAsGameMaster(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		attributePlayerAction.setNewPlayerName(newPlayerName);
		attributePlayerAction.setPersonnageId(personnageId);
		String result = attributePlayerAction.attribute();
		
		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(personnageBS).attribute(personnageWork, null, user);
	}
	
	
	@Test
	public void testAttribute_Success_BadPlayer() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		String newPlayerName = "Alfred";
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageBS.loadPersonnageAsGameMaster(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		attributePlayerAction.setNewPlayerName(newPlayerName);
		attributePlayerAction.setPersonnageId(personnageId);
		String result = attributePlayerAction.attribute();
		
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
		personnageWork.setGameMaster(user);
		Mockito.when(personnageBS.loadPersonnageAsGameMaster(personnageId, user)).thenReturn(personnageWork);
		
		String newPlayerName = "Alfred";
		UserBO newPlayer = AuthentificationBSTest.buildUser(newPlayerName);
		Mockito.when(authentificationBS.getUser(newPlayerName)).thenReturn(newPlayer);
		
		// Execute
		attributePlayerAction.setNewPlayerName(newPlayerName);
		attributePlayerAction.setPersonnageId(personnageId);
		String result = attributePlayerAction.attribute();
		
		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(personnageBS).attribute(personnageWork, newPlayer, user);
	}
	
}
