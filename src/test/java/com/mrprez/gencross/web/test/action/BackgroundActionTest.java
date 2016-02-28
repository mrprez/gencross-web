package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.BackgroundAction;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.bs.face.ITableBS;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class BackgroundActionTest extends AbstractActionTest {

	@Mock
	private IPersonnageBS personnageBS;

	@Mock
	private ITableBS tableBS;

	@InjectMocks
	private BackgroundAction backgroundAction;



	@Test
	public void testSave() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		String background = "bg example";
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		backgroundAction.setPersonnageId(personnageId);
		backgroundAction.setBackground(background);
		String result = backgroundAction.save();

		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(personnageBS).savePersonnageBackground(personnageWork, background);
	}


	@Test
	public void testExecute() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setBackground("bg example");
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		backgroundAction.setPersonnageId(personnageId);
		String result = backgroundAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals(personnageWork, backgroundAction.getPersonnageWork());
		Assert.assertEquals("bg example", backgroundAction.getBackground());
	}
	
	
	@Test
	public void testGetIsGameMaster_Fail_NoGameMaster() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		
		// Execute
		backgroundAction.setPersonnageWork(personnageWork);
		boolean result = backgroundAction.getIsGameMaster();
		
		// Check
		Assert.assertFalse(result);
	}
	
	
	@Test
	public void testGetIsGameMaster_Fail_BadGameMaster() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(AuthentificationBSTest.buildUser("robin"));
		
		// Execute
		backgroundAction.setPersonnageWork(personnageWork);
		boolean result = backgroundAction.getIsGameMaster();
		
		// Check
		Assert.assertFalse(result);
	}
	
	
	@Test
	public void testGetIsGameMaster_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(AuthentificationBSTest.buildUser("batman"));
		
		// Execute
		backgroundAction.setPersonnageWork(personnageWork);
		boolean result = backgroundAction.getIsGameMaster();
		
		// Check
		Assert.assertTrue(result);
	}
}
