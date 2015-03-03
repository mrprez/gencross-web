package com.mrprez.gencross.web.bs;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.PoolPoint;
import com.mrprez.gencross.disk.PersonnageFactory;
import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.web.bo.ParamBO;
import com.mrprez.gencross.web.bo.PersonnageDataBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.TableMessageBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.util.BusinessException;
import com.mrprez.gencross.web.dao.ParamDAO;
import com.mrprez.gencross.web.dao.face.IMailResource;
import com.mrprez.gencross.web.dao.face.IPersonnageDAO;
import com.mrprez.gencross.web.dao.face.ITableDAO;
import com.mrprez.gencross.web.dao.face.IUserDAO;

public class TableBSTest {
	
	
	@Test
	public void testCreateTable() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		
		String name = "Gotham";
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		String type = "DC-Comics";
		
		// Execute
		TableBO table = tableBS.createTable(name, gm, type);
		
		// Check
		Assert.assertNotNull(table);
		Assert.assertEquals(name, table.getName());
		Assert.assertEquals(gm, table.getGameMaster());
		Assert.assertEquals(table.getType(), type);
		Mockito.verify(tableBS.getTableDAO()).saveTable(table);
	}
	
	
	@Test
	public void testRemoveTable_Success_None() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildTable(gm, "Gotham", "DC");
		PersonnageWorkBO pj = new PersonnageWorkBO();
		pj.setGameMaster(gm);
		pj.setPlayer(AuthentificationBSTest.buildUser("robin"));
		table.getPersonnages().add(pj);
		PersonnageWorkBO pnj = new PersonnageWorkBO();
		pnj.setGameMaster(gm);
		table.getPersonnages().add(pnj);
		Mockito.when(tableBS.getTableDAO().loadTable(table.getId())).thenReturn(table);
		
		IPersonnageDAO personnageDao = Mockito.mock(IPersonnageDAO.class);
		tableBS.setPersonnageDAO(personnageDao);
		
		// Execute
		tableBS.removeTable(table.getId(), false, false, AuthentificationBSTest.buildUser("batman"));
		
		// Check
		Mockito.verify(tableBS.getTableDAO()).deleteTable(table);
		Mockito.verify(personnageDao, Mockito.never()).deletePersonnage(Mockito.any(PersonnageWorkBO.class));
	}
	
	
	@Test
	public void testRemoveTable_Success_Pj() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildTable(gm, "Gotham", "DC");
		PersonnageWorkBO pj = new PersonnageWorkBO();
		pj.setGameMaster(gm);
		pj.setPlayer(AuthentificationBSTest.buildUser("robin"));
		table.getPersonnages().add(pj);
		PersonnageWorkBO pnj = new PersonnageWorkBO();
		pnj.setGameMaster(gm);
		table.getPersonnages().add(pnj);
		Mockito.when(tableBS.getTableDAO().loadTable(table.getId())).thenReturn(table);
		
		IPersonnageDAO personnageDao = Mockito.mock(IPersonnageDAO.class);
		tableBS.setPersonnageDAO(personnageDao);
		
		// Execute
		tableBS.removeTable(table.getId(), true, false, AuthentificationBSTest.buildUser("batman"));
		
		// Check
		Mockito.verify(tableBS.getTableDAO()).deleteTable(table);
		Mockito.verify(personnageDao).deletePersonnage(pj);
		Mockito.verify(personnageDao, Mockito.never()).deletePersonnage(pnj);
	}
	
	
	@Test
	public void testRemoveTable_Success_Pnj() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildTable(gm, "Gotham", "DC");
		PersonnageWorkBO pj = new PersonnageWorkBO();
		pj.setGameMaster(gm);
		pj.setPlayer(AuthentificationBSTest.buildUser("robin"));
		table.getPersonnages().add(pj);
		PersonnageWorkBO pnj = new PersonnageWorkBO();
		pnj.setGameMaster(gm);
		table.getPersonnages().add(pnj);
		Mockito.when(tableBS.getTableDAO().loadTable(table.getId())).thenReturn(table);
		
		// Execute
		tableBS.removeTable(table.getId(), false, true, AuthentificationBSTest.buildUser("batman"));
		
		// Check
		Mockito.verify(tableBS.getTableDAO()).deleteTable(table);
		Mockito.verify(tableBS.getPersonnageDAO()).deletePersonnage(pnj);
		Mockito.verify(tableBS.getPersonnageDAO(), Mockito.never()).deletePersonnage(pj);
	}
	
	
	@Test
	public void testRemoveTable_Success_All() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildTable(gm, "Gotham", "DC");
		PersonnageWorkBO pj = new PersonnageWorkBO();
		pj.setGameMaster(gm);
		pj.setPlayer(AuthentificationBSTest.buildUser("robin"));
		table.getPersonnages().add(pj);
		PersonnageWorkBO pnj = new PersonnageWorkBO();
		pnj.setGameMaster(gm);
		table.getPersonnages().add(pnj);
		Mockito.when(tableBS.getTableDAO().loadTable(table.getId())).thenReturn(table);
		
		IPersonnageDAO personnageDao = Mockito.mock(IPersonnageDAO.class);
		tableBS.setPersonnageDAO(personnageDao);
		
		// Execute
		tableBS.removeTable(table.getId(), true, true, AuthentificationBSTest.buildUser("batman"));
		
		// Check
		Mockito.verify(tableBS.getTableDAO()).deleteTable(table);
		Mockito.verify(personnageDao).deletePersonnage(pnj);
		Mockito.verify(personnageDao).deletePersonnage(pj);
	}
	
	
	@Test
	public void testRemoveTable_Fail() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildTable(gm, "Gotham", "DC");
		Mockito.when(tableBS.getTableDAO().loadTable(table.getId())).thenReturn(table);
		
		// Execute
		BusinessException exception = null;
		try{
			tableBS.removeTable(table.getId(), true, true, AuthentificationBSTest.buildUser("robin"));
		}catch(BusinessException e){
			exception = e;
		}
		
		// Check
		Assert.assertNotNull(exception);
		Assert.assertEquals("User is not table game master", exception.getMessage());
	}
	
	
	
	
	
	public static TableBO buildTable(UserBO gm, String name, String type) {
		TableBO table = new TableBO();
		table.setGameMaster(gm);
		table.setId((int) (Math.random()*1000));
		table.setName(name);
		table.setType(type);
		table.setPersonnages(new HashSet<PersonnageWorkBO>());
		
		return table;
	}
	
	
	@Test
	public void testGetTableListForUser() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		UserBO user = AuthentificationBSTest.buildUser("batman");
		List<TableBO> tableList = new ArrayList<TableBO>();
		tableList.add(buildTable(user, "Gotham", "DC-Comics"));
		tableList.add(buildTable(user, "Mystérieuses caraïbes", "Pavillon Noir"));
		Mockito.when(tableBS.getTableDAO().getTableFromGM(user)).thenReturn(tableList);
		
		// Execute
		Set<TableBO> returnedTableList = tableBS.getTableListForUser(user);
		
		// Check
		Assert.assertEquals(new HashSet<TableBO>(tableList), returnedTableList);
		Mockito.verify(tableBS.getTableDAO()).getTableFromGM(user);
	}
	
	
	@Test
	public void testGetTableForGM_Success() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		UserBO user = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildTable(user, "Gotham", "DC-Comics");
		table.setMessages(new TreeSet<TableMessageBO>());
		Integer tableId = table.getId();
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		
		// Execute
		TableBO returnedTable = tableBS.getTableForGM(tableId, user);
		
		// Check
		Assert.assertEquals(table, returnedTable);
		Mockito.verify(tableBS.getTableDAO()).loadTable(tableId);
	}
	
	@Test
	public void testGetTableForGM_Fail() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		UserBO user = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildTable(user, "Gotham", "DC-Comics");
		table.setMessages(new TreeSet<TableMessageBO>());
		Integer tableId = table.getId();
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		
		// Execute
		TableBO returnedTable = tableBS.getTableForGM(tableId, AuthentificationBSTest.buildUser("robin"));
		
		// Check
		Assert.assertNull(returnedTable);
		Mockito.verify(tableBS.getTableDAO()).loadTable(tableId);
	}
	
	@Test
	public void testGetPjList() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		TableBO table = buildTable(AuthentificationBSTest.buildUser("batman"), "Gotham", "DC-Comics");
		PersonnageWorkBO pj1 = new PersonnageWorkBO();
		pj1.setPlayer(AuthentificationBSTest.buildUser("robin"));
		PersonnageWorkBO pj2 = new PersonnageWorkBO();
		pj2.setPlayer(table.getGameMaster());
		PersonnageWorkBO pnj1 = new PersonnageWorkBO();
		PersonnageWorkBO pnj2 = new PersonnageWorkBO();
		table.getPersonnages().addAll(Arrays.asList(pnj1, pj1, pj2, pnj2));
		
		Integer tableId = table.getId();
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		
		// Execute
		Collection<PersonnageWorkBO> returnedPjList = tableBS.getPjList(tableId);
		
		// Check
		Assert.assertEquals(2, returnedPjList.size());
		Assert.assertTrue(returnedPjList.contains(pj1));
		Assert.assertTrue(returnedPjList.contains(pj2));
	}
	
	
	@Test
	public void testGetPointPoolList() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		TableBO table = buildTable(AuthentificationBSTest.buildUser("batman"), "Gotham", "DC-Comics");
		Integer tableId = table.getId();
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn( table );
		Personnage newPersonnage = new Personnage();
		newPersonnage.getPointPools().put("Attributs", new PoolPoint("Attributs", 10));
		newPersonnage.getPointPools().put("Compétences", new PoolPoint("Compétences", 100));
		newPersonnage.getPointPools().put("Expérience", new PoolPoint("Expérience", 0));
		Mockito.when(tableBS.getPersonnageFactory().buildNewPersonnage("DC-Comics")).thenReturn(newPersonnage);
		
		// Execute
		Collection<String> returnedList = tableBS.getPointPoolList(tableId);
		
		// Check
		Assert.assertEquals(newPersonnage.getPointPools().size(), returnedList.size());
		for(String poolName : newPersonnage.getPointPools().keySet()){
			Assert.assertTrue(returnedList.contains(poolName));
		}
	}
	
	
	@Test
	public void testAddNewPersonnageToTable_Success() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildTable(gm, "Gotham", "DC-Comics");
		Integer tableId = table.getId();
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		Personnage newPersonnage = new Personnage();
		newPersonnage.setPluginDescriptor(new PluginDescriptor());
		newPersonnage.getPluginDescriptor().setName("DC-Comics");
		Mockito.when(tableBS.getPersonnageFactory().buildNewPersonnage("DC-Comics")).thenReturn(newPersonnage);
		
		// Execute
		TableBO returnedTable = tableBS.addNewPersonnageToTable(tableId, "Joker", gm);
		
		// Check
		Assert.assertFalse(returnedTable.getPersonnages().isEmpty());
		PersonnageWorkBO personnageWork = returnedTable.getPersonnages().iterator().next();
		Assert.assertEquals(gm, personnageWork.getGameMaster());
		Assert.assertEquals("Joker", personnageWork.getName());
		
		Mockito.verify(tableBS.getPersonnageDAO()).savePersonnage(personnageWork.getPersonnageData());
		Mockito.verify(tableBS.getPersonnageDAO()).savePersonnage(personnageWork.getValidPersonnageData());
		Mockito.verify(tableBS.getPersonnageDAO()).savePersonnageWork(personnageWork);
	}
	
	
	@Test
	public void testAddNewPersonnageToTable_Fail_TableDoesNotExist() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		
		// Execute
		TableBO returnedTable = tableBS.addNewPersonnageToTable(10, "Joker", gm);
		
		// Check
		Assert.assertNull(returnedTable);
	}
	
	
	@Test
	public void testAddNewPersonnageToTable_Fail_NotMj() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		UserBO user = AuthentificationBSTest.buildUser("robin");
		TableBO table = buildTable(AuthentificationBSTest.buildUser("batman"), "Gotham", "DC-Comics");
		Integer tableId = table.getId();
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		
		// Execute
		TableBO returnedTable = tableBS.addNewPersonnageToTable(tableId, "Joker", user);
		
		// Check
		Assert.assertNull(returnedTable);
	}
	
	
	@Test
	public void testAddPersonnageToTable_Success() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildTable(gm, "Gotham", "DC-Comics");
		Integer tableId = table.getId();
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(gm);
		Integer personnageId = 10;
		personnageWork.setId(personnageId);
		Mockito.when(tableBS.getPersonnageDAO().loadPersonnageWork(personnageId)).thenReturn(personnageWork);
		
		// Execute
		PersonnageWorkBO returnedPersonnageWork = tableBS.addPersonnageToTable(tableId, personnageId, gm);
		
		// Check
		Assert.assertEquals(table, returnedPersonnageWork.getTable());
		Assert.assertEquals(returnedPersonnageWork, personnageWork);
		Assert.assertTrue(table.getPersonnages().contains(returnedPersonnageWork));
		Mockito.verify(tableBS.getPersonnageDAO()).loadPersonnageWork(personnageId);
	}
	
	
	@Test
	public void testAddPersonnageToTable_FailTableNotExist() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		
		// Execute
		PersonnageWorkBO returnedPersonnageWork = tableBS.addPersonnageToTable(9, 10, gm);
		
		// Check
		Assert.assertNull(returnedPersonnageWork);
	}
	
	
	@Test
	public void testAddPersonnageToTable_FailNotTableGm() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildTable(gm, "Gotham", "DC-Comics");
		Integer tableId = table.getId();
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		
		// Execute
		UserBO user = AuthentificationBSTest.buildUser("robin");
		PersonnageWorkBO returnedPersonnageWork = tableBS.addPersonnageToTable(tableId, 10, user);
		
		// Check
		Assert.assertNull(returnedPersonnageWork);
		Assert.assertTrue(table.getPersonnages().isEmpty());
	}
	
	
	@Test
	public void testAddPersonnageToTable_PersoNotExist() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildTable(gm, "Gotham", "DC-Comics");
		Integer tableId = table.getId();
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		
		// Execute
		PersonnageWorkBO returnedPersonnageWork = tableBS.addPersonnageToTable(tableId, 9, gm);
		
		// Check
		Assert.assertNull(returnedPersonnageWork);
		Assert.assertTrue(table.getPersonnages().isEmpty());
	}
	
	
	@Test
	public void testAddPersonnageToTable_FailNotPersoGm() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildTable(gm, "Gotham", "DC-Comics");
		Integer tableId = table.getId();
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		UserBO user = AuthentificationBSTest.buildUser("robin");
		personnageWork.setGameMaster(user);
		Integer personnageId = 10;
		personnageWork.setId(personnageId);
		Mockito.when(tableBS.getPersonnageDAO().loadPersonnageWork(personnageId)).thenReturn(personnageWork);
		
		// Execute
		PersonnageWorkBO returnedPersonnageWork = tableBS.addPersonnageToTable(tableId, personnageId, gm);
		
		// Check
		Assert.assertNull(returnedPersonnageWork);
		Assert.assertTrue(table.getPersonnages().isEmpty());
		Assert.assertNull(personnageWork.getTable());
	}
	
	
	@Test
	public void testRemovePersonnageFromTable_Success() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildTable(gm, "Gotham", "DC-Comics");
		Integer tableId = table.getId();
		table.getPersonnages().add(new PersonnageWorkBO());
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		
		Integer personnageId = 20;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setId(personnageId);
		personnageWork.setTable(table);
		table.getPersonnages().add(personnageWork);
		Mockito.when(tableBS.getPersonnageDAO().loadPersonnageWork(personnageId)).thenReturn(personnageWork);
		
		// Execute
		PersonnageWorkBO returnedPersonnageWork = tableBS.removePersonnageFromTable(tableId, personnageId, gm);
		
		// Check
		Assert.assertEquals(personnageWork, returnedPersonnageWork);
		Assert.assertFalse(table.getPersonnages().contains(personnageWork));
		Assert.assertNull(personnageWork.getTable());
		Mockito.verify(tableBS.getPersonnageDAO()).loadPersonnageWork(personnageId);
		Mockito.verify(tableBS.getTableDAO()).loadTable(tableId);
	}
	
	
	@Test
	public void testRemovePersonnageFromTable_FailTableNotExists() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		Integer tableId = 10;
		Integer personnageId = 20;
		
		// Execute
		PersonnageWorkBO returnedPersonnageWork = tableBS.removePersonnageFromTable(tableId, personnageId, gm);
		
		// Check
		Assert.assertNull(returnedPersonnageWork);
	}
	
	
	@Test
	public void testRemovePersonnageFromTable_FailNoGmTable() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildTable(gm, "Gotham", "DC-Comics");
		Integer tableId = table.getId();
		table.getPersonnages().add(new PersonnageWorkBO());
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		
		Integer personnageId = 20;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setId(personnageId);
		personnageWork.setTable(table);
		table.getPersonnages().add(personnageWork);
		Mockito.when(tableBS.getPersonnageDAO().loadPersonnageWork(personnageId)).thenReturn(personnageWork);
		
		// Execute
		UserBO user = AuthentificationBSTest.buildUser("robin");
		PersonnageWorkBO returnedPersonnageWork = tableBS.removePersonnageFromTable(tableId, personnageId, user);
		
		// Check
		Assert.assertNull(returnedPersonnageWork);
		Assert.assertTrue(table.getPersonnages().contains(personnageWork));
		Assert.assertNotNull(personnageWork.getTable());
	}
	
	
	@Test
	public void testRemovePersonnageFromTable_FailPersonnageNotExists() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildTable(gm, "Gotham", "DC-Comics");
		Integer tableId = table.getId();
		table.getPersonnages().add(new PersonnageWorkBO());
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		
		Integer personnageId = 20;
		
		// Execute
		PersonnageWorkBO returnedPersonnageWork = tableBS.removePersonnageFromTable(tableId, personnageId, gm);
		
		// Check
		Assert.assertNull(returnedPersonnageWork);
		Assert.assertEquals( 1, table.getPersonnages().size());
	}
	
	
	@Test
	public void testRemovePersonnageFromTable_FailNotTablePersonnage() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildTable(gm, "Gotham", "DC-Comics");
		Integer tableId = table.getId();
		table.getPersonnages().add(new PersonnageWorkBO());
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		
		Integer personnageId = 20;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setId(personnageId);
		personnageWork.setTable(buildTable(gm, "Metropolis", "DC-Comics"));
		Mockito.when(tableBS.getPersonnageDAO().loadPersonnageWork(personnageId)).thenReturn(personnageWork);
		
		// Execute
		PersonnageWorkBO returnedPersonnageWork = tableBS.removePersonnageFromTable(tableId, personnageId, gm);
		
		// Check
		Assert.assertNull(returnedPersonnageWork);
		Assert.assertFalse(table.getPersonnages().contains(personnageWork));
	}
	
	
	@Test
	public void testGetPersonnageTable_Success() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		
		TableBO table = buildTable(AuthentificationBSTest.buildUser("batman"), "Gotham", "DC-Comics");
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		table.getPersonnages().add(personnageWork);
		personnageWork.setTable(table);
		Mockito.when(tableBS.getTableDAO().getPersonnageTable(personnageWork)).thenReturn(table);
		
		// Execute
		TableBO resultTable = tableBS.getPersonnageTable(personnageWork);
		
		// Check
		Assert.assertEquals(table, resultTable);
	}
	
	
	@Test
	public void testAddPointsToPj_Fail() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		
		TableBO table = buildTable(AuthentificationBSTest.buildUser("batman"), "Gotham", "DC-Comics");
		Integer tableId = table.getId();
		for(int i=0; i<5; i++){
			Personnage personnage = new Personnage();
			personnage.getPointPools().put("Compétences", new PoolPoint("Compétences", 50));
			PersonnageDataBO personnageData = new PersonnageDataBO();
			personnageData.setPersonnage(personnage);
			PersonnageWorkBO personnageWork = new PersonnageWorkBO();
			personnageWork.setPersonnageData(personnageData);
			table.getPersonnages().add(personnageWork);
		}
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		
		// Execute
		UserBO user = AuthentificationBSTest.buildUser("robin");
		String errorMessage = tableBS.addPointsToPj(tableId, user, "Compétences", 10);
		
		// Check
		Assert.assertEquals("Vous n'ête pas propriétaire de cette table", errorMessage);
		for(PersonnageWorkBO personnageWork : table.getPersonnages()){
			Assert.assertEquals(50,  personnageWork.getPersonnage().getPointPools().get("Compétences").getTotal() );
		}
		
	}
	
	
	@Test
	public void testAddPointsToPj_Success() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		
		UserBO gm =  AuthentificationBSTest.buildUser("batman");
		TableBO table = buildTable(gm, "Gotham", "DC-Comics");
		Integer tableId = table.getId();
		Map<PersonnageWorkBO, Integer> expectedPoints = new HashMap<PersonnageWorkBO, Integer>();
		for(int i=0; i<7; i++){
			Personnage personnage = new Personnage();
			personnage.getPointPools().put("Compétences", new PoolPoint("Compétences", 50+i));
			PersonnageDataBO personnageData = new PersonnageDataBO();
			personnageData.setPersonnage(personnage);
			PersonnageWorkBO personnageWork = new PersonnageWorkBO();
			personnageWork.setPersonnageData(personnageData);
			if( i<5 ){
				personnageWork.setPlayer(AuthentificationBSTest.buildUser("player"+i));
				expectedPoints.put(personnageWork, 60+i);
			}else{
				expectedPoints.put(personnageWork, 50+i);
			}
			
			table.getPersonnages().add(personnageWork);
			
		}
		
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		
		// Execute
		String errorMessage = tableBS.addPointsToPj(tableId, gm, "Compétences", 10);
		
		// Check
		Assert.assertNull(errorMessage);
		for(PersonnageWorkBO personnageWork : table.getPersonnages()){
			Assert.assertEquals((int)expectedPoints.get(personnageWork),  (int)personnageWork.getPersonnage().getPointPools().get("Compétences").getTotal() );
		}
	}
	
	
	@Test
	public void testGetAddablePersonnages() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		TableBO table = buildTable(AuthentificationBSTest.buildUser("batman"), "Gotham", "DC-Comics");
		Collection<PersonnageWorkBO> addablePersonnageList = new ArrayList<PersonnageWorkBO>();
		addablePersonnageList.add(new PersonnageWorkBO());
		Mockito.when(tableBS.getAddablePersonnages(table)).thenReturn(addablePersonnageList);
		
		// Execute
		Collection<PersonnageWorkBO> methodResult = tableBS.getAddablePersonnages(table);
		
		// Check
		Assert.assertEquals(addablePersonnageList, methodResult);
	}
	
	
	@Test
	public void testAddMessageToTable_Success() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildTable(gm, "Gotham", "DC-Comics");
		Integer tableId = table.getId();
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		
		// Execute
		String message = "Message to test";
		tableBS.addMessageToTable(message, tableId, gm);
		
		// Check
		Mockito.verify(tableBS.getTableDAO()).saveTable(table);
		Assert.assertEquals(1, table.getMessages().size());
		TableMessageBO resultMessage = table.getMessages().iterator().next();
		Assert.assertEquals(gm, resultMessage.getAuthor());
		Assert.assertEquals(message, resultMessage.getData());
		Assert.assertTrue(resultMessage.getDate().getTime()-new Date().getTime() < 10*1000);
	}
	
	
	@Test
	public void testAddMessageToTable_Fail() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildTable(gm, "Gotham", "DC-Comics");
		Integer tableId = table.getId();
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		
		// Execute
		String message = "Message to test";
		UserBO user = AuthentificationBSTest.buildUser("robin");
		BusinessException businessException = null;
		try{
			tableBS.addMessageToTable(message, tableId, user);
		}catch(BusinessException e){
			businessException = e;
		}
		
		// Check
		Assert.assertNotNull(businessException);
		Assert.assertEquals("The author is not the table game master", businessException.getMessage());
	}
	
	
	@Test
	public void testAddSendMessage_Success() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildFullTable(gm, "Gotham", "DC-Comics");
		Integer tableId = table.getId();
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		Mockito.when(tableBS.getParamDAO().getParam(ParamBO.TABLE_ADRESS)).thenReturn(ParamsBSTest.buildParamBO(ParamBO.TABLE_ADRESS, "admin@gmail.com"));
		
		// Execute
		String message = "Message to test";
		tableBS.addSendMessage(message, tableId, gm);
		
		// Check
		Mockito.verify(tableBS.getTableDAO()).saveTable(table);
		Set<String> toAddresses = new HashSet<String>(Arrays.asList("catwoman@gmail.com", "robin@gmail.com", "batman@gmail.com"));
		Mockito.verify(tableBS.getMailResource()).send(toAddresses, "admin@gmail.com", "["+table.getId()+"] "+table.getName(), message);
	}
	
	
	@Test
	public void testAddSendMessage_Fail() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildFullTable(gm, "Gotham", "DC-Comics");
		Integer tableId = table.getId();
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		
		// Execute
		String message = "Message to test";
		UserBO user = AuthentificationBSTest.buildUser("robin");
		BusinessException businessException = null;
		try{
			tableBS.addSendMessage(message, tableId, user);
		}catch(BusinessException be){
			businessException = be;
		}
		
		// Check
		Assert.assertNotNull(businessException);
		Assert.assertEquals("The author is not the table game master", businessException.getMessage());
		Mockito.verify(tableBS.getTableDAO(), Mockito.never()).saveTable(Mockito.any(TableBO.class));
		Mockito.verify(tableBS.getMailResource(), Mockito.never()).send(Mockito.anyCollectionOf(String.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}
	
	
	@Test
	public void testRemoveMessageFromTable_Fail() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildFullTable(gm, "Gotham", "DC-Comics");
		Integer tableId = table.getId();
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		
		// Execute
		UserBO user = AuthentificationBSTest.buildUser("robin");
		BusinessException businessException = null;
		try{
			tableBS.removeMessageFromTable(20, tableId, user);
		}catch(BusinessException be){
			businessException = be;
		}
		
		// Check
		Assert.assertNotNull(businessException);
		Assert.assertEquals("User is not the table game master", businessException.getMessage());
		Assert.assertEquals(2, table.getMessages().size());
	}
	
	
	@Test
	public void testRemoveMessageFromTable_Success() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = buildFullTable(gm, "Gotham", "DC-Comics");
		Integer tableId = table.getId();
		Mockito.when(tableBS.getTableDAO().loadTable(tableId)).thenReturn(table);
		
		// Execute
		tableBS.removeMessageFromTable(20, tableId, gm);
		
		// Check
		Assert.assertEquals(1, table.getMessages().size());
	}
	
	
	@Test
	public void testConnectTableMailBox() throws Exception{
		// Prepare
		TableBS tableBS = buildMockedTableBS();
		UserBO gm1 = AuthentificationBSTest.buildUser("batman");
		TableBO table1 = buildFullTable(gm1, "Gotham", "DC-Comics");
		table1.setId(1);
		Mockito.when(tableBS.getTableDAO().loadTable(1)).thenReturn(table1);
		UserBO gm2 = AuthentificationBSTest.buildUser("superman");
		TableBO table2 = buildFullTable(gm2, "Metropolis", "DC-Comics");
		Mockito.when(tableBS.getTableDAO().loadTable(2)).thenReturn(table2);
		
		TableMessageBO message1 = buildTableMessage(null, "No table ID", "2015/03/01 15:55:55", 0);
		message1.setSubject("batcave");
		message1.setSenderMail("pinguin@gmail.com");
		message1.setTableId(null);
		TableMessageBO message2 = buildTableMessage(null, "No table", "2015/03/01 15:00:52", 3);
		message2.setSubject("[3]batcar");
		message2.setSenderMail("joker@gmail.com");
		TableMessageBO message3 = buildTableMessage(null, "No author", "2015/03/01 15:59:59", 1);
		message3.setTitle("gift");
		TableMessageBO message4 = buildTableMessage(null, "Normal message", "2015/03/01 17:12:45", 1);
		message4.setSubject("Signal");
		message4.setSenderMail("robin@gmail.com");
		Mockito.when(tableBS.getUserDAO().getUserFromMail("robin@gmail.com")).thenReturn(AuthentificationBSTest.buildUser("robin"));
		Collection<TableMessageBO> messageList = Arrays.asList(message1, message2, message3, message4);
		Mockito.when(tableBS.getMailResource().getMails()).thenReturn(messageList);
		
		Mockito.when(tableBS.getParamDAO().getParam(ParamBO.TABLE_ADRESS)).thenReturn(ParamsBSTest.buildParamBO(ParamBO.TABLE_ADRESS, "table.adress@gmail.com"));

		// Execute
		Collection<TableMessageBO> result = tableBS.connectTableMailBox();
		
		// Check
		Assert.assertEquals(messageList, result);
		Assert.assertEquals(4, table1.getMessages().size());
		boolean messageFound = false;
		for(TableMessageBO message : table1.getMessages()){
			if(message.getDate().equals(message3.getDate())){
				messageFound = true;
				Assert.assertEquals(message3.getData(), message.getData());
				Assert.assertNull(message.getSenderMail());
				Assert.assertEquals(message3.getTitle(), message.getTitle());
				Assert.assertNull(message.getAuthor());
				Assert.assertEquals(1, (int)message3.getTableId());
			}
		}
		Assert.assertTrue(messageFound);
		messageFound = false;
		for(TableMessageBO message : table1.getMessages()){
			if(message.getDate().equals(message4.getDate())){
				messageFound = true;
				Assert.assertEquals(message4.getData(), message.getData());
				Assert.assertEquals(message4.getSenderMail(), message.getSenderMail());
				Assert.assertEquals(message4.getTitle(), message.getTitle());
				Assert.assertEquals(message4.getAuthor(), message.getAuthor());
				Assert.assertEquals(1, (int)message4.getTableId());
			}
		}
		Assert.assertTrue(messageFound);
		Mockito.verify(tableBS.getMailResource()).send("pinguin@gmail.com", "table.adress@gmail.com", "Invalid subject: batcave", "Votre message n'a pas pu être associé à une table. Il faut que l'objet du mail contienne le numéro de la table entre crochet ('[<numero_table>]').\n\n\n\nNo table ID");
		Mockito.verify(tableBS.getMailResource()).send("joker@gmail.com", "table.adress@gmail.com", "Invalid subject: [3]batcar", "Votre message n'a pas pu être associé à une table. Il faut que l'objet du mail contienne le numéro de la table entre crochet ('[<numero_table>]').\n\n\n\nNo table");
	}
	
	
		
	private TableBS buildMockedTableBS(){
		TableBS tableBS = new TableBS();
		tableBS.setTableDAO(Mockito.mock(ITableDAO.class));
		tableBS.setPersonnageDAO(Mockito.mock(IPersonnageDAO.class));
		tableBS.setPersonnageFactory(Mockito.mock(PersonnageFactory.class));
		tableBS.setMailResource(Mockito.mock(IMailResource.class));
		tableBS.setParamDAO(Mockito.mock(ParamDAO.class));
		tableBS.setUserDAO(Mockito.mock(IUserDAO.class));
		
		return tableBS;
	}
	
	
	private static TableMessageBO buildTableMessage(UserBO author, String data, String date, int tableId) throws ParseException{
		TableMessageBO message = new TableMessageBO();
		message.setAuthor(author);
		message.setData(data);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		message.setDate(dateFormat.parse(date));
		message.setTableId(tableId);
		
		return message;
	}
	
	
	public static TableBO buildFullTable(UserBO gm, String name, String type) throws Exception{
		TableBO table = buildTable(gm, name, type);
		
		PersonnageWorkBO personnageWork1 = PersonnageWorkBSTest.buildPersonnageWork();
		personnageWork1.setGameMaster(gm);
		personnageWork1.setPlayer(AuthentificationBSTest.buildUser("robin"));
		table.getPersonnages().add(personnageWork1);
		PersonnageWorkBO personnageWork2 = PersonnageWorkBSTest.buildPersonnageWork();
		personnageWork2.setGameMaster(gm);
		personnageWork2.setPlayer(AuthentificationBSTest.buildUser("catwoman"));
		table.getPersonnages().add(personnageWork2);
		PersonnageWorkBO personnageWork3 = PersonnageWorkBSTest.buildPersonnageWork();
		personnageWork3.setGameMaster(gm);
		table.getPersonnages().add(personnageWork3);
		
		TableMessageBO message1 = buildTableMessage(personnageWork2.getPlayer(), "Message 1", "2015/01/20 22:41:52", table.getId());
		message1.setId(10);
		table.getMessages().add(message1);
		
		TableMessageBO message2 = buildTableMessage(gm, "Message 2", "2015/02/15 11:52:25", table.getId());
		message2.setId(20);
		table.getMessages().add(message2);
		
		return table;
	}
	
	
	
	
}
