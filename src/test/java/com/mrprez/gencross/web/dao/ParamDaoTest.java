package com.mrprez.gencross.web.dao;

import java.io.IOException;
import java.sql.SQLException;

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
	
	@Test
	public void testGetParam() throws Exception{
		// Execute test
		ParamBO paramBO = paramDao.getParam("StringKey");
		
		// Check
		Assert.assertEquals("StringKey", paramBO.getKey());
		Assert.assertEquals("String Value", paramBO.getValue());
		Assert.assertEquals(ParamBO.STRING_TYPE, paramBO.getType());
	}


	@Override
	public String getDataSetFileName() {
		return "ParamDaoDataset.xml";
	}

}
