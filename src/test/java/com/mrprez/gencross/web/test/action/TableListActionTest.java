package com.mrprez.gencross.web.test.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.web.action.TableListAction;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.bs.face.ITableBS;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class TableListActionTest extends AbstractActionTest {

	@Mock
	private IPersonnageBS personnageBS;

	@Mock
	private ITableBS tableBS;

	@InjectMocks
	private TableListAction tableListAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Collection<PluginDescriptor> tableTypeList = new ArrayList<PluginDescriptor>();
		Set<TableBO> tableList = new HashSet<TableBO>();
		Mockito.when(personnageBS.getAvailablePersonnageTypes()).thenReturn(tableTypeList);
		Mockito.when(tableBS.getTableListForUser(user)).thenReturn(tableList);
		
		// Execute
		String result = tableListAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals(tableTypeList, tableListAction.getTableTypeList());
		Assert.assertEquals(tableList, tableListAction.getTableList());
	}


	@Test
	public void testAddTable_Fail() throws Exception {
		// Execute
		tableListAction.setTableName("");
		String result = tableListAction.addTable();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(tableListAction.getActionErrors().contains("Le nom de la table à créer ne doit pas être vide"));
	}
	
	
	@Test
	public void testAddTable_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		String tableName = "Metropolis";
		String tableType = "DC";
		
		// Execute
		tableListAction.setTableName(tableName);
		tableListAction.setTableType(tableType);
		String result = tableListAction.addTable();

		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(tableBS).createTable(tableName, user, tableType);
	}
	
	
	@Test
	public void testRemoveTable() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer tableId = 2;
		String personnageDeletion = "ONLY_PJ";
		
		// Execute
		tableListAction.setTableId(tableId);
		tableListAction.setPersonnageDeletion(personnageDeletion);
		String result = tableListAction.removeTable();

		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(tableBS).removeTable(tableId, true, false, user);
	}
}
