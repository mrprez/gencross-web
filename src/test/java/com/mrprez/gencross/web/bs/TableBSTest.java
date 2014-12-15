package com.mrprez.gencross.web.bs;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.dao.face.ITableDAO;

public class TableBSTest {
	
	
	@Test
	public void testCreateTable() throws Exception{
		// Prepare
		String name = "Gotham";
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		String type = "DC-Comics";
		TableBS tableBS = new TableBS();
		ITableDAO tableDao = Mockito.mock(ITableDAO.class);
		tableBS.setTableDAO(tableDao);
		
		// Execute
		TableBO table = tableBS.createTable(name, gm, type);
		
		// Check
		Assert.assertNotNull(table);
		Assert.assertEquals(name, table.getName());
		Assert.assertEquals(gm, table.getGameMaster());
		Assert.assertEquals(table.getType(), type);
		Mockito.verify(tableDao).saveTable(table);
	}
	
	
	@Test
	public void testRemoveTable_Success_None() throws Exception{
		// Prepare
		TableBS tableBS = new TableBS();
		ITableDAO tableDao = Mockito.mock(ITableDAO.class);
		TableBO table = buildTable(AuthentificationBSTest.buildUser("batman"), "Gotham", "DC");
		table.getPersonnages().add(new PersonnageWorkBO());
		
		Mockito.when(tableDao.loadTable(table.getId())).thenReturn(table);
		tableBS.setTableDAO(tableDao);
		
		// Execute
		tableBS.removeTable(table.getId(), false, false, AuthentificationBSTest.buildUser("batman"));
		
		// Check
		
		
	}
	
	
	private TableBO buildTable(UserBO gm, String name, String type) {
		TableBO table = new TableBO();
		table.setGameMaster(gm);
		table.setId((int) (Math.random()*1000));
		table.setName(name);
		table.setType(type);
		
		return table;
	}

}
