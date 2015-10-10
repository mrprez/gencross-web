package com.mrprez.gencross.web.test.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.ITable;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Assert;
import org.junit.Test;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.disk.PersonnageFactory;
import com.mrprez.gencross.value.StringValue;
import com.mrprez.gencross.web.bo.PersonnageDataBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.PersonnageXmlBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.dao.AbstractDAO;
import com.mrprez.gencross.web.dao.PersonnageDAO;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.mrprez.gencross.web.test.bs.TableBSTest;

public class PersonnageDaoTest extends AbstractDaoTest {

	private PersonnageDAO personnageDao = new PersonnageDAO();
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
	
	
	@Test
	public void testSavePersonnage() throws Exception{
		// Prepare
		Date minDate = new Date();
		PersonnageDataBO personnageData = new PersonnageDataBO();
		personnageData.setPersonnage(new PersonnageFactory().buildNewPersonnage("Pavillon Noir"));
		
		// Execute
		personnageDao.savePersonnage(personnageData);
		getTransaction().commit();
		
		// Check
		Assert.assertNotNull(personnageData.getLastUpdateDate());
		Date maxDate = new Date();
		Date lastUpDate = personnageData.getLastUpdateDate();
		Assert.assertTrue(dateFormat.format(lastUpDate)+" before "+dateFormat.format(minDate), minDate.getTime() <= lastUpDate.getTime() );
		Assert.assertTrue(dateFormat.format(maxDate)+" before "+dateFormat.format(lastUpDate), lastUpDate.getTime() <= maxDate.getTime() );
		Assert.assertNotNull(personnageData.getId());
		
		ITable table = getTable("PERSONNAGE");
		int row = checkTableRow(table, "ID", personnageData.getId());
		String tableString = new String((byte[])table.getValue(row, "DATA")).replace("\r\n", "\n");
		Assert.assertEquals(getPersonnageXmlString(personnageData), tableString);
	}
	
	
	@Test
	public void testSavePersonnageWork() throws IOException, Exception{
		// Prepare
		Date minDate = new Date();
		PersonnageWorkBO personnageWork = buildPersonnageWork();
		personnageDao.savePersonnage(personnageWork.getPersonnageData());
		personnageDao.savePersonnage(personnageWork.getValidPersonnageData());
		personnageWork.setPlayer(AuthentificationBSTest.buildUser("robin"));
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		personnageWork.setGameMaster(gm);
		personnageWork.setTable(TableBSTest.buildTable(gm, "Gotham", "Pavillon Noir"));
		
		// Execute
		personnageDao.savePersonnageWork(personnageWork);
		getTransaction().commit();
		
		// Check
		Date maxDate = new Date();
		Assert.assertNotNull(personnageWork.getId());
		ITable personnageWorkTable = getTable("PERSONNAGE_WORK");
		int personnageWorkRow = checkTableRow(personnageWorkTable, "ID", personnageWork.getId());
		Assert.assertEquals(personnageWork.getName(), personnageWorkTable.getValue(personnageWorkRow, "NAME"));
		Assert.assertEquals(personnageWork.getPlayer().getUsername(), personnageWorkTable.getValue(personnageWorkRow, "USER_NAME"));
		Assert.assertEquals(personnageWork.getGameMaster().getUsername(), personnageWorkTable.getValue(personnageWorkRow, "GM_NAME"));
		Assert.assertEquals(personnageWork.getPluginName(), personnageWorkTable.getValue(personnageWorkRow, "TYPE"));
		Assert.assertEquals(personnageWork.getValidationDate(), personnageWorkTable.getValue(personnageWorkRow, "VALIDATION_DATE"));
		Assert.assertEquals(personnageWork.getLastUpdateDate(), personnageWorkTable.getValue(personnageWorkRow, "LAST_UPDATE_DATE"));
		Assert.assertTrue(minDate.getTime() <= personnageWork.getLastUpdateDate().getTime());
		Assert.assertTrue(personnageWork.getLastUpdateDate().getTime() <= maxDate.getTime());
	}
	
