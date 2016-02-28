package com.mrprez.gencross.web.test.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.PoolPoint;
import com.mrprez.gencross.history.HistoryItem;
import com.mrprez.gencross.web.action.EditPersonnageAction;
import com.mrprez.gencross.web.bo.PersonnageDataBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.bs.face.ITableBS;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.mrprez.gencross.web.test.bs.PersonnageWorkBSTest;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

@RunWith(MockitoJUnitRunner.class)
public class EditPersonnageActionTest extends AbstractActionTest {

	@Mock
	private IPersonnageBS personnageBS;

	@Mock
	private ITableBS tableBS;
	
	@Mock
	private ValueStack valueStack;

	@InjectMocks
	private EditPersonnageAction editPersonnageAction;



	@Test
	public void testSave_Fail() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		
		// Execute
		String result = editPersonnageAction.save();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(editPersonnageAction.getActionErrors().contains("Vous devez sélectionner un personnage avant de le modifier."));
	}
	
	
	@Test
	public void testSave_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Integer personnageId = 2;
		Mockito.when(personnageBS.loadPersonnage(personnageId , user)).thenReturn(personnageWork);
		
		// Execute
		editPersonnageAction.setPersonnageId(personnageId);
		String result = editPersonnageAction.save();

		// Check
		Assert.assertEquals("input", result);
		Mockito.verify(personnageBS).savePersonnage(personnageWork);
	}


	@Test
	public void testExecute() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		editPersonnageAction.setPersonnageId(personnageId);
		String result = editPersonnageAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals(personnageWork, editPersonnageAction.getPersonnageWork());
	}


	@Test
	public void testNextPhase_Fail() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		
		// Execute
		String result = editPersonnageAction.nextPhase();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(editPersonnageAction.getActionErrors().contains("Vous devez sélectionner un personnage avant de le modifier."));
	}
	
	
	@Test
	public void testNextPhase_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		editPersonnageAction.setPersonnageId(personnageId);
		String result = editPersonnageAction.nextPhase();

		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(personnageBS).nextPhase(personnageWork);
	}
	
	
	@Test
	public void testValidatePersonnage_Fail() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		
		// Execute
		String result = editPersonnageAction.validatePersonnage();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(editPersonnageAction.getActionErrors().contains("Vous devez sélectionner un personnage avant de le modifier."));
	}
	
	
	@Test
	public void testValidatePersonnage_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		editPersonnageAction.setPersonnageId(personnageId);
		String result = editPersonnageAction.validatePersonnage();

		// Check
		Assert.assertEquals("input", result);
		Mockito.verify(personnageBS).validatePersonnage(personnageWork);
	}


	@Test
	public void testUnvalidatePersonnage_Fail() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		
		// Execute
		String result = editPersonnageAction.unvalidatePersonnage();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(editPersonnageAction.getActionErrors().contains("Vous devez sélectionner un personnage avant de le modifier."));
	}
	
	
	@Test
	public void testUnvalidatePersonnage_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		editPersonnageAction.setPersonnageId(personnageId);
		String result = editPersonnageAction.unvalidatePersonnage();

		// Check
		Assert.assertEquals("input", result);
		Mockito.verify(personnageBS).unvalidatePersonnage(personnageWork);
	}
	
	
	@Test
	public void testGetPointPool_Fail() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
				
		// Execute
		editPersonnageAction.setPersonnageId(personnageId);
		String result = editPersonnageAction.getPointPool();
		
		// Check
		Assert.assertEquals("invalidRequest", result);
	}
	
	
	@Test
	public void testGetPointPool_Success() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		ActionContext.getContext().setValueStack(valueStack);
		Integer personnageId = 2;
		String pointPoolName = "Expérience";
		PoolPoint pointPool = new PoolPoint(pointPoolName, 10);
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPersonnageData(new PersonnageDataBO());
		personnageWork.getPersonnageData().setPersonnage(new Personnage());
		personnageWork.getPersonnage().getPointPools().put(pointPoolName, pointPool);
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		editPersonnageAction.setPersonnageId(personnageId);
		editPersonnageAction.setPointPoolName(pointPoolName);
		String result = editPersonnageAction.getPointPool();
		
		// Check
		Assert.assertEquals("pointPool", result);
		Mockito.verify(valueStack).push(pointPool);
	}
	
	
	@Test
	public void testGetErrorList_Fail() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
				
		// Execute
		editPersonnageAction.setPersonnageId(personnageId);
		String result = editPersonnageAction.getErrorList();
		
		// Check
		Assert.assertEquals("invalidRequest", result);
	}
	
	
	@Test
	public void testGetErrorList_Success() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		ActionContext.getContext().setValueStack(valueStack);
		Integer personnageId = 2;
		Personnage personnage = new Personnage();
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPersonnageData(new PersonnageDataBO());
		personnageWork.getPersonnageData().setPersonnage(personnage);
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		editPersonnageAction.setPersonnageId(personnageId);
		String result = editPersonnageAction.getErrorList();
		
		// Check
		Assert.assertEquals("errorList", result);
		Mockito.verify(valueStack).push(personnage);
	}
	
	
	@Test
	public void testGetHistoryItem_Fail() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
				
		// Execute
		editPersonnageAction.setPersonnageId(personnageId);
		String result = editPersonnageAction.getHistoryItem();
		
		// Check
		Assert.assertEquals("invalidRequest", result);
	}
	
	
	@Test
	public void testGetHistoryItem_Success() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		ActionContext.getContext().setValueStack(valueStack);
		Integer personnageId = 2;
		Personnage personnage = new Personnage();
		HistoryItem historyItem = new HistoryItem();
		personnage.getHistory().add(historyItem);
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPersonnageData(new PersonnageDataBO());
		personnageWork.getPersonnageData().setPersonnage(personnage);
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		editPersonnageAction.setPersonnageId(personnageId);
		editPersonnageAction.setHistoryItemIndex(0);
		String result = editPersonnageAction.getHistoryItem();
		
		// Check
		Assert.assertEquals("historyItem", result);
		Mockito.verify(valueStack).push(personnage);
		Mockito.verify(valueStack).push(historyItem);
	}
	
	
	@Test
	public void testGetProperty_Success() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		ActionContext.getContext().setValueStack(valueStack);
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		ServletActionContext.setRequest(request);
		Integer personnageId = 2;
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork();
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		editPersonnageAction.setPersonnageId(personnageId);
		editPersonnageAction.setPropertyAbsoluteName("Compétences#Médecine#Apprentissage");
		String result = editPersonnageAction.getProperty();
		
		// Check
		Assert.assertEquals("property", result);
		Mockito.verify(request).setAttribute("propertyNum", "5_8_2");
		Mockito.verify(valueStack).push(personnageWork.getPersonnage().getProperty("Compétences#Médecine#Apprentissage"));
	}
	
	
	@Test
	public void testGetProperty_Fail() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		
		// Execute
		editPersonnageAction.setPersonnageId(personnageId);
		String result = editPersonnageAction.getProperty();
		
		// Check
		Assert.assertEquals("invalidRequest", result);
	}
	
	@Test
	public void testGetIsGameMaster_Fail_NoGameMaster() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		
		// Execute
		editPersonnageAction.setPersonnageWork(personnageWork);
		boolean result = editPersonnageAction.getIsGameMaster();
		
		// Check
		Assert.assertFalse(result);
	}
	
	@Test
	public void testGetIsGameMaster_Fail_BadGameMaster() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(AuthentificationBSTest.buildUser("robin"));
		
		// Execute
		editPersonnageAction.setPersonnageWork(personnageWork);
		boolean result = editPersonnageAction.getIsGameMaster();
		
		// Check
		Assert.assertFalse(result);
	}
	
	@Test
	public void testGetIsGameMaster_Success() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(AuthentificationBSTest.buildUser("batman"));
		
		// Execute
		editPersonnageAction.setPersonnageWork(personnageWork);
		boolean result = editPersonnageAction.getIsGameMaster();
		
		// Check
		Assert.assertTrue(result);
	}
}
