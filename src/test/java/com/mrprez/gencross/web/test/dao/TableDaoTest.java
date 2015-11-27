package com.mrprez.gencross.web.test.dao;

import java.sql.SQLException;
import java.util.Collection;

import org.dbunit.DatabaseUnitException;
import org.junit.Assert;
import org.junit.Test;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.TableMessageBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.dao.AbstractDAO;
import com.mrprez.gencross.web.dao.TableDAO;

public class TableDaoTest extends AbstractDaoTest {

	private TableDAO tableDao = new TableDAO();
	
	
	@Test
	public void testGetTableFromGM_Success() throws Exception{
		// Prepare
		UserBO user = new UserBO();
		user.setUsername("batman");
		
		// Execute
		Collection<TableBO> tableList = tableDao.getTableFromGM(user);
		
		// Check
		Assert.assertEquals(count(getTable("RPG_TABLE"), "GM_NAME", "batman"), tableList.size());
		for(TableBO table : tableList){
			checkTable(table);
		}
	}
	
	
	@Test
	public void testLoadTable_Success() throws Exception{
		// Prepare
		UserBO user = new UserBO();
		user.setUsername("batman");
		
		// Execute
		TableBO table = tableDao.loadTable(1);
		
		// Check
		Assert.assertNotNull(table);
		checkTable(table);
	}
	
	
	@Test
	public void testLoadTable_Fail_NotFound() throws Exception{
		// Prepare
		UserBO user = new UserBO();
		user.setUsername("batman");
		
		// Execute
		TableBO table = tableDao.loadTable(10);
		
		// Check
		Assert.assertNull(table);
	}
	
	
	@Test
	public void testSaveTable_Success() throws Exception{
		// Prepare
		TableBO table = buildTable();
		
		// Execute
		tableDao.saveTable(table);
		getTransaction().commit();
		
		// Check
		Assert.assertNotNull(table.getId());
		int row = checkTableRow(getTable("RPG_TABLE"), "ID", table.getId());
		Assert.assertEquals(getTable("RPG_TABLE").getValue(row, "NAME"), table.getName());
		Assert.assertEquals(getTable("RPG_TABLE").getValue(row, "TYPE"), table.getType());
		Assert.assertEquals(getTable("RPG_TABLE").getValue(row, "GM_NAME"), table.getGameMaster().getUsername());
	}
	
	
	@Test
	public void testGetPersonnageTable_Success() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setId(2);
		
		// Execute
		TableBO table = tableDao.getPersonnageTable(personnageWork);
		
		// Check
		Assert.assertNotNull(table);
		int row = findTableRow(getTable("TABLE_PERSONNAGE"), "PERSONNAGE", 2);
		Assert.assertEquals(getTable("TABLE_PERSONNAGE").getValue(row, "RPG_TABLE"), table.getId());
		checkTable(table);
	}
	
	
	private TableBO buildTable(){
		TableBO table = new TableBO();
		UserBO gameMaster = new UserBO();
		gameMaster.setUsername("batman");
		table.setGameMaster(gameMaster);
		table.setName("Nouvelle Gotham");
		table.setType("Pavillon Noir");
		
		return table;
	}
	
	
	private void checkTable(TableBO table) throws DatabaseUnitException, SQLException{
		int row = checkTableRow(getTable("RPG_TABLE"), "ID", table.getId());
		Assert.assertEquals(getTable("RPG_TABLE").getValue(row, "NAME"), table.getName());
		Assert.assertEquals(getTable("RPG_TABLE").getValue(row, "TYPE"), table.getType());
		Assert.assertEquals(getTable("RPG_TABLE").getValue(row, "GM_NAME"), table.getGameMaster().getUsername());
		Assert.assertEquals(count(getTable("TABLE_PERSONNAGE"), "RPG_TABLE", table.getId()), table.getPersonnages().size());
		for(PersonnageWorkBO personnageWork : table.getPersonnages()){
			int personnageWorkRow = checkTableRow(getTable("RPG_TABLE"), "ID", personnageWork.getId());
			Assert.assertEquals(getTable("PERSONNAGE_WORK").getValue(personnageWorkRow, "BACKGROUND"),  personnageWork.getBackground());
			Assert.assertSame(table.getGameMaster(),  personnageWork.getGameMaster());
			Assert.assertEquals(getTable("PERSONNAGE_WORK").getValue(personnageWorkRow, "LAST_UPDATE_DATE"),  personnageWork.getLastUpdateDate());
			Assert.assertEquals(getTable("PERSONNAGE_WORK").getValue(personnageWorkRow, "NAME"),  personnageWork.getName());
			Assert.assertEquals(getTable("PERSONNAGE_WORK").getValue(personnageWorkRow, "PERSONNAGE"),  personnageWork.getPersonnageData().getId());
			Assert.assertEquals(getTable("PERSONNAGE_WORK").getValue(personnageWorkRow, "USER_NAME"),  personnageWork.getPlayer().getUsername());
			Assert.assertEquals(getTable("PERSONNAGE_WORK").getValue(personnageWorkRow, "TYPE"),  personnageWork.getPluginName());
			Assert.assertEquals(table,  personnageWork.getTable());
			Assert.assertEquals(getTable("PERSONNAGE_WORK").getValue(personnageWorkRow, "VALIDATION_DATE"),  personnageWork.getValidationDate());
			Assert.assertEquals(getTable("PERSONNAGE_WORK").getValue(personnageWorkRow, "VALID_PERSONNAGE"),  personnageWork.getValidPersonnageData().getId());
		}
		Assert.assertEquals(count(getTable("TABLE_MESSAGE"), "RPG_TABLE", table.getId()), table.getMessages().size());
		for(TableMessageBO tableMessage : table.getMessages()){
			int messageRow = checkTableRow(getTable("TABLE_MESSAGE"), "ID", tableMessage.getId());
			if(tableMessage.getAuthor()==null){
				Assert.assertNull(getTable("TABLE_MESSAGE").getValue(messageRow, "AUTHOR"));
			}else{
				Assert.assertEquals(getTable("TABLE_MESSAGE").getValue(messageRow, "AUTHOR"), tableMessage.getAuthor().getUsername());
			}
			Assert.assertEquals(getTable("TABLE_MESSAGE").getValue(messageRow, "DATA"), tableMessage.getData());
		}
	}
	
	
	
	
	@Override
	public AbstractDAO getDao() {
		return tableDao;
	}

	@Override
	public String getDataSetFileName() {
		return "TableDaoDataset.xml";
	}

}
