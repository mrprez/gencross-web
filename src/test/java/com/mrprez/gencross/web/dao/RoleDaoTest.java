package com.mrprez.gencross.web.dao;

import org.junit.Assert;
import org.junit.Test;

import com.mrprez.gencross.web.bo.RoleBO;

public class RoleDaoTest extends AbstractDaoTest {

	private RoleDAO roleDao = new RoleDAO();
	
	
	
	@Test
	public void testGetRole_Success() throws Exception{
		// Execute
		RoleBO role = roleDao.getRole("manager");
		
		// Check
		Assert.assertNotNull(role);
		Assert.assertEquals("manager", role.getName());
	}
	
	
	@Test
	public void testGetRole_Fail() throws Exception{
		// Execute
		RoleBO role = roleDao.getRole("clown");
		
		// Check
		Assert.assertNull(role);
	}
	
	
	@Override
	public AbstractDAO getDao() {
		return roleDao;
	}

	@Override
	public String getDataSetFileName() {
		return "RoleDaoDataset.xml";
	}

}
