package com.mrprez.gencross.web.test.dao;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Test;

import com.mrprez.gencross.web.bo.RoleBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.dao.AbstractDAO;
import com.mrprez.gencross.web.dao.UserDAO;

public class UserDaoTest extends AbstractDaoTest {

	private UserDAO userDao = new UserDAO();
	
	
	@Test
	public void testGetUser_Success(){
		// Execute
		UserBO user = userDao.getUser("batman", "243d39f8f28b13186597bef6b5d4797b");
		
		// Check
		Assert.assertEquals("batman", user.getUsername());
		Assert.assertEquals("243d39f8f28b13186597bef6b5d4797b", user.getDigest());
		Assert.assertEquals("batman@mail.com", user.getMail());
		Assert.assertEquals(2, user.getRoles().size());
		Assert.assertTrue(user.getRoles().contains(new RoleBO(RoleBO.MANAGER)));
		Assert.assertTrue(user.getRoles().contains(new RoleBO(RoleBO.USER)));
	}
	
	@Test
	public void testGetUser_Fail_BadPassword(){
		// Execute
		UserBO user = userDao.getUser("batman", "243d39f8f28b13186597bef6b5d47900");
		
		// Check
		Assert.assertNull(user);
	}
	
	@Test
	public void testGetUser_Success_NoPassword() throws Exception{
		// Execute
		UserBO user = userDao.getUser("batman");
		
		// Check
		checkUser(user, "batman");
		Assert.assertEquals("243d39f8f28b13186597bef6b5d4797b", user.getDigest());
	}
	
	@Test
	public void testGetUserFromMail_Success() throws Exception{
		// Execute
		UserBO user = userDao.getUserFromMail("batman@mail.com");
		
		// Check
		checkUser(user, "batman");
	}
	
	@Test
	public void testGetUserFromMail_Fail() throws Exception{
		// Execute
		UserBO user = userDao.getUserFromMail("noFound@mail.com");
		
		// Check
		Assert.assertNull(user);
	}
	
	@Test
	public void testGetUserList_Success() throws Exception{
		// Execute
		List<UserBO> userList = userDao.getUserList();
		
		// Check
		Assert.assertEquals(4, userList.size());
		checkUser(userList.get(0), "azerty");
		checkUser(userList.get(1), "batman");
		checkUser(userList.get(2), "ertyaz");
		checkUser(userList.get(3), "zertya");
	}
	
	@Test
	public void testSaveUser_Success() throws Exception{
		// Prepare
		UserBO user = new UserBO();
		user.setUsername("robin");
		user.setDigest("243d39f8f28b13186597bef6b5d4797b");
		user.setMail("robin@mail.com");
		Set<RoleBO> roleSet = new HashSet<RoleBO>();
		roleSet.add(new RoleBO(RoleBO.USER));
		user.setRoles(roleSet);
		
		// Execute
		UserBO returnedUser = userDao.saveUser(user);
		getTransaction().commit();
		
		// Check
		Assert.assertEquals(user, returnedUser);
		checkUser(user, "robin");
	}
	
	
	private void checkUser(UserBO user, String username) throws DatabaseUnitException, SQLException{
		Assert.assertNotNull(user);
		Assert.assertEquals(username, user.getUsername());
		
		ITable userTable = getTable("USERS");
		ITable userRoleTable = getTable("USER_ROLES");
		for(int userRow=0; userRow<userTable.getRowCount(); userRow++){
			if(userTable.getValue(userRow, "USER_NAME").equals(username)){
				Assert.assertEquals(userTable.getValue(userRow, "MAIL"), user.getMail());
				Set<RoleBO> userRoles = new HashSet<RoleBO>(user.getRoles());
				for(int userRoleRow=0; userRoleRow<userRoleTable.getRowCount(); userRoleRow++){
					if(userRoleTable.getValue(userRoleRow, "USER_NAME").equals(username)){
						if( ! userRoles.remove(new RoleBO((String)userRoleTable.getValue(userRoleRow, "ROLE_NAME"))) ){
							Assert.fail("Role "+userRoleTable.getValue(userRoleRow, "ROLE_NAME")+" found in database but not in user "+username);
						}
					}
				}
				Assert.assertTrue(userRoles.isEmpty());
			}
		}
	}
	
	@Test
	public void testDeleteUser_Success() throws Exception{
		// Prepare
		UserBO user = new UserBO();
		user.setUsername("batman");
		
		// Execute
		userDao.deleteUser(user);
		getTransaction().commit();
		
		// Check
		ITable userTable = getTable("USERS");
		for(int userRow=0; userRow<userTable.getRowCount(); userRow++){
			if(userTable.getValue(userRow, "USER_NAME").equals(user.getUsername())){
				Assert.fail("User not deleted in database");
			}
		}
	}
	
	
	
	@Override
	public AbstractDAO getDao() {
		return userDao;
	}

	@Override
	public String getDataSetFileName() {
		return "UserDaoDataset.xml";
	}

}