	@Test
	public void testLoadPersonnageWork_Success() throws Exception{
		// Execute
		PersonnageWorkBO personnageWork = personnageDao.loadPersonnageWork(1);
		
		// Check
		checkPersonnageWorkDatabaseCompliance(personnageWork);
		
		ITable personnageTable = getTable("PERSONNAGE");
		
		int personnageRow = checkTableRow(personnageTable, "ID", personnageWork.getPersonnageData().getId());
		String tableString = new String((byte[])personnageTable.getValue(personnageRow, "DATA")).replace("\r\n", "\n");
		Assert.assertEquals(tableString, getPersonnageXmlString(personnageWork.getPersonnageData()));
		
		int validRow = checkTableRow(personnageTable, "ID", personnageWork.getPersonnageData().getId());
		String validString = new String((byte[])personnageTable.getValue(validRow, "DATA")).replace("\r\n", "\n");
		Assert.assertEquals(validString, getPersonnageXmlString(personnageWork.getValidPersonnageData()));
	}
	
	@Test
	public void testLoadPersonnageWork_Fail() throws Exception{
		// Execute
		PersonnageWorkBO personnageWork = personnageDao.loadPersonnageWork(10);
		
		// Check
		Assert.assertNull(personnageWork);
		
	}
	
	
	@Test
	public void testLoadValidPersonnage() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = buildPersonnageWork();
		getSession().beginTransaction();
		getSession().save(personnageWork.getPersonnageData());
		getSession().save(personnageWork.getValidPersonnageData());
		getSession().save(personnageWork);
		getTransaction().commit();
		
		// Execute
		PersonnageDataBO validPersonnageData = personnageDao.loadValidPersonnage(personnageWork);
		
		// Check
		ITable personnageTable = getTable("PERSONNAGE");
		
