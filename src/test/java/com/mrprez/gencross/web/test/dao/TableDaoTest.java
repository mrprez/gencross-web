package com.mrprez.gencross.web.test.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Test;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.PlannedGameBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.TableMessageBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.dao.AbstractDAO;
import com.mrprez.gencross.web.dao.TableDAO;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;

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
		PersonnageWorkBO personnageWork = (PersonnageWorkBO) getSession().get(PersonnageWorkBO.class, Integer.valueOf(1));
		
		// Execute
		TableBO table = tableDao.getPersonnageTable(personnageWork);
		
		// Check
		Assert.assertEquals(1, table.getId().intValue());
		checkTableDatabaseCompliance(table);
	}
	
	@Test
	public void testGetPersonnageTable_Fail() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = (PersonnageWorkBO) getSession().get(PersonnageWorkBO.class, Integer.valueOf(3));
		
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
		table.setGameMaster((UserBO) getSession().get(UserBO.class, "robin"));
		
		// Execute
		tableDao.saveTable(table);
		getTransaction().commit();
		
		// Check
		Assert.assertNotNull(table.getId());
		ITable databaseTable = getTable("RPG_TABLE");
		int row = findTableRow(databaseTable, "ID", table.getId());
		Assert.assertEquals(table.getName(), databaseTable.getValue(row, "NAME"));
		Assert.assertEquals(table.getType(), databaseTable.getValue(row, "TYPE"));
		Assert.assertEquals(table.getGameMaster().getUsername(), databaseTable.getValue(row, "GM_NAME"));
	}
	
	
	@Test
	public void testDeletePlannedGame() throws Exception{
		// Prepare
		PlannedGameBO plannedGame = (PlannedGameBO) getSession().get(PlannedGameBO.class, 1);
		
		// Execute
		tableDao.deletePlannedGame(plannedGame);
		getTransaction().commit();
		
		// Check
		Assert.assertNull(findTableRow(getTable("PLANNED_GAME"), "ID", 1));
	}
	
	
	@Test
	public void testDeleteTable() throws Exception{
		// Prepare
		TableBO table = (TableBO) getSession().get(TableBO.class, 1);
		
		// Execute
		tableDao.deleteTable(table);
		getTransaction().commit();
		
		// Check
		Assert.assertNull(findTableRow(getTable("RPG_TABLE"), "ID", 1));
		Assert.assertNull(findTableRow(getTable("PLANNED_GAME"), "RPG_TABLE", 1));
		Assert.assertNull(findTableRow(getTable("TABLE_PERSONNAGE"), "RPG_TABLE", 1));
		Assert.assertNull(findTableRow(getTable("TABLE_MESSAGE"), "RPG_TABLE", 1));
	}
	
	
	@Test
	public void testSavePlannedGame() throws Exception{
		// Prepare
		PlannedGameBO plannedGame = new PlannedGameBO();
		Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2015-10-08 19:00");
		plannedGame.setEndTime(endTime);
		Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2015-10-09 00:00");
		plannedGame.setStartTime(startTime);
		plannedGame.setTitle("Nouvelle partie");
		plannedGame.setTable((TableBO) getSession().get(TableBO.class, 1));
		
		// Execute
		tableDao.savePlannedGame(plannedGame);
		getTransaction().commit();
		
		// Check
		Assert.assertNotNull(plannedGame.getId());
		ITable table = getTable("PLANNED_GAME");
		int row = checkTableRow(table, "ID", plannedGame.getId());
		Assert.assertEquals(plannedGame.getTitle(), table.getValue(row, "TITLE"));
		Assert.assertEquals(plannedGame.getEndTime(), table.getValue(row, "GAME_END"));
		Assert.assertEquals(plannedGame.getStartTime(), table.getValue(row, "GAME_START"));
		Assert.assertEquals(plannedGame.getTable().getId(), table.getValue(row, "RPG_TABLE"));
	}
	
	@Test
	public void testDeleteMessage() throws Exception{
		// Prepare
		TableMessageBO message = (TableMessageBO) getSession().get(TableMessageBO.class, 1);
		
		// Execute
		tableDao.deleteMessage(message);
		getTransaction().commit();
		
		// Check
		Assert.assertNull(findTableRow(getTable("TABLE_MESSAGE"), "ID", 1));
	}
	
	@Test
	public void testLoadTablePlannedGames() throws Exception{
		// Execute
		List<PlannedGameBO> plannedGameList = tableDao.loadTablePlannedGames(1);
		
		// Check
		ITable table = getTable("PLANNED_GAME");
		Assert.assertEquals(count(table, "RPG_TABLE", 1), plannedGameList.size());
		for(PlannedGameBO plannedGame : plannedGameList){
			int row = checkTableRow(table, "ID", plannedGame.getId());
			Assert.assertEquals(table.getValue(row, "RPG_TABLE"), plannedGame.getTable().getId());
			Assert.assertEquals(table.getValue(row, "TITLE"), plannedGame.getTitle());
			Assert.assertEquals(table.getValue(row, "GAME_START"), plannedGame.getStartTime());
			Assert.assertEquals(table.getValue(row, "GAME_END"), plannedGame.getEndTime());
		}
	}
	
	@Test
	public void testLoadPlannedGame() throws Exception{
		// Execute
		PlannedGameBO plannedGame = tableDao.loadPlannedGame(3);
		
		// Check
		Assert.assertNotNull(plannedGame);
		Assert.assertEquals(3, plannedGame.getId().intValue());
		ITable table = getTable("PLANNED_GAME");
		int row = checkTableRow(table, "ID", plannedGame.getId());
		Assert.assertEquals(table.getValue(row, "RPG_TABLE"), plannedGame.getTable().getId());
		Assert.assertEquals(table.getValue(row, "TITLE"), plannedGame.getTitle());
		Assert.assertEquals(table.getValue(row, "GAME_START"), plannedGame.getStartTime());
		Assert.assertEquals(table.getValue(row, "GAME_END"), plannedGame.getEndTime());
	}
	
	@Test
	public void testFindPlannedGame_Success() throws Exception{
		// Check
		Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse("2015-10-25 19:00:00.000");
		
		// Execute
		PlannedGameBO plannedGame = tableDao.findPlannedGame(1, startDate);
		
		// Check
		Assert.assertNotNull(plannedGame);
		Assert.assertEquals(1, plannedGame.getTable().getId().intValue());
		Assert.assertEquals(startDate, plannedGame.getStartTime());
		ITable table = getTable("PLANNED_GAME");
		int row = checkTableRow(table, "ID", plannedGame.getId());
		Assert.assertEquals(table.getValue(row, "RPG_TABLE"), plannedGame.getTable().getId());
		Assert.assertEquals(table.getValue(row, "TITLE"), plannedGame.getTitle());
		Assert.assertEquals(table.getValue(row, "GAME_START"), plannedGame.getStartTime());
		Assert.assertEquals(table.getValue(row, "GAME_END"), plannedGame.getEndTime());
	}
	
	@Test
	public void testFindPlannedGame_Fail() throws Exception{
		// Check
		Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse("2015-10-25 19:01:00.000");
		
		// Execute
		PlannedGameBO plannedGame = tableDao.findPlannedGame(1, startDate);
		
		// Check
		Assert.assertNull(plannedGame);
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
