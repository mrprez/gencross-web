package com.mrprez.gencross.web.test.bs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.RoleBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.AuthentificationBS;
import com.mrprez.gencross.web.dao.face.IMailResource;
import com.mrprez.gencross.web.dao.face.IPersonnageDAO;
import com.mrprez.gencross.web.dao.face.IRoleDAO;
import com.mrprez.gencross.web.dao.face.IUserDAO;

@RunWith(MockitoJUnitRunner.class)
public class AuthentificationBSTest {
	
	@Mock
	private IUserDAO userDao;
	@Mock
	private IPersonnageDAO personnageDao;
	@Mock
	private IRoleDAO roleDao;
	@Mock
	private IMailResource mailResource;
	
	@InjectMocks
	private AuthentificationBS authentificationBS;
	
	
	@Test
	public void testAuthentificateUser_Success() throws Exception{
		// Prepare
		String username = "batman";
		String password = "robin";
		String mail = "batman@gmail.com";
		String digest = "8ee60a2e00c90d7e00d5069188dc115b";
		Set<RoleBO> roles = new HashSet<RoleBO>();
		roles.add(new RoleBO(RoleBO.USER));
		UserBO returnedUser = new UserBO();
		returnedUser.setUsername(username);
		returnedUser.setMail(mail);
		returnedUser.setDigest(digest);
		returnedUser.setRoles(roles);
		
		Mockito.when(userDao.getUser(username, digest)).thenReturn(returnedUser);
		
		// Execute
		UserBO user = authentificationBS.authentificateUser(username, password);
		
		// Check
		Assert.assertNotNull(user);
		Assert.assertEquals(username, user.getUsername());
		Assert.assertEquals(mail, user.getMail());
		Assert.assertEquals(roles, user.getRoles());
	}
	
	
	@Test
	public void testAuthentificateUser_Fail_BadPassword() throws Exception{
		// Prepare
		String username = "batman";
		String mail = "batman@gmail.com";
		String digest = "8ee60a2e00c90d7e00d5069188dc115b";
		Set<RoleBO> roles = new HashSet<RoleBO>();
		roles.add(new RoleBO(RoleBO.USER));
		UserBO returnedUser = new UserBO();
		returnedUser.setUsername(username);
		returnedUser.setMail(mail);
		returnedUser.setDigest(digest);
		returnedUser.setRoles(roles);
		
		Mockito.when(userDao.getUser(username, digest)).thenReturn(returnedUser);
		
		// Execute
		UserBO user = authentificationBS.authentificateUser(username, "badPassword");
		
		// Check
		Assert.assertNull(user);
	}
	
	
	@Test
	public void testCreateUser_Success() throws Exception{
		// Prepare
		Mockito.when(roleDao.getRole(RoleBO.USER)).thenReturn(new RoleBO(RoleBO.USER));

		String username = "batman";
		String password = "robin";
		String mail = "batman@gmail.com";
		String digest = "8ee60a2e00c90d7e00d5069188dc115b";
		
		// Excecute
		UserBO user = authentificationBS.createUser(username, password, mail);
		
		// Check
		Assert.assertNotNull(user);
		Assert.assertEquals(username, user.getUsername());
		Assert.assertEquals(mail, user.getMail());
		Assert.assertEquals(digest, user.getDigest());
		Assert.assertEquals(1, user.getRoles().size());
		Assert.assertTrue(user.getRoles().contains(new RoleBO(RoleBO.USER)));
		
		Mockito.verify(userDao).saveUser(user);
	}
	
	
	@Test
	public void testRemoveUser_Success() throws Exception{
		// Prepare
		String userName = "batman";
		UserBO user = buildUser(userName);
		Mockito.when(userDao.getUser(userName)).thenReturn(user);
		
		PersonnageWorkBO gameMasterPersonnage1 = new PersonnageWorkBO();
		gameMasterPersonnage1.setGameMaster(user);
		PersonnageWorkBO gameMasterPersonnage2 = new PersonnageWorkBO();
		gameMasterPersonnage2.setGameMaster(user);
		gameMasterPersonnage2.setPlayer(buildUser("toto"));
		PersonnageWorkBO bothPersonnage = new PersonnageWorkBO();
		bothPersonnage.setGameMaster(user);
		bothPersonnage.setPlayer(user);
		PersonnageWorkBO playerPersonnage1 = new PersonnageWorkBO();
		playerPersonnage1.setPlayer(user);
		PersonnageWorkBO playerPersonnage2 = new PersonnageWorkBO();
		playerPersonnage2.setPlayer(user);
		playerPersonnage2.setGameMaster(buildUser("toto"));
		Mockito.when(personnageDao.getGameMasterPersonnageList(user)).thenReturn(Arrays.asList(gameMasterPersonnage1, gameMasterPersonnage2, bothPersonnage));
		Mockito.when(personnageDao.getPlayerPersonnageList(user)).thenReturn(Arrays.asList(playerPersonnage1, playerPersonnage2, bothPersonnage));
		
		// Execute
		boolean result = authentificationBS.removeUser(userName);
		
		// Check
		Assert.assertTrue(result);
		Mockito.verify(personnageDao).deletePersonnage(gameMasterPersonnage1);
		Assert.assertNull(gameMasterPersonnage2.getGameMaster());
		Mockito.verify(personnageDao).deletePersonnage(bothPersonnage);
		Mockito.verify(personnageDao).deletePersonnage(playerPersonnage1);
		Assert.assertNull(playerPersonnage2.getPlayer());
		
		Mockito.verify(userDao).deleteUser(user);
	}
	