		int validRow = checkTableRow(personnageTable, "ID", validPersonnageData.getId());
		String validString = new String((byte[])personnageTable.getValue(validRow, "DATA")).replace("\r\n", "\n");
		Assert.assertEquals(validString, getPersonnageXmlString(validPersonnageData));
	}
	
	@Test
	public void testGetPlayerPersonnageList() throws Exception{
		// Prepare
		UserBO player = AuthentificationBSTest.buildUser("robin");
		
		// Execute
		List<PersonnageWorkBO> personageWorkList = personnageDao.getPlayerPersonnageList(player);
		
		// Check
		Assert.assertEquals(count(getTable("PERSONNAGE_WORK"), "USER_NAME", "robin"), personageWorkList.size());
		for(PersonnageWorkBO personnageWork : personageWorkList){
			checkPersonnageWorkDatabaseCompliance(personnageWork);
			Integer tablePersonnageRow = findTableRow(getTable("TABLE_PERSONNAGE"), "PERSONNAGE", personnageWork.getId());
			if(tablePersonnageRow!=null){
				Assert.assertNotNull(personnageWork.getTable());
				checkTable(personnageWork.getTable());
			}else{
				Assert.assertNull(personnageWork.getTable());
			}
		}
	}
	
	
	@Test
	public void testGetGameMasterPersonnageList() throws Exception{
		// Prepare
		UserBO gameMaster = AuthentificationBSTest.buildUser("batman");
		
		// Execute
		List<PersonnageWorkBO> personageWorkList = personnageDao.getGameMasterPersonnageList(gameMaster);
		
		// Check
		Assert.assertEquals(count(getTable("PERSONNAGE_WORK"), "GM_NAME", "batman"), personageWorkList.size());
		for(PersonnageWorkBO personnageWork : personageWorkList){
			checkPersonnageWorkDatabaseCompliance(personnageWork);
			Integer tablePersonnageRow = findTableRow(getTable("TABLE_PERSONNAGE"), "PERSONNAGE", personnageWork.getId());
			if(tablePersonnageRow!=null){
				Assert.assertNotNull(personnageWork.getTable());
				checkTable(personnageWork.getTable());
			}else{
				Assert.assertNull(personnageWork.getTable());
			}
		}
	}
	
	
	@Test
	public void testDeletePersonnage() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = (PersonnageWorkBO) getSession().get(PersonnageWorkBO.class, 1);
		
		// Execute
		personnageDao.deletePersonnage(personnageWork);
		getTransaction().commit();
		
		// Check
		Assert.assertNull( findTableRow(getTable("PERSONNAGE_WORK"), "ID", personnageWork.getId()) );;
		
	}
	
	
	@Test
	public void testGetAllPersonnages() throws Exception{
		// Execute
		List<PersonnageWorkBO> personnageList = personnageDao.getAllPersonnages();
		
		// Check
		Assert.assertEquals(getTable("PERSONNAGE_WORK").getRowCount(), personnageList.size());
		for(PersonnageWorkBO personnageWork : personnageList){
			checkPersonnageWorkDatabaseCompliance(personnageWork);
		}
	}
	
	
	@Test
	public void testGetXmlByType() throws Exception{
		// Execute
		Collection<PersonnageXmlBO> personnageXmlList = personnageDao.getXmlByType("Pavillon Noir");
		
		// Check
		Assert.assertEquals(getTable("PERSONNAGE").getRowCount(), personnageXmlList.size());
		for(PersonnageXmlBO personnageXml : personnageXmlList){
			int row = checkTableRow(getTable("PERSONNAGE"), "ID", personnageXml.getId());
			Assert.assertEquals(getTable("PERSONNAGE").getValue(row, "LAST_UPDATE_DATE"), personnageXml.getLastUpdateDate());
			Assert.assertArrayEquals((byte[])getTable("PERSONNAGE").getValue(row, "DATA"), personnageXml.getXml());
		}
	}
	
	
	@Test
	public void testSavePersonnageXml() throws Exception{
		// Prepare
		Date minDate = new Date();
		PersonnageXmlBO personnageXml = (PersonnageXmlBO) getSession().get(PersonnageXmlBO.class, 5);
		personnageXml.setXml("This is not XML, but here, it does not matter".getBytes());
		
		// Execute
		personnageDao.savePersonnageXml(personnageXml);
		getTransaction().commit();
		
		// Check
		Date maxDate = new Date();
		int row = checkTableRow(getTable("PERSONNAGE"), "ID", 5);
		Date lastUpdateDate = (Date) getTable("PERSONNAGE").getValue(row, "LAST_UPDATE_DATE");
		Assert.assertTrue(minDate.getTime() <= lastUpdateDate.getTime());
		Assert.assertTrue(lastUpdateDate.getTime() <= maxDate.getTime());
		Assert.assertArrayEquals((byte[])getTable("PERSONNAGE").getValue(row, "DATA"), personnageXml.getXml());
	}
	
	
	@Test
	public void testGetAddablePersonnages() throws Exception{
		// Prepare
		TableBO table = (TableBO) getSession().get(TableBO.class, 1);
		
		// Execute
		Collection<PersonnageWorkBO> personnageWorkList = personnageDao.getAddablePersonnages(table);
		
		// Check
		Set<Integer> expectedPersonnageWorkIdList = new HashSet<Integer>(Arrays.asList(3, 4));
		Assert.assertEquals(expectedPersonnageWorkIdList.size(), personnageWorkList.size());
		for(PersonnageWorkBO personnageWork : personnageWorkList){
			Assert.assertTrue(expectedPersonnageWorkIdList.contains(personnageWork.getId()));
			expectedPersonnageWorkIdList.remove(personnageWork.getId());
			checkPersonnageWorkDatabaseCompliance(personnageWork);
		}
		Assert.assertTrue(expectedPersonnageWorkIdList.isEmpty());
	}
	
	
	@Test
	public void testGetPersonnageListFromType() throws Exception{
		// Execute
		Collection<PersonnageWorkBO> personnageWorkList = personnageDao.getPersonnageListFromType("Pavillon Noir");
		
		// Check
		Assert.assertEquals(getTable("PERSONNAGE_WORK").getRowCount(), personnageWorkList.size());
		for(PersonnageWorkBO personnageWork : personnageWorkList){
			checkPersonnageWorkDatabaseCompliance(personnageWork);
		}
	}
	
	
	@Test
	public void testGetPersonnageListFromType_EmptyList() throws Exception{
		// Execute
		Collection<PersonnageWorkBO> personnageWorkList = personnageDao.getPersonnageListFromType("No existing type");
		
		// Check
		Assert.assertTrue(personnageWorkList.isEmpty());
	}
	
	
	@Test
	public void testGetLastModified() throws Exception{
		// Prepare
		Date date = dateFormat.parse("2015-03-25 00:00:00,000");
		
		// Execute
		Collection<PersonnageWorkBO> personnageWorkList = personnageDao.getLastModified(date);
		
		// Check
		Set<Integer> expectedPersonnageWorkIdList = new HashSet<Integer>(Arrays.asList(3, 4));
		Assert.assertEquals(expectedPersonnageWorkIdList.size(), personnageWorkList.size());
		for(PersonnageWorkBO personnageWork : personnageWorkList){
			Assert.assertTrue(expectedPersonnageWorkIdList.contains(personnageWork.getId()));
			expectedPersonnageWorkIdList.remove(personnageWork.getId());
			checkPersonnageWorkDatabaseCompliance(personnageWork);
		}
		Assert.assertTrue(expectedPersonnageWorkIdList.isEmpty());
	}
	
	
	public static PersonnageWorkBO buildPersonnageWork() throws Exception{
		PersonnageFactory personnageFactory = new PersonnageFactory();
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPluginName("Pavillon Noir");
		personnageWork.setName("Joker");
		
		PersonnageDataBO validPersonnageData = new PersonnageDataBO();
		Personnage validPersonnage = personnageFactory.buildNewPersonnage("Pavillon Noir");
		validPersonnageData.setPersonnage(validPersonnage);
		personnageWork.setValidPersonnageData(validPersonnageData);
		
		personnageWork.setValidationDate(dateFormat.parse("2015-10-01 16:09:30,158"));
		
		PersonnageDataBO personnageData = new PersonnageDataBO();
		Personnage personnage = personnageFactory.buildNewPersonnage("Pavillon Noir");
		personnage.setNewValue("Nom", new StringValue("Bruce Wayne"));
		personnageData.setPersonnage(personnage);
		personnageWork.setPersonnageData(personnageData);
		
		personnageWork.setBackground("Personnage background");
		return personnageWork;
	}
	
	
	
	
	private String getPersonnageXmlString(PersonnageDataBO personnageData) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLWriter writer = new XMLWriter(baos, new OutputFormat("\t", true, "UTF-8"));
		writer.write(personnageData.getPersonnage().getXML());
		return new String(baos.toByteArray()).replace("\r\n", "\n");
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
		
		if(table.getValue(row, "GM_NAME")!=null){
			Assert.assertNotNull(personnageWork.getGameMaster());
			Assert.assertEquals(table.getValue(row, "GM_NAME"), personnageWork.getGameMaster().getUsername());
		}else{
			Assert.assertNull(personnageWork.getGameMaster());
		}
		
		Assert.assertEquals(table.getValue(row, "TYPE"), personnageWork.getPluginName());
		Assert.assertEquals(table.getValue(row, "VALIDATION_DATE"), personnageWork.getValidationDate());
		Assert.assertEquals(table.getValue(row, "LAST_UPDATE_DATE"), personnageWork.getLastUpdateDate());
		Assert.assertEquals(table.getValue(row, "BACKGROUND"), personnageWork.getBackground());
	}
	
	
	private void checkTable(TableBO table) throws DatabaseUnitException, SQLException{
		int row = checkTableRow(getTable("RPG_TABLE"), "ID", table.getId());
		Assert.assertEquals(getTable("RPG_TABLE").getValue(row, "NAME"), table.getName());
		Assert.assertEquals(getTable("RPG_TABLE").getValue(row, "TYPE"), table.getType());
		Assert.assertEquals(getTable("RPG_TABLE").getValue(row, "GM_NAME"), table.getGameMaster().getUsername());
	}
	
	
	
	@Override
	public AbstractDAO getDao() {
		return personnageDao;
	}

	@Override
	public String getDataSetFileName() {
		return "PersonnageDaoDataset.xml";
	}

}
