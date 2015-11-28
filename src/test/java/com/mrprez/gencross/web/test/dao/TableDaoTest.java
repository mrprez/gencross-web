package com.mrprez.gencross.web.test.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.dbunit.DatabaseUnitException;
import org.junit.Assert;
import org.junit.Test;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.PlannedGameBO;
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
		PersonnageWorkBO personnageWork = (PersonnageWorkBO) getSession().load(PersonnageWorkBO.class, 2);
		
		// Execute
		TableBO table = tableDao.getPersonnageTable(personnageWork);
		
		// Check
		Assert.assertNotNull(table);
		int row = findTableRow(getTable("TABLE_PERSONNAGE"), "PERSONNAGE", 2);
		Assert.assertEquals(getTable("TABLE_PERSONNAGE").getValue(row, "RPG_TABLE"), table.getId());
		checkTable(table);
	}
	
	
	@Test
	public void testGetPersonnageTable_Fail_NotFound() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = (PersonnageWorkBO) getSession().load(PersonnageWorkBO.class, 3);
		
		// Execute
		TableBO table = tableDao.getPersonnageTable(personnageWork);
		
		// Check
		Assert.assertNull(table);
	}
	
	
	@Test
	public void testFindTableByName_Success() throws Exception{
		// Execute
		TableBO table = tableDao.findTableByName("Gotham");
		
		// Check
		Assert.assertNotNull(table);
		Assert.assertEquals("Gotham", table.getName());
		checkTable(table);
	}
	
	
	@Test
	public void testFindTableByName_Fail_NotFound() throws Exception{
		// Execute
		TableBO table = tableDao.findTableByName("NotFound");
		
		// Check
		Assert.assertNull(table);
	}
	
	
	@Test
	public void testDeleteMessage_Success() throws Exception{
		// Prepare
		TableMessageBO message = (TableMessageBO) getSession().load(TableMessageBO.class, 2);
		
		// Execute
		tableDao.deleteMessage(message);
		getTransaction().commit();
		
		// Check
		Assert.assertNull(findTableRow(getTable("TABLE_MESSAGE"), "ID", 2));
	}
	
	
	@Test
	public void testSavePlannedGame_Success() throws Exception{
		// Prepare
		PlannedGameBO plannedGame = new PlannedGameBO();
		plannedGame.setTable((TableBO) getSession().load(TableBO.class, 1));
		plannedGame.setTitle("New planned game");
		plannedGame.setStartTime(new SimpleDateFormat("hh:mm dd/MM/yyyy").parse("19:00 16/01/2015"));
		plannedGame.setEndTime(new SimpleDateFormat("hh:mm dd/MM/yyyy").parse("00:00 17/01/2015"));
		
		// Execute
		tableDao.savePlannedGame(plannedGame);
		getTransaction().commit();
		
		// Check
		Assert.assertNotNull(plannedGame.getId());
		int row = checkTableRow(getTable("PLANNED_GAME"), "ID", plannedGame.getId());
		Assert.assertEquals(getTable("PLANNED_GAME").getValue(row, "RPG_TABLE"), plannedGame.getTable().getId());
		Assert.assertEquals(((Date)getTable("PLANNED_GAME").getValue(row, "GAME_START")).getTime(), plannedGame.getStartTime().getTime());
		Assert.assertEquals(((Date)getTable("PLANNED_GAME").getValue(row, "GAME_END")).getTime(), plannedGame.getEndTime().getTime());
		Assert.assertEquals(getTable("PLANNED_GAME").getValue(row, "TITLE"), plannedGame.getTitle());
	}
	
	
	@Test
	public void testFindPlannedGame_Success() throws ParseException, Exception {
		// Execute
		PlannedGameBO plannedGame = tableDao.findPlannedGame(1, new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2015-08-10 12:00"));
		
		//Check
		Assert.assertNotNull(plannedGame);
		Assert.assertEquals(getTable("PLANNED_GAME").getValue(0, "ID"), plannedGame.getId());
		Assert.assertEquals(getTable("PLANNED_GAME").getValue(0, "RPG_TABLE"), plannedGame.getTable().getId());
		Assert.assertEquals(((Date)getTable("PLANNED_GAME").getValue(0, "GAME_START")).getTime(), plannedGame.getStartTime().getTime());
		Assert.assertEquals(((Date)getTable("PLANNED_GAME").getValue(0, "GAME_END")).getTime(), plannedGame.getEndTime().getTime());
		Assert.assertEquals(getTable("PLANNED_GAME").getValue(0, "TITLE"), plannedGame.getTitle());
	}
	
	
	@Test
	public void testFindPlannedGame_Fail_NotFound() throws ParseException, Exception {
		// Execute
		PlannedGameBO plannedGame = tableDao.findPlannedGame(1, new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2015-08-10 12:01"));
		
		//Check
		Assert.assertNull(plannedGame);
	}
	
	
	@Test
	public void testLoadPlannedGame_Success() throws Exception{
		// Execute
		PlannedGameBO plannedGame = tableDao.loadPlannedGame(1);
		
		// Check
		Assert.assertNotNull(plannedGame);
		Assert.assertNotNull(plannedGame.getId());
		Assert.assertEquals(1, plannedGame.getId().intValue());
		int row = checkTableRow(getTable("PLANNED_GAME"), "ID", plannedGame.getId());
		Assert.assertEquals(getTable("PLANNED_GAME").getValue(row, "RPG_TABLE"), plannedGame.getTable().getId());
		Assert.assertEquals(((Date)getTable("PLANNED_GAME").getValue(row, "GAME_START")).getTime(), plannedGame.getStartTime().getTime());
		Assert.assertEquals(((Date)getTable("PLANNED_GAME").getValue(row, "GAME_END")).getTime(), plannedGame.getEndTime().getTime());
		Assert.assertEquals(getTable("PLANNED_GAME").getValue(row, "TITLE"), plannedGame.getTitle());
	}
	
	
	@Test
	public void testLoadPlannedGame_Fail_NotFound() throws Exception{
		// Execute
		PlannedGameBO plannedGame = tableDao.loadPlannedGame(10);
		
		// Check
		Assert.assertNull(plannedGame);
	}
	
	
	@Test
	public void testLoadTablePlannedGames_Success() throws Exception{
		// Execute
		List<PlannedGameBO> plannedGameList = tableDao.loadTablePlannedGames(1);
		
		// Check
		Assert.assertEquals(2, plannedGameList.size());
		for(PlannedGameBO plannedGame : plannedGameList){
			int row = checkTableRow(getTable("PLANNED_GAME"), "ID", plannedGame.getId());
			Assert.assertEquals(getTable("PLANNED_GAME").getValue(row, "RPG_TABLE"), plannedGame.getTable().getId());
			Assert.assertEquals(((Date)getTable("PLANNED_GAME").getValue(row, "GAME_START")).getTime(), plannedGame.getStartTime().getTime());
			Assert.assertEquals(((Date)getTable("PLANNED_GAME").getValue(row, "GAME_END")).getTime(), plannedGame.getEndTime().getTime());
			Assert.assertEquals(getTable("PLANNED_GAME").getValue(row, "TITLE"), plannedGame.getTitle());
		}
	}
	
	
	@Test
	public void testDeletePlannedGame() throws Exception{
		// Prepare
		PlannedGameBO plannedGame = (PlannedGameBO) getSession().load(PlannedGameBO.class, 2);
		
		// Execute
		tableDao.deletePlannedGame(plannedGame);
		getTransaction().commit();
		
		// Check
		Assert.assertNull(findTableRow(getTable("PLANNED_GAME"), "ID", plannedGame.getId()));
	}
	
	
	@Test
	public void testDeleteTable() throws Exception{
		// Prepare
		TableBO table = (TableBO) getSession().load(TableBO.class, 1);
		
		// Execute
		tableDao.deleteTable(table);
		getTransaction().commit();
		
		// Check
		Assert.assertEquals(0, count(getTable("PLANNED_GAME"), "RPG_TABLE", 1));
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
