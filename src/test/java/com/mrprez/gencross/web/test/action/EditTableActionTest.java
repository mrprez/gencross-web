package com.mrprez.gencross.web.test.action;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.EditTableAction;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.TableMessageBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAdminBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.bs.face.ITableBS;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class EditTableActionTest extends AbstractActionTest {

	@Mock
	private IPersonnageBS personnageBS;

	@Mock
	private ITableBS tableBS;

	@Mock
	private IAdminBS adminBS;

	@InjectMocks
	private EditTableAction editTableAction;



	@Test
	public void testExecute_Fail() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer id = 2;
		
		// Execute
		editTableAction.setId(id);
		String result = editTableAction.execute();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(editTableAction.getActionErrors().contains("Impossible de charger cette table"));
	}
	
	
	@Test
	public void testExecute_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer id = 2;
		Collection<PersonnageWorkBO> addablePersonnages = Arrays.asList(new PersonnageWorkBO());
		PersonnageWorkBO personnageWork1 = new PersonnageWorkBO();
		personnageWork1.setName("PJ B");
		personnageWork1.setPlayer(new UserBO());
		PersonnageWorkBO personnageWork2 = new PersonnageWorkBO();
		personnageWork2.setName("PJ A");
		personnageWork2.setPlayer(new UserBO());
		PersonnageWorkBO personnageWork3 = new PersonnageWorkBO();
		personnageWork3.setName("PNJ B");
		PersonnageWorkBO personnageWork4 = new PersonnageWorkBO();
		personnageWork4.setName("PNJ A");
		TableBO table = new TableBO();
		table.setPersonnages(new HashSet<PersonnageWorkBO>(Arrays.asList(personnageWork1, personnageWork2, personnageWork3, personnageWork4)));
		Mockito.when(tableBS.getAddablePersonnages(table)).thenReturn(addablePersonnages);
		Mockito.when(tableBS.getTableForGM(id, user)).thenReturn(table);
		
		// Execute
		editTableAction.setId(id);
		String result = editTableAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals(2, editTableAction.getPjList().size());
		Iterator<PersonnageWorkBO> pjIt = editTableAction.getPjList().iterator();
		Assert.assertEquals(personnageWork2, pjIt.next());
		Assert.assertEquals(personnageWork1, pjIt.next());
		Assert.assertEquals(2, editTableAction.getPnjList().size());
		Iterator<PersonnageWorkBO> pnjIt = editTableAction.getPnjList().iterator();
		Assert.assertEquals(personnageWork4, pnjIt.next());
		Assert.assertEquals(personnageWork3, pnjIt.next());
		Assert.assertEquals(addablePersonnages, editTableAction.getAddablePersonnage());
	}


	@Test
	public void testTransformInPNJ_Fail() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);

		// Execute
		String result = editTableAction.transformInPNJ();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(editTableAction.getActionErrors().contains("Vous n'êtes pas MJ de ce personnage."));
	}
	
	
	@Test
	public void testTransformInPNJ_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageBS.loadPersonnageAsGameMaster(personnageId , user)).thenReturn(personnageWork);
		
		// Execute
		editTableAction.setPersonnageId(personnageId);
		String result = editTableAction.transformInPNJ();

		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(personnageBS).attribute(personnageWork, null, user);
	}


	@Test
	public void testBindPersonnage_Fail_NoPersonnage() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer id = 2;
		TableBO table = new TableBO();
		table.setPersonnages(new HashSet<PersonnageWorkBO>());
		Mockito.when(tableBS.getTableForGM(id, user)).thenReturn(table);
		
		// Execute
		editTableAction.setId(id);
		String result = editTableAction.bindPersonnage();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertTrue(editTableAction.getActionErrors().contains("Veillez selectionner un personnage à ajouter à la table."));
	}
	
	
	@Test
	public void testBindPersonnage_Fail_BadGameMaster() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer id = 2;
		Integer personnageId = 3;
		
		// Execute
		editTableAction.setId(id);
		editTableAction.setPersonnageId(personnageId);
		String result = editTableAction.bindPersonnage();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(editTableAction.getActionErrors().contains("Vous n'êtes pas MJ de ce personnage ou de cette table."));
	}
	
	
	@Test
	public void testBindPersonnage_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer id = 2;
		Integer personnageId = 3;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(tableBS.addPersonnageToTable(id, personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		editTableAction.setId(id);
		editTableAction.setPersonnageId(personnageId);
		String result = editTableAction.bindPersonnage();

		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(tableBS).addPersonnageToTable(id, personnageId, user);
	}


	@Test
	public void testUnbindPersonnage_Fail() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer id = 2;
		Integer personnageId = 3;
		
		// Execute
		editTableAction.setId(id);
		editTableAction.setPersonnageId(personnageId);
		String result = editTableAction.unbindPersonnage();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(editTableAction.getActionErrors().contains("Vous n'êtes pas MJ de ce personnage ou de cette table."));
	}
	
	
	@Test
	public void testUnbindPersonnage_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer id = 2;
		Integer personnageId = 3;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(tableBS.addPersonnageToTable(id, personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		editTableAction.setId(id);
		editTableAction.setPersonnageId(personnageId);
		String result = editTableAction.unbindPersonnage();

		// Check
		Assert.assertEquals("error", result);
		Mockito.verify(tableBS).removePersonnageFromTable(id, personnageId, user);
	}


	@Test
	public void testRefreshMessages() throws Exception {
		// Prepare
		Integer id = 2;
		TableMessageBO message1 = new TableMessageBO();
		message1.setTableId(2);
		TableMessageBO message2 = new TableMessageBO();
		message2.setTableId(1);
		TableMessageBO message3 = new TableMessageBO();
		message3.setTableId(2);
		Mockito.when(tableBS.connectTableMailBox()).thenReturn(Arrays.asList(message1, message2, message3));

		// Execute
		editTableAction.setId(id);
		String result = editTableAction.refreshMessages();

		// Check
		Assert.assertEquals("success", result);
		Assert.assertEquals(2, editTableAction.getLoadedMessageNumber().intValue());
	}


	@Test
	public void testAddPersonnage_Fail_NoPersonnageName() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer id = 2;
		TableBO table = new TableBO();
		table.setPersonnages(new HashSet<PersonnageWorkBO>());
		Mockito.when(tableBS.getTableForGM(id, user)).thenReturn(table);
		
		// Execute
		editTableAction.setPersonnageName("");
		editTableAction.setId(id);
		String result = editTableAction.addPersonnage();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertTrue(editTableAction.getActionErrors().contains("Veuillez renseigner un nom pour ce personnage."));
	}
	
	
	@Test
	public void testAddPersonnage_Fail_BadTable() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		String personnageName = "superman";
		Integer id = 2;
		
		// Execute
		editTableAction.setPersonnageName(personnageName);
		editTableAction.setId(id);
		String result = editTableAction.addPersonnage();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(editTableAction.getActionErrors().contains("Impossible de charger cette table"));
	}
	
	
	@Test
	public void testAddPersonnage_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		String personnageName = "superman";
		Integer id = 2;
		TableBO table = new TableBO();
		Mockito.when(tableBS.addNewPersonnageToTable(id, personnageName, user)).thenReturn(table);
		
		// Execute
		editTableAction.setPersonnageName(personnageName);
		editTableAction.setId(id);
		String result = editTableAction.addPersonnage();

		// Check
		Assert.assertEquals("success", result);
		Assert.assertEquals(table, editTableAction.getTable());
		Mockito.verify(tableBS).addNewPersonnageToTable(id, personnageName, user);
	}

	@Ignore
	@Test
	public void testNewMessage() throws Exception {
		// Prepare
		editTableAction.setLoadedMessageNumber(1);
		editTableAction.setPointPoolName("string_2");
		editTableAction.setPersonnageId(3);
		editTableAction.setMessage("string_4");
		editTableAction.setAddMessage("string_5");
		editTableAction.setPointPoolModification(6);
		editTableAction.setId(7);
		editTableAction.setPersonnageName("string_8");
		editTableAction.setMessageId(9);
		editTableAction.setSendMessage("string_10");

		// Execute
		String result = editTableAction.newMessage();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", editTableAction.getPjList());
		Assert.assertEquals("failTest", editTableAction.getMessageId());
		Assert.assertEquals("failTest", editTableAction.getMinPjPoints());
		Assert.assertEquals("failTest", editTableAction.getSendMessage());
		Assert.assertEquals("failTest", editTableAction.getMessage());
		Assert.assertEquals("failTest", editTableAction.getPointPoolName());
		Assert.assertEquals("failTest", editTableAction.getAddablePersonnage());
		Assert.assertEquals("failTest", editTableAction.getTable());
		Assert.assertEquals("failTest", editTableAction.getPersonnageId());
		Assert.assertEquals("failTest", editTableAction.getId());
		Assert.assertEquals("failTest", editTableAction.getAddMessage());
		Assert.assertEquals("failTest", editTableAction.getLoadedMessageNumber());
		Assert.assertEquals("failTest", editTableAction.getMaxPnjPoints());
		Assert.assertEquals("failTest", editTableAction.getMinPnjPoints());
		Assert.assertEquals("failTest", editTableAction.getMaxPjPoints());
		Assert.assertEquals("failTest", editTableAction.getPointPoolModification());
		Assert.assertEquals("failTest", editTableAction.getPoolPointList());
		Assert.assertEquals("failTest", editTableAction.getPnjList());
		Assert.assertEquals("failTest", editTableAction.getPersonnageName());
	}

	@Ignore
	@Test
	public void testRemoveMessage() throws Exception {
		// Prepare
		editTableAction.setLoadedMessageNumber(1);
		editTableAction.setPointPoolName("string_2");
		editTableAction.setPersonnageId(3);
		editTableAction.setMessage("string_4");
		editTableAction.setAddMessage("string_5");
		editTableAction.setPointPoolModification(6);
		editTableAction.setId(7);
		editTableAction.setPersonnageName("string_8");
		editTableAction.setMessageId(9);
		editTableAction.setSendMessage("string_10");

		// Execute
		String result = editTableAction.removeMessage();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", editTableAction.getPjList());
		Assert.assertEquals("failTest", editTableAction.getMessageId());
		Assert.assertEquals("failTest", editTableAction.getMinPjPoints());
		Assert.assertEquals("failTest", editTableAction.getSendMessage());
		Assert.assertEquals("failTest", editTableAction.getMessage());
		Assert.assertEquals("failTest", editTableAction.getPointPoolName());
		Assert.assertEquals("failTest", editTableAction.getAddablePersonnage());
		Assert.assertEquals("failTest", editTableAction.getTable());
		Assert.assertEquals("failTest", editTableAction.getPersonnageId());
		Assert.assertEquals("failTest", editTableAction.getId());
		Assert.assertEquals("failTest", editTableAction.getAddMessage());
		Assert.assertEquals("failTest", editTableAction.getLoadedMessageNumber());
		Assert.assertEquals("failTest", editTableAction.getMaxPnjPoints());
		Assert.assertEquals("failTest", editTableAction.getMinPnjPoints());
		Assert.assertEquals("failTest", editTableAction.getMaxPjPoints());
		Assert.assertEquals("failTest", editTableAction.getPointPoolModification());
		Assert.assertEquals("failTest", editTableAction.getPoolPointList());
		Assert.assertEquals("failTest", editTableAction.getPnjList());
		Assert.assertEquals("failTest", editTableAction.getPersonnageName());
	}
}
