package com.mrprez.gencross.web.test.bs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Version;
import com.mrprez.gencross.disk.PersonnageFactory;
import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.web.bo.PersonnageDataBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.PersonnageBS;
import com.mrprez.gencross.web.dao.face.IMailResource;
import com.mrprez.gencross.web.dao.face.IPersonnageDAO;
import com.mrprez.gencross.web.dao.face.IUserDAO;

@RunWith(MockitoJUnitRunner.class)
public class PersonnageWorkBSTest {
	@InjectMocks
	private PersonnageBS personnageBS;
	
	@Mock
	private IPersonnageDAO personnageDao;
	
	@Mock
	private IUserDAO userDao;
	
	@Mock
	private IMailResource mailResource;
	
	@Spy
	private PersonnageFactory personnageFactory;
	
	
	@Before
	public void setUp(){
		personnageBS.setPersonnageFactory(personnageFactory);
	}
	
	public static PersonnageWorkBO buildPersonnageWork() throws Exception{
		return buildPersonnageWork("Pavillon Noir");
	}
	
	
	public static PersonnageWorkBO buildPersonnageWork(String pluginName) throws Exception{
		PersonnageFactory personnageFactory = new PersonnageFactory();
		Personnage personnage = personnageFactory.buildNewPersonnage(pluginName);
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPluginName(pluginName);
		PersonnageDataBO personnageData = new PersonnageDataBO();
		personnageData.setPersonnage(personnage);
		personnageWork.setPersonnageData(personnageData);
		return personnageWork;
	}
	
	public static PersonnageWorkBO buildPersonnageWorkMock(Integer id, String name, UserBO player, UserBO gm) throws Exception{
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setId(id);
		personnageWork.setName(name);
		personnageWork.setPlayer(player);
		personnageWork.setGameMaster(gm);
		return personnageWork;
	}
	
	public static PersonnageWorkBO buildPersonnageWorkMock(Integer id, String name, String player, String gm) throws Exception{
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setId(id);
		personnageWork.setName(name);
		if(player!=null){
			personnageWork.setPlayer(AuthentificationBSTest.buildUser(player));
		}
		if(gm!=null){
			personnageWork.setGameMaster(AuthentificationBSTest.buildUser(gm));
		}
		return personnageWork;
	}

	@Test
	public void testCreatePersonnageAsPlayer_WithoutGM() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("GrosBill");
		
		// Execute
		PersonnageWorkBO personnageWork = personnageBS.createPersonnageAsPlayer("Pavillon Noir", "Robert", null, user);
		