	@Test
	public void testRemoveUser_Fail() throws Exception{
		// Prepare
		Mockito.when(userDao.getUser("batman")).thenReturn(null);
		
		// Execute
		boolean result = authentificationBS.removeUser("batman");
		
		// Check
		Assert.assertFalse(result);
	}
	
	@Test
	public void testSendPassword_Success() throws Exception{
		// Prepare
		String username = "batman";
		String digest = "8ee60a2e00c90d7e00d5069188dc115b";
		UserBO user = buildUser(username);
		user.setDigest(digest);
		Mockito.when(userDao.getUser(username)).thenReturn(user);
		
		// Execute
		UserBO resultUser = authentificationBS.sendPassword(username);
		
		// Check
		Assert.assertNotNull(resultUser);
		Mockito.verify(mailResource).send(Mockito.eq(user.getMail()), Mockito.anyString(), Mockito.anyString());
		Assert.assertFalse(resultUser.getDigest().equals(digest));
	}
	
	
	@Test
	public void testSendPassword_Fail() throws Exception{
		// Prepare
		String username = "batman";
		String digest = "8ee60a2e00c90d7e00d5069188dc115b";
		UserBO user = buildUser(username);
		user.setDigest(digest);
		Mockito.when(userDao.getUser(username)).thenReturn(null);
				
		// Execute
		UserBO resultUser = authentificationBS.sendPassword(username);
		
		// Check
		Assert.assertNull(resultUser);
		Mockito.verify(mailResource, Mockito.never()).send(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Assert.assertEquals(digest, user.getDigest());
	}
	
	@Test
	public void testChangePassword() throws Exception{
		// Prepare
		UserBO user = buildUser("batman");
		String digest = "8ee60a2e00c90d7e00d5069188dc115b";
		user.setDigest(digest);
		
		// Execute
		authentificationBS.changePassword(user, "alfred");
		
		// Check
		Assert.assertEquals("batman@gmail.com", user.getMail());
		Assert.assertFalse(user.getDigest().equals(digest));
		Mockito.verify(userDao).saveUser(user);
	}
	
	
	@Test
	public void testChangeMail() throws Exception{
		// Prepare
		UserBO user = buildUser("batman");
		
		// Execute
		authentificationBS.changeMail(user, "batman@hotmail.fr");
		
		// Check
		Assert.assertEquals("batman@hotmail.fr", user.getMail());
		Mockito.verify(userDao).saveUser(user);
	}
	
	
	public static UserBO buildUser(String name){
		UserBO user = new UserBO();
		user.setUsername(name);
		user.setMail(name+"@gmail.com");
		return user;
	}

}
