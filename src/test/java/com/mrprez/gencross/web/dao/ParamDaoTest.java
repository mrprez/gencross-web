package com.mrprez.gencross.web.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Test;

import com.mrprez.gencross.web.bo.ParamBO;

public class ParamDaoTest extends AbstractDaoTest {

	private ParamDAO paramDao = new ParamDAO();
	
	
	public ParamDaoTest() throws SQLException, IOException {
		super();
	}

	
	@Override
	public AbstractDAO getDao() {
		return paramDao;
	}
	
	@Override
	public String getDataSetFileName() {
		return "ParamDaoDataset.xml";
	}
	
	@Test
	public void testGetParam_Success_String() throws Exception{
		// Execute test
		ParamBO paramBO = paramDao.getParam("StringKey");
		
		// Check
		Assert.assertEquals("StringKey", paramBO.getKey());
		checkDatabaseExistence(paramBO, getSetupDataSet().getTable("PARAMS"));
	}
	
	@Test
	public void testGetParam_Fail_Null() throws Exception{
		// Execute test
		ParamBO paramBO = paramDao.getParam("NotFound");
		
		// Check
		Assert.assertNull(paramBO);
	}
	
	@Test
	public void testGetParam_Success_Date() throws Exception{
		// Execute test
		ParamBO paramBO = paramDao.getParam("DateKey");
		
		// Check
		Assert.assertEquals("DateKey", paramBO.getKey());
		checkDatabaseExistence(paramBO, getSetupDataSet().getTable("PARAMS"));
	}
	
	@Test
	public void testGetParam_Success_Int() throws Exception{
		// Execute test
		ParamBO paramBO = paramDao.getParam("IntKey");
		
		// Check
		Assert.assertEquals("IntKey", paramBO.getKey());
		checkDatabaseExistence(paramBO, getSetupDataSet().getTable("PARAMS"));
	}
	
	@Test
	public void testGetParam_Success_Real() throws Exception{
		// Execute test
		ParamBO paramBO = paramDao.getParam("RealKey");
		
		// Check
		Assert.assertEquals("RealKey", paramBO.getKey());
		checkDatabaseExistence(paramBO, getSetupDataSet().getTable("PARAMS"));
	}
	
	@Test
	public void testGetParam_Success_Boolean() throws Exception{
		// Execute test
		ParamBO paramBO = paramDao.getParam("BooleanKey");
		
		// Check
		Assert.assertEquals("BooleanKey", paramBO.getKey());
		checkDatabaseExistence(paramBO, getSetupDataSet().getTable("PARAMS"));
	}
	
	@Test
	public void testGetAllParams_Success() throws Exception{
		// Execute test
		Collection<ParamBO> paramList = paramDao.getAllParams();
		
		// Check
		ITable table = getSetupDataSet().getTable("PARAMS");
		Assert.assertEquals(table.getRowCount(), paramList.size());
		for(ParamBO param : paramList){
			checkDatabaseExistence(param, getSetupDataSet().getTable("PARAMS"));
		}
		
	}
	
	
	@Test
	public void testSave_Success() throws Exception{
		// Prepare
		ParamBO param = new ParamBO();
		param.setKey("NewKey");
		param.setType(ParamBO.STRING_TYPE);
		param.setValue("New Value");
		
		// Execute
		paramDao.save(param);
		paramDao.getTransaction().commit();
		
		// Check
		checkDatabaseExistence(param, getTable("PARAMS"));
	}
	
	
	private void checkDatabaseExistence(ParamBO param, ITable table) throws DataSetException, ParseException{
		for(int count=0; count<table.getRowCount(); count++){
			if(table.getValue(count, "PARAM_KEY").equals(param.getKey())){
				assertEquals(table, count, param);
				return;
			}
		}
		Assert.fail("Parameter "+param.getKey()+" not found in database");
	}
	
	private void assertEquals(ITable table, int row, ParamBO param) throws DataSetException, ParseException{
		Assert.assertEquals(1, ((String)table.getValue(row, "TYPE")).length());
		Assert.assertEquals(((String)table.getValue(row, "TYPE")).charAt(0), param.getType());
		switch (param.getType()) {
		case ParamBO.STRING_TYPE:
			Assert.assertEquals(table.getValue(row, "PARAM_VALUE"), param.getValue());
			break;
		case ParamBO.DATE_TYPE:
			Assert.assertEquals(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss,SSS").parse((String)table.getValue(row, "PARAM_VALUE")), param.getValue());
			break;
		case ParamBO.INTEGER_TYPE:
			Assert.assertEquals(Integer.valueOf((String)table.getValue(row, "PARAM_VALUE")), param.getValue());
			break;
		case ParamBO.REAL_TYPE:
			Assert.assertEquals(Double.valueOf((String)table.getValue(row, "PARAM_VALUE")), param.getValue());
			break;
		case ParamBO.BOOLEAN_TYPE:
			Assert.assertEquals(Boolean.valueOf((String)table.getValue(row, "PARAM_VALUE")), param.getValue());
			break;
		default:
			Assert.fail();
			break;
		}
		
	}


	

}
