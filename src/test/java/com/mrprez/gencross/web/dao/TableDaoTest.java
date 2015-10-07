package com.mrprez.gencross.web.dao;

import java.sql.SQLException;
import java.util.Collection;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Test;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.AuthentificationBSTest;

public class TableDaoTest extends AbstractDaoTest {

	private TableDAO tableDao = new TableDAO();
	
	
	
	@Test
	public void testGetTableFromGM() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		
		// Execute
		Collection<TableBO> tableList = tableDao.getTableFromGM(user);
		
		// Check
		Assert.assertEquals(count(getSetupDataSet().getTable("RPG_TABLE"), "GM_NAME", user.getUsername()), tableList.size());
		for(TableBO table : tableList){
			checkTableDatabaseCompliance(table);
		}
	}
	
	@Test
	public void testLoadTable() throws Exception{
		// Execute
		TableBO table = tableDao.loadTable(1);
		
		// Check
		Assert.assertNotNull(table);
		checkTableDatabaseCompliance(table);
	}
	
	@Test
	public void testGetPersonnageTable_Success() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = (PersonnageWorkBO) tableDao.getSession().get(PersonnageWorkBO.class, Integer.valueOf(1));
		
		// Execute
		TableBO table = tableDao.getPersonnageTable(personnageWork);
		
		// Check
		Assert.assertEquals(1, table.getId().intValue());
		checkTableDatabaseCompliance(table);
	}
	
	@Test
	public void testGetPersonnageTable_Fail() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = (PersonnageWorkBO) tableDao.getSession().get(PersonnageWorkBO.class, Integer.valueOf(3));
		
		// Execute
		TableBO table = tableDao.getPersonnageTable(personnageWork);
		
		// Check
		Assert.assertNull(table);
	}
	
	@Test
	public void testFindTableByName_Success() throws Exception{
		// Execute
		TableBO table = tableDao.findTableByName("Spin-Off");
		
		// Check
		Assert.assertNotNull(table);
		checkTableDatabaseCompliance(table);
	}
	
	@Test
	public void testFindTableByName_Fail() throws Exception{
		// Execute
		TableBO table = tableDao.findTableByName("Not exist");
		
		// Check
		Assert.assertNull(table);
	}
	
	@Test
	public void testSaveTable() throws Exception{
		// Prepare
		TableBO table = new TableBO();
		table.setName("New Gotham");
		table.setType("Pavillon Noir");
		table.setGameMaster((UserBO) tableDao.getSession().get(UserBO.class, "robin"));
		
		// Execute
		tableDao.saveTable(table);
		tableDao.getTransaction().commit();
		
		// Check
		Assert.assertNotNull(table.getId());
		ITable databaseTable = getTable("RPG_TABLE");
		int row = findTableRow(databaseTable, "ID", table.getId());
		Assert.assertEquals(table.getName(), databaseTable.getValue(row, "NAME"));
		Assert.assertEquals(table.getType(), databaseTable.getValue(row, "TYPE"));
		Assert.assertEquals(table.getGameMaster().getUsername(), databaseTable.getValue(row, "GM_NAME"));
	}
	
	@Override
	public AbstractDAO getDao() {
		return tableDao;
	}

	@Override
	public String getDataSetFileName() {
		return "TableDaoDataset.xml";
	}
	
	
	private void checkTableDatabaseCompliance(TableBO rpgTable) throws DatabaseUnitException, SQLException{
		ITable table = getTable("RPG_TABLE");
		int row=checkTableRow(table, "ID", rpgTable.getId());
		Assert.assertEquals(table.getValue(row, "NAME"), rpgTable.getName());
		Assert.assertEquals(table.getValue(row, "TYPE"), rpgTable.getType());
		Assert.assertEquals(table.getValue(row, "GM_NAME"), rpgTable.getGameMaster().getUsername());
		if(rpgTable.getPersonnages()!=null){
			for(PersonnageWorkBO personnageWork : rpgTable.getPersonnages()){
				checkPersonnageWorkDatabaseCompliance(personnageWork);
			}
		}
			
	}
	
	
	private void checkPersonnageWorkDatabaseCompliance(PersonnageWorkBO personnageWork) throws DatabaseUnitException, SQLException{
		ITable table = getTable("PERSONNAGE_WORK");
		int row = checkTableRow(table, "ID", personnageWork.getId());
		Assert.assertEquals(table.getValue(row, "NAME"), personnageWork.getName());
		
		if(table.getValue(row, "USER_NAME")!=null){
			Assert.assertNotNull(personnageWork.getPlayer());
			Assert.assertEquals(table.getValue(row, "USER_NAME"), personnageWork.getPlayer().getUsername());
		}else{
			Assert.assertNull(personnageWork.getPlayer());
		}
		
		Assert.assertNotNull(personnageWork.getGameMaster());
		Assert.assertEquals(table.getValue(row, "GM_NAME"), personnageWork.getGameMaster().getUsername());
		
		Assert.assertEquals(table.getValue(row, "TYPE"), personnageWork.getPluginName());
		Assert.assertEquals(table.getValue(row, "VALIDATION_DATE"), personnageWork.getValidationDate());
		Assert.assertEquals(table.getValue(row, "LAST_UPDATE_DATE"), personnageWork.getLastUpdateDate());
		Assert.assertEquals(table.getValue(row, "BACKGROUND"), personnageWork.getBackground());
	}

}