		// Check
		Assert.assertEquals(personnageWork.getPluginName(), "Pavillon Noir");
		Assert.assertEquals(personnageWork.getPlayer().getUsername(), "GrosBill");
		Assert.assertNull(personnageWork.getGameMaster());
		Mockito.verify(personnageDao).savePersonnage(personnageWork.getPersonnageData());
		Mockito.verify(personnageDao).savePersonnage(personnageWork.getValidPersonnageData());
		Mockito.verify(personnageDao, Mockito.atLeastOnce()).savePersonnageWork(personnageWork);
	}
	
	@Test
	public void testCreatePersonnageAsPlayer_WithGM() throws Exception{
		// Prepare
		UserBO mrprez = AuthentificationBSTest.buildUser("mrprez");
		Mockito.when(userDao.getUser("mrprez")).thenReturn(mrprez);
		
		UserBO user = AuthentificationBSTest.buildUser("GrosBill");
		
		// Execute
		PersonnageWorkBO personnageWork = personnageBS.createPersonnageAsPlayer("Pavillon Noir", "Robert", "mrprez", user);
		
		// Check
		Assert.assertEquals(personnageWork.getPluginName(), "Pavillon Noir");
		Assert.assertEquals(personnageWork.getPlayer().getUsername(), "GrosBill");
		Assert.assertEquals(personnageWork.getGameMaster().getUsername(), "mrprez");
		Mockito.verify(personnageDao).savePersonnage(personnageWork.getPersonnageData());
		Mockito.verify(personnageDao).savePersonnage(personnageWork.getValidPersonnageData());
		Mockito.verify(personnageDao, Mockito.atLeastOnce()).savePersonnageWork(personnageWork);
	}
	
	@Test
	public void testCreatePersonnageAsGameMaster_WithoutPlayer() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("azerty");
		
		// Execute
		PersonnageWorkBO personnageWork = personnageBS.createPersonnageAsGameMaster("Pavillon Noir", "Robert", null, user);
		
		// Check
		Assert.assertEquals(personnageWork.getPluginName(), "Pavillon Noir");
		Assert.assertEquals(personnageWork.getGameMaster().getUsername(), "azerty");
		Assert.assertNull(personnageWork.getPlayer());
		Mockito.verify(personnageDao).savePersonnage(personnageWork.getPersonnageData());
		Mockito.verify(personnageDao).savePersonnage(personnageWork.getValidPersonnageData());
		Mockito.verify(personnageDao, Mockito.atLeastOnce()).savePersonnageWork(personnageWork);
	}
	
	@Test
	public void testCreatePersonnageAsGameMaster_WithPlayer() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("azerty");
		Mockito.when(userDao.getUser("azerty")).thenReturn(user);
		
		UserBO mrprez = AuthentificationBSTest.buildUser("mrprez");
		
		// Execute
		PersonnageWorkBO personnageWork = personnageBS.createPersonnageAsGameMaster("Pavillon Noir", "Robert", "azerty", mrprez);
		
		// Check
		Assert.assertEquals(personnageWork.getPluginName(), "Pavillon Noir");
		Assert.assertEquals(personnageWork.getGameMaster().getUsername(), "mrprez");
		Assert.assertEquals(personnageWork.getPlayer().getUsername(), "azerty");
		Mockito.verify(personnageDao).savePersonnage(personnageWork.getPersonnageData());
		Mockito.verify(personnageDao).savePersonnage(personnageWork.getValidPersonnageData());
		Mockito.verify(personnageDao, Mockito.atLeastOnce()).savePersonnageWork(personnageWork);
	}
	
	@Test
	public void testCreatePersonnageAsGameMasterAndPlayer() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("mrprez");
		
		// Execute
		PersonnageWorkBO personnageWork = personnageBS.createPersonnageAsGameMasterAndPlayer("Pavillon Noir", "Robert", user);
		
		// Check
		Assert.assertEquals(personnageWork.getPluginName(), "Pavillon Noir");
		Assert.assertEquals(personnageWork.getGameMaster().getUsername(), "mrprez");
		Assert.assertEquals(personnageWork.getPlayer().getUsername(), "mrprez");
		Mockito.verify(personnageDao).savePersonnage(personnageWork.getPersonnageData());
		Mockito.verify(personnageDao).savePersonnage(personnageWork.getValidPersonnageData());
		Mockito.verify(personnageDao, Mockito.atLeastOnce()).savePersonnageWork(personnageWork);
	}
	
	@Test
	public void testDeletePersonnageFromUser() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("mrprez");
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(user);
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		
		// Execute
		personnageBS.deletePersonnageFromUser(1, user);
		
		// Check
		Mockito.verify(personnageDao).deletePersonnage(personnageWork);
	}
	
	@Test
	public void testDeletePersonnageFromUser_WithGm() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("mrprez");
		
		UserBO gm = AuthentificationBSTest.buildUser("azerty");
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(gm);
		personnageWork.setPlayer(user);
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		
		// Execute
		personnageBS.deletePersonnageFromUser(1, user);
		
		// Check
		Mockito.verify(personnageDao, Mockito.never()).deletePersonnage(personnageWork);
		Assert.assertEquals(personnageWork.getGameMaster(), gm);
		Assert.assertNull(personnageWork.getPlayer());
	}
	
	@Test
	public void testDeletePersonnageFromGm() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("mrprez");
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(user);
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		
		// Execute
		personnageBS.deletePersonnageFromUser(1, user);
		
		// Check
		Mockito.verify(personnageDao).deletePersonnage(personnageWork);
	}
	
	@Test
	public void testDeletePersonnageFromGm_WithUser() throws Exception{
		// Prepare
		UserBO gm = AuthentificationBSTest.buildUser("mrprez");
		
		UserBO user = AuthentificationBSTest.buildUser("azerty");
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(gm);
		personnageWork.setPlayer(user);
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		
		// Execute
		personnageBS.deletePersonnageFromUser(1, gm);
		
		// Check
		Mockito.verify(personnageDao, Mockito.never()).deletePersonnage(personnageWork);
		Assert.assertEquals(personnageWork.getPlayer(), user);
		Assert.assertNull(personnageWork.getGameMaster());
	}
	
	@Test
	public void testGetAvailablePersonnageTypes() throws Exception{
		// Prepare
		PluginDescriptor pluginDescriptor1 = new PluginDescriptor();
		pluginDescriptor1.setName("A");
		pluginDescriptor1.setVersion(new Version(1,0));
		PluginDescriptor pluginDescriptor2 = new PluginDescriptor();
		pluginDescriptor2.setName("A");
		pluginDescriptor2.setVersion(new Version(1,1));
		PluginDescriptor pluginDescriptor3 = new PluginDescriptor();
		pluginDescriptor3.setName("B");
		pluginDescriptor3.setVersion(new Version(1,0));
		
		Collection<PluginDescriptor> pluginDescriptorList = Arrays.asList(pluginDescriptor3, pluginDescriptor2, pluginDescriptor1);
		Mockito.when(personnageFactory.getPluginList()).thenReturn(pluginDescriptorList);
		
		// Execute
		Collection<PluginDescriptor> resultList = personnageBS.getAvailablePersonnageTypes();
		
		// Check
		Assert.assertEquals(resultList.size(), 3);
		Assert.assertArrayEquals(resultList.toArray(new PluginDescriptor[3]), new PluginDescriptor[]{pluginDescriptor1, pluginDescriptor2, pluginDescriptor3});
	}
	
	@Test
	public void testGetGameMasterPersonnageList() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("mrprez");
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageDao.getGameMasterPersonnageList(user)).thenReturn(Arrays.asList(personnageWork));
		
		// Execute
		List<PersonnageWorkBO> personnageWorkBOList = personnageBS.getGameMasterPersonnageList(user);
		
		// Check
		Assert.assertEquals(personnageWorkBOList.size(), 1);
		Assert.assertEquals(personnageWorkBOList.get(0), personnageWork);
	}
	
	@Test
	public void testGetPersonnageListFromType() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork1 = new PersonnageWorkBO();
		PersonnageWorkBO personnageWork2 = new PersonnageWorkBO();
		Mockito.when(personnageDao.getPersonnageListFromType("Pavillon Noir")).thenReturn(Arrays.asList(personnageWork1, personnageWork2));
		
		// Execute
		Collection<PersonnageWorkBO> personnageWorkList = personnageBS.getPersonnageListFromType("Pavillon Noir");
		
		// Check
		Assert.assertArrayEquals(personnageWorkList.toArray(new PersonnageWorkBO[0]), new PersonnageWorkBO[]{personnageWork1, personnageWork2});
	}
	
	@Test
	public void testGetPlayerPersonnageList() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("mrprez");
		Mockito.when(personnageDao.getPlayerPersonnageList(user)).thenReturn(new ArrayList<PersonnageWorkBO>());
		
		// Execute
		personnageBS.getPlayerPersonnageList(user);
		
		// Check
		Mockito.verify(personnageDao).getPlayerPersonnageList(user);
	}
	
	@Test
	public void testLoadPersonnage() throws Exception {
		// Prepare
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageDao.loadPersonnageWork(new Integer(1))).thenReturn(personnageWork);
		
		// Execute
		PersonnageWorkBO resultPersonnageWork = personnageBS.loadPersonnage(new Integer(1));
		
		// Check
		Mockito.verify(personnageDao).loadPersonnageWork(new Integer(1));
		Assert.assertEquals(personnageWork, resultPersonnageWork);
	}
	
	@Test
	public void testLoadPersonnage_Fail_Null() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("mrprez");
		
		// Execute
		PersonnageWorkBO resultPersonnageWork = personnageBS.loadPersonnage(null, user);
		
		// Check
		Assert.assertNull(resultPersonnageWork);
	}
	
	@Test
	public void testLoadPersonnage_Success_RightPlayer() throws Exception {
		// Prepare
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		
		// Execute
		PersonnageWorkBO result = personnageBS.loadPersonnage(1, AuthentificationBSTest.buildUser("mrprez"));
		
		// Check
		Assert.assertEquals(personnageWork, result);
	}
	
	@Test
	public void testLoadPersonnage_Fail_WrongPlayer() throws Exception {
		// Prepare
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		
		// Execute
		PersonnageWorkBO result = personnageBS.loadPersonnage(1, AuthentificationBSTest.buildUser("azerty"));
		
		// Check
		Assert.assertNull(result);
	}
		

	@Test
	public void testLoadPersonnage_Success_RightGM() throws Exception {
		// Prepare
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		
		// Execute
		PersonnageWorkBO result = personnageBS.loadPersonnage(1, AuthentificationBSTest.buildUser("mrprez"));
		
		// Check
		Assert.assertEquals(personnageWork, result);
	}
	
	
	@Test
	public void testLoadPersonnage_Success_WrongGM() throws Exception {
		// Prepare
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		
		// Execute
		PersonnageWorkBO result = personnageBS.loadPersonnage(1, AuthentificationBSTest.buildUser("azerty"));
		
		// Check
		Assert.assertNull(result);
	}
	
	@Test
	public void getLoadPersonnageAsGameMaster() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		
		// Execute
		PersonnageWorkBO result = personnageBS.loadPersonnageAsGameMaster(1, AuthentificationBSTest.buildUser("mrprez"));
		
		// Check
		Assert.assertEquals(personnageWork, result);
	}
	
	@Test
	public void getLoadPersonnageAsGameMaster_Fail_GmNull() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		
		// Execute
		PersonnageWorkBO result = personnageBS.loadPersonnageAsGameMaster(1, AuthentificationBSTest.buildUser("mrprez"));
		
		// Check
		Assert.assertNull(result);
	}
	
	
	@Test
	public void getLoadPersonnageAsGameMaster_Fail_NullId() throws Exception{
		// Execute
		PersonnageWorkBO result = personnageBS.loadPersonnageAsGameMaster(null, AuthentificationBSTest.buildUser("mrprez"));
		
		// Check
		Assert.assertNull(result);
	}
	
	@Test
	public void getLoadPersonnageAsGameMaster_Fail_NotRightGm() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		
		// Execute
		PersonnageWorkBO result = personnageBS.loadPersonnageAsGameMaster(1, AuthentificationBSTest.buildUser("azerty"));
		
		// Check
		Assert.assertNull(result);
	}
	
	@Test
	public void getLoadPersonnageAsPlayer() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		
		// Execute
		PersonnageWorkBO result = personnageBS.loadPersonnageAsPlayer(1, AuthentificationBSTest.buildUser("mrprez"));
		
		// Check
		Assert.assertEquals(personnageWork, result);
	}
	

	@Test
	public void getLoadPersonnageAsPlayer_Fail_PlayerNull() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		
		// Execute
		PersonnageWorkBO result = personnageBS.loadPersonnageAsPlayer(1, AuthentificationBSTest.buildUser("mrprez"));
		
		// Check
		Assert.assertNull(result);
	}
	
	@Test
	public void getLoadPersonnageAsPlayer_Fail_NotRightPlayer() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(AuthentificationBSTest.buildUser("azerty"));
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		
		// Execute
		PersonnageWorkBO result = personnageBS.loadPersonnageAsPlayer(1, AuthentificationBSTest.buildUser("mrprez"));
		
		// Check
		Assert.assertNull(result);
	}
	
	@Test
	public void getLoadPersonnageAsPlayer_Fail_NullId() throws Exception{
		// Execute
		PersonnageWorkBO result = personnageBS.loadPersonnageAsPlayer(null, AuthentificationBSTest.buildUser("mrprez"));
		
		// Check
		Assert.assertNull(result);
	}
	
	@Test
	public void testSavePersonnage() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		
		// Execute
		personnageBS.savePersonnage(personnageWork);
		
		// Check
		Mockito.verify(personnageDao).savePersonnage(personnageWork.getPersonnageData());
	}
	
	@Test
	public void testSavePersonnageBackground() throws Exception {
		// Prepare
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		
		// Execute
		personnageBS.savePersonnageBackground(personnageWork, "Background Test");
		
		// Check
		Mockito.verify(personnageDao, Mockito.atLeastOnce()).savePersonnageWork(personnageWork);
		Assert.assertEquals("Background Test", personnageWork.getBackground());
	}
	
	@Test
	public void testUnvalidatePersonnage() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		personnageWork.setValidPersonnageData(new PersonnageDataBO());
		personnageWork.getValidPersonnageData().setPersonnage(personnageWork.getPersonnage().clone());
		personnageWork.getPersonnage().setNewValue("Attributs#Erudition", 4);
		
		// Execute
		personnageBS.unvalidatePersonnage(personnageWork);
		
		// Check
		Assert.assertEquals(1, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
	}
	
	@Test
	public void testValidatePersonnage() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		personnageWork.setValidPersonnageData(new PersonnageDataBO());
		personnageWork.getValidPersonnageData().setPersonnage(personnageWork.getPersonnage().clone());
		personnageWork.getPersonnage().setNewValue("Attributs#Erudition", 4);
		
		// Execute
		personnageBS.validatePersonnage(personnageWork);
		
		// Check
		Assert.assertEquals(4, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
	}
	
	@Test
	public void testAttribute_Success_WithOld() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		UserBO oldPlayer = AuthentificationBSTest.buildUser("oldPlayer");
		UserBO oldGameMaster = AuthentificationBSTest.buildUser("oldGm");
		personnageWork.setPlayer(oldPlayer);
		personnageWork.setGameMaster(oldGameMaster);
		UserBO player = AuthentificationBSTest.buildUser("robin");
		UserBO gameMaster = AuthentificationBSTest.buildUser("batman");
		
		// Execute
		personnageBS.attribute(personnageWork, player, gameMaster);
		
		// Check
		Assert.assertEquals(player, personnageWork.getPlayer());
		Assert.assertEquals(gameMaster, personnageWork.getGameMaster());
		Mockito.verify(personnageBS.getMailResource()).send(Mockito.eq(oldPlayer.getMail()), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(personnageBS.getMailResource()).send(Mockito.eq(oldGameMaster.getMail()), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(personnageBS.getMailResource()).send(Mockito.eq(player.getMail()), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(personnageBS.getMailResource()).send(Mockito.eq(gameMaster.getMail()), Mockito.anyString(), Mockito.anyString());
	}
	
	
	@Test
	public void testAttribute_Success() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		UserBO player = AuthentificationBSTest.buildUser("robin");
		UserBO gameMaster = AuthentificationBSTest.buildUser("batman");
		
		// Execute
		personnageBS.attribute(personnageWork, player, gameMaster);
		
		// Check
		Assert.assertEquals(player, personnageWork.getPlayer());
		Assert.assertEquals(gameMaster, personnageWork.getGameMaster());
		Mockito.verify(personnageBS.getMailResource()).send(Mockito.eq(player.getMail()), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(personnageBS.getMailResource()).send(Mockito.eq(gameMaster.getMail()), Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	public void testAttribute_Success_RemovePlayer() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		UserBO oldPlayer = AuthentificationBSTest.buildUser("oldPlayer");
		UserBO oldGameMaster = AuthentificationBSTest.buildUser("oldGm");
		personnageWork.setPlayer(oldPlayer);
		personnageWork.setGameMaster(oldGameMaster);
		
		// Execute
		personnageBS.attribute(personnageWork, null, oldGameMaster);
		
		// Check
		Assert.assertNull(personnageWork.getPlayer());
		Assert.assertEquals(oldGameMaster, personnageWork.getGameMaster());
		Mockito.verify(personnageBS.getMailResource()).send(Mockito.eq(oldPlayer.getMail()), Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	public void testAttribute_Success_RemoveGm() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		UserBO oldPlayer = AuthentificationBSTest.buildUser("oldPlayer");
		UserBO oldGameMaster = AuthentificationBSTest.buildUser("oldGm");
		personnageWork.setPlayer(oldPlayer);
		personnageWork.setGameMaster(oldGameMaster);
		
		// Execute
		personnageBS.attribute(personnageWork, oldPlayer, null);
		
		// Check
		Assert.assertNull(personnageWork.getGameMaster());
		Assert.assertEquals(oldPlayer, personnageWork.getPlayer());
		Mockito.verify(personnageBS.getMailResource()).send(Mockito.eq(oldGameMaster.getMail()), Mockito.anyString(), Mockito.anyString());
	}
	
	
}
