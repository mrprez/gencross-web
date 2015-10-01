package com.mrprez.gencross.web.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.AuthentificationBSTest;
import com.mrprez.gencross.web.bs.TableBSTest;

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
		personnageDao.getTransaction().commit();
		
		// Check
		Assert.assertNotNull(personnageData.getLastUpdateDate());
		Date maxDate = new Date();
		Date lastUpDate = personnageData.getLastUpdateDate();
		Assert.assertTrue(dateFormat.format(lastUpDate)+" before "+dateFormat.format(minDate), minDate.getTime() <= lastUpDate.getTime() );
		Assert.assertTrue(dateFormat.format(maxDate)+" before "+dateFormat.format(lastUpDate), lastUpDate.getTime() <= maxDate.getTime() );
		Assert.assertNotNull(personnageData.getId());
		
		checkPersonnageDataExistence(personnageData);
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
		personnageDao.getTransaction().commit();
		
		// Check
		Date maxDate = new Date();
		Assert.assertNotNull(personnageWork.getId());
		ITable personnageWorkTable = getTable("PERSONNAGE_WORK");
		int personnageWorkRow = findTableRow(personnageWorkTable, "ID", personnageWork.getId());
		Assert.assertEquals(personnageWork.getName(), personnageWorkTable.getValue(personnageWorkRow, "NAME"));
		Assert.assertEquals(personnageWork.getPlayer().getUsername(), personnageWorkTable.getValue(personnageWorkRow, "USER_NAME"));
		Assert.assertEquals(personnageWork.getGameMaster().getUsername(), personnageWorkTable.getValue(personnageWorkRow, "GM_NAME"));
		Assert.assertEquals(personnageWork.getPluginName(), personnageWorkTable.getValue(personnageWorkRow, "TYPE"));
		Assert.assertEquals(personnageWork.getValidationDate(), personnageWorkTable.getValue(personnageWorkRow, "VALIDATION_DATE"));
		Assert.assertEquals(personnageWork.getLastUpdateDate(), personnageWorkTable.getValue(personnageWorkRow, "LAST_UPDATE_DATE"));
		Assert.assertTrue(minDate.getTime() <= personnageWork.getLastUpdateDate().getTime());
		Assert.assertTrue(personnageWork.getLastUpdateDate().getTime() <= maxDate.getTime());
		
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
	
	
	private void checkPersonnageDataExistence(PersonnageDataBO personnageData) throws DatabaseUnitException, SQLException, IOException{
		ITable table = getTable("PERSONNAGE");
		
		int row = findTableRow(table, "ID", personnageData.getId());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLWriter writer = new XMLWriter(baos, new OutputFormat("\t", true, "UTF-8"));
		writer.write(personnageData.getPersonnage().getXML());
		Assert.assertArrayEquals((byte[])baos.toByteArray(), (byte[])table.getValue(row, "DATA"));
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
