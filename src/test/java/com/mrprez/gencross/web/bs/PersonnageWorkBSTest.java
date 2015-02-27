package com.mrprez.gencross.web.bs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Version;
import com.mrprez.gencross.disk.PersonnageFactory;
import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.web.bo.PersonnageDataBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;

public class PersonnageWorkBSTest {
	
	public static PersonnageWorkBO buildPersonnageWork() throws Exception{
		return buildPersonnageWork("Pavillon Noir");
	}
	
	
	public static PersonnageWorkBO buildPersonnageWork(String pluginName) throws Exception{
		PersonnageFactory personnageFactory = new PersonnageFactory();
		Personnage personnage = personnageFactory.buildNewPersonnage(pluginName);
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		PersonnageDataBO personnageData = new PersonnageDataBO();
		personnageData.setPersonnage(personnage);
		personnageWork.setPersonnageData(personnageData);
		return personnageWork;
	}

	@Test
	public void testCreatePersonnageAsPlayer() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		UserBO mrprez = AuthentificationBSTest.buildUser("mrprez");
		Mockito.when(personnageBS.getUserDAO().getUser("mrprez")).thenReturn(mrprez);
		
		UserBO user = AuthentificationBSTest.buildUser("GrosBill");
		
		PersonnageWorkBO personnageWork = personnageBS.createPersonnageAsPlayer("Pavillon Noir", "Robert", "mrprez", user);
		Assert.assertEquals(personnageWork.getPluginName(), "Pavillon Noir");
		Assert.assertEquals(personnageWork.getPlayer().getUsername(), "GrosBill");
		Assert.assertEquals(personnageWork.getGameMaster().getUsername(), "mrprez");
		Mockito.verify(personnageBS.getPersonnageDAO()).savePersonnage(personnageWork.getPersonnageData());
		Mockito.verify(personnageBS.getPersonnageDAO()).savePersonnage(personnageWork.getValidPersonnageData());
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnageWork(personnageWork);
		
		personnageWork = personnageBS.createPersonnageAsPlayer("Pavillon Noir", "Robert", null, user);
		Assert.assertEquals(personnageWork.getPluginName(), "Pavillon Noir");
		Assert.assertEquals(personnageWork.getPlayer().getUsername(), "GrosBill");
		Assert.assertNull(personnageWork.getGameMaster());
		Mockito.verify(personnageBS.getPersonnageDAO()).savePersonnage(personnageWork.getPersonnageData());
		Mockito.verify(personnageBS.getPersonnageDAO()).savePersonnage(personnageWork.getValidPersonnageData());
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnageWork(personnageWork);
	}
	
	@Test
	public void testCreatePersonnageAsGameMaster() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		UserBO user = AuthentificationBSTest.buildUser("azerty");
		Mockito.when(personnageBS.getUserDAO().getUser("azerty")).thenReturn(user);
		
		UserBO mrprez = AuthentificationBSTest.buildUser("mrprez");
		
		PersonnageWorkBO personnageWork = personnageBS.createPersonnageAsGameMaster("Pavillon Noir", "Robert", "azerty", mrprez);
		Assert.assertEquals(personnageWork.getPluginName(), "Pavillon Noir");
		Assert.assertEquals(personnageWork.getGameMaster().getUsername(), "mrprez");
		Assert.assertEquals(personnageWork.getPlayer().getUsername(), "azerty");
		Mockito.verify(personnageBS.getPersonnageDAO()).savePersonnage(personnageWork.getPersonnageData());
		Mockito.verify(personnageBS.getPersonnageDAO()).savePersonnage(personnageWork.getValidPersonnageData());
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnageWork(personnageWork);
		
		personnageWork = personnageBS.createPersonnageAsGameMaster("Pavillon Noir", "Robert", null, user);
		Assert.assertEquals(personnageWork.getPluginName(), "Pavillon Noir");
		Assert.assertEquals(personnageWork.getGameMaster().getUsername(), "azerty");
		Assert.assertNull(personnageWork.getPlayer());
		Mockito.verify(personnageBS.getPersonnageDAO()).savePersonnage(personnageWork.getPersonnageData());
		Mockito.verify(personnageBS.getPersonnageDAO()).savePersonnage(personnageWork.getValidPersonnageData());
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnageWork(personnageWork);
	}
	
	@Test
	public void testCreatePersonnageAsGameMasterAndPlayer() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		UserBO user = AuthentificationBSTest.buildUser("mrprez");
		
		PersonnageWorkBO personnageWork = personnageBS.createPersonnageAsGameMasterAndPlayer("Pavillon Noir", "Robert", user);
		Assert.assertEquals(personnageWork.getPluginName(), "Pavillon Noir");
		Assert.assertEquals(personnageWork.getGameMaster().getUsername(), "mrprez");
		Assert.assertEquals(personnageWork.getPlayer().getUsername(), "mrprez");
		Mockito.verify(personnageBS.getPersonnageDAO()).savePersonnage(personnageWork.getPersonnageData());
		Mockito.verify(personnageBS.getPersonnageDAO()).savePersonnage(personnageWork.getValidPersonnageData());
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnageWork(personnageWork);
	}
	
	@Test
	public void testDeletePersonnageFromUser() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		UserBO user = AuthentificationBSTest.buildUser("mrprez");
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(user);
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		
		personnageBS.deletePersonnageFromUser(1, user);
		Mockito.verify(personnageBS.getPersonnageDAO()).deletePersonnage(personnageWork);
	}
	
	@Test
	public void testDeletePersonnageFromUser_WithGm() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		
		UserBO user = AuthentificationBSTest.buildUser("mrprez");
		
		UserBO gm = AuthentificationBSTest.buildUser("azerty");
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(gm);
		personnageWork.setPlayer(user);
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		
		personnageBS.deletePersonnageFromUser(1, user);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.never()).deletePersonnage(personnageWork);
		Assert.assertEquals(personnageWork.getGameMaster(), gm);
		Assert.assertNull(personnageWork.getPlayer());
	}
	
	@Test
	public void testDeletePersonnageFromGm() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		UserBO user = AuthentificationBSTest.buildUser("mrprez");
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(user);
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		
		personnageBS.deletePersonnageFromUser(1, user);
		Mockito.verify(personnageBS.getPersonnageDAO()).deletePersonnage(personnageWork);
	}
	
	@Test
	public void testDeletePersonnageFromGm_WithUser() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		
		UserBO gm = AuthentificationBSTest.buildUser("mrprez");
		
		UserBO user = AuthentificationBSTest.buildUser("azerty");
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(gm);
		personnageWork.setPlayer(user);
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		
		personnageBS.deletePersonnageFromUser(1, gm);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.never()).deletePersonnage(personnageWork);
		Assert.assertEquals(personnageWork.getPlayer(), user);
		Assert.assertNull(personnageWork.getGameMaster());
	}
	
	@Test
	public void testGetAvailablePersonnageTypes() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		personnageBS.setPersonnageFactory(Mockito.mock(PersonnageFactory.class));
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
		Mockito.when(personnageBS.getPersonnageFactory().getPluginList()).thenReturn(pluginDescriptorList);
		
		Collection<PluginDescriptor> resultList = personnageBS.getAvailablePersonnageTypes();
		
		Assert.assertEquals(resultList.size(), 3);
		Assert.assertArrayEquals(resultList.toArray(new PluginDescriptor[3]), new PluginDescriptor[]{pluginDescriptor1, pluginDescriptor2, pluginDescriptor3});
		
	}
	
	@Test
	public void testGetGameMasterPersonnageList() throws Exception {
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		UserBO user = AuthentificationBSTest.buildUser("mrprez");
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageBS.getPersonnageDAO().getGameMasterPersonnageList(user)).thenReturn(Arrays.asList(personnageWork));
		List<PersonnageWorkBO> personnageWorkBOList = personnageBS.getGameMasterPersonnageList(user);
		Assert.assertEquals(personnageWorkBOList.size(), 1);
		Assert.assertEquals(personnageWorkBOList.get(0), personnageWork);
	}
	
	@Test
	public void testGetPersonnageListFromType() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		PersonnageWorkBO personnageWork1 = new PersonnageWorkBO();
		PersonnageWorkBO personnageWork2 = new PersonnageWorkBO();
		Mockito.when(personnageBS.getPersonnageDAO().getPersonnageListFromType("Pavillon Noir")).thenReturn(Arrays.asList(personnageWork1, personnageWork2));
		Collection<PersonnageWorkBO> personnageWorkList = personnageBS.getPersonnageListFromType("Pavillon Noir");
		Assert.assertArrayEquals(personnageWorkList.toArray(new PersonnageWorkBO[0]), new PersonnageWorkBO[]{personnageWork1, personnageWork2});
	}
	
	@Test
	public void testGetPlayerPersonnageList() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		UserBO user = AuthentificationBSTest.buildUser("mrprez");
		Mockito.when(personnageBS.getPersonnageDAO().getPlayerPersonnageList(user)).thenReturn(new ArrayList<PersonnageWorkBO>());
		
		personnageBS.getPlayerPersonnageList(user);
		Mockito.verify(personnageBS.getPersonnageDAO()).getPlayerPersonnageList(user);
	}
	
	@Test
	public void testLoadPersonnage() throws Exception {
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(new Integer(1))).thenReturn(personnageWork);
		
		PersonnageWorkBO resultPersonnageWork = personnageBS.loadPersonnage(new Integer(1));
		
		Mockito.verify(personnageBS.getPersonnageDAO()).loadPersonnageWork(new Integer(1));
		Assert.assertEquals(personnageWork, resultPersonnageWork);
	}
	
	@Test
	public void testLoadPersonnage_Fail_Null() throws Exception {
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		UserBO user = AuthentificationBSTest.buildUser("mrprez");
		
		PersonnageWorkBO resultPersonnageWork = personnageBS.loadPersonnage(null, user);
		
		Assert.assertNull(resultPersonnageWork);
	}
	
	@Test
	public void testLoadPersonnage_WithUser() throws Exception {
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		
		PersonnageWorkBO personnageWork;
		PersonnageWorkBO result;
		
		personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		result = personnageBS.loadPersonnage(1, AuthentificationBSTest.buildUser("mrprez"));
		Assert.assertEquals(personnageWork, result);
		
		personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		result = personnageBS.loadPersonnage(1, AuthentificationBSTest.buildUser("azerty"));
		Assert.assertNull(result);
		
		personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		result = personnageBS.loadPersonnage(1, AuthentificationBSTest.buildUser("mrprez"));
		Assert.assertEquals(personnageWork, result);
		
		personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		result = personnageBS.loadPersonnage(1, AuthentificationBSTest.buildUser("azerty"));
		Assert.assertNull(result);
		
	}
	
	@Test
	public void getLoadPersonnageAsGameMaster() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		PersonnageWorkBO result = personnageBS.loadPersonnageAsGameMaster(1, AuthentificationBSTest.buildUser("mrprez"));
		Assert.assertEquals(personnageWork, result);
	}
	
	@Test
	public void getLoadPersonnageAsGameMaster_Fail_GmNull() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		PersonnageWorkBO result = personnageBS.loadPersonnageAsGameMaster(1, AuthentificationBSTest.buildUser("mrprez"));
		Assert.assertNull(result);
	}
	
	
	@Test
	public void getLoadPersonnageAsGameMaster_Fail_NullId() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		
		PersonnageWorkBO result = personnageBS.loadPersonnageAsGameMaster(null, AuthentificationBSTest.buildUser("mrprez"));
		Assert.assertNull(result);
	}
	
	@Test
	public void getLoadPersonnageAsGameMaster_Fail_NotRightGm() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		PersonnageWorkBO result = personnageBS.loadPersonnageAsGameMaster(1, AuthentificationBSTest.buildUser("azerty"));
		Assert.assertNull(result);
	}
	
	@Test
	public void getLoadPersonnageAsPlayer() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		
		PersonnageWorkBO result = personnageBS.loadPersonnageAsPlayer(1, AuthentificationBSTest.buildUser("mrprez"));
		
		Assert.assertEquals(personnageWork, result);
	}
	
	

	@Test
	public void getLoadPersonnageAsPlayer_Fail_PlayerNull() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		
		PersonnageWorkBO result = personnageBS.loadPersonnageAsPlayer(1, AuthentificationBSTest.buildUser("mrprez"));
		
		Assert.assertNull(result);
	}
	
	@Test
	public void getLoadPersonnageAsPlayer_Fail_NotRightPlayer() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(AuthentificationBSTest.buildUser("azerty"));
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		
		PersonnageWorkBO result = personnageBS.loadPersonnageAsPlayer(1, AuthentificationBSTest.buildUser("mrprez"));
		
		Assert.assertNull(result);
	}
	
	@Test
	public void getLoadPersonnageAsPlayer_Fail_NullId() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		PersonnageWorkBO result = personnageBS.loadPersonnageAsPlayer(null, AuthentificationBSTest.buildUser("mrprez"));
		
		Assert.assertNull(result);
	}
	
	@Test
	public void testSavePersonnage() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		
		personnageBS.savePersonnage(personnageWork);
		
		Mockito.verify(personnageBS.getPersonnageDAO()).savePersonnage(personnageWork.getPersonnageData());
	}
	
	@Test
	public void testSavePersonnageBackground() throws Exception {
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		
		personnageBS.savePersonnageBackground(personnageWork, "Background Test");
		
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnageWork(personnageWork);
		Assert.assertEquals("Background Test", personnageWork.getBackground());
	}
	
	@Test
	public void testSavePersonnageWork() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");

		personnageBS.savePersonnageWork(personnageWork);
		
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnageWork(personnageWork);
	}

	@Test
	public void testUnvalidatePersonnage() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		personnageWork.setValidPersonnageData(new PersonnageDataBO());
		personnageWork.getValidPersonnageData().setPersonnage(personnageWork.getPersonnage().clone());
		personnageWork.getPersonnage().setNewValue("Attributs#Erudition", 4);
		
		personnageBS.unvalidatePersonnage(personnageWork);
		
		Assert.assertEquals(1, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
	}
	
	@Test
	public void testValidatePersonnage() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		personnageWork.setValidPersonnageData(new PersonnageDataBO());
		personnageWork.getValidPersonnageData().setPersonnage(personnageWork.getPersonnage().clone());
		personnageWork.getPersonnage().setNewValue("Attributs#Erudition", 4);
		
		personnageBS.validatePersonnage(personnageWork);
		
		Assert.assertEquals(4, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
	}
	
	@Test
	public void testAttribute_Success_WithOld() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		UserBO oldPlayer = AuthentificationBSTest.buildUser("oldPlayer");
		UserBO oldGameMaster = AuthentificationBSTest.buildUser("oldGm");
		personnageWork.setPlayer(oldPlayer);
		personnageWork.setGameMaster(oldGameMaster);
		UserBO player = AuthentificationBSTest.buildUser("robin");
		UserBO gameMaster = AuthentificationBSTest.buildUser("batman");
		
		personnageBS.attribute(personnageWork, player, gameMaster);
		
		Assert.assertEquals(player, personnageWork.getPlayer());
		Assert.assertEquals(gameMaster, personnageWork.getGameMaster());
		Mockito.verify(personnageBS.getMailResource()).send(Mockito.eq(oldPlayer.getMail()), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(personnageBS.getMailResource()).send(Mockito.eq(oldGameMaster.getMail()), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(personnageBS.getMailResource()).send(Mockito.eq(player.getMail()), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(personnageBS.getMailResource()).send(Mockito.eq(gameMaster.getMail()), Mockito.anyString(), Mockito.anyString());
	}
	
	
	@Test
	public void testAttribute_Success() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		UserBO player = AuthentificationBSTest.buildUser("robin");
		UserBO gameMaster = AuthentificationBSTest.buildUser("batman");
		
		personnageBS.attribute(personnageWork, player, gameMaster);
		
		Assert.assertEquals(player, personnageWork.getPlayer());
		Assert.assertEquals(gameMaster, personnageWork.getGameMaster());
		Mockito.verify(personnageBS.getMailResource()).send(Mockito.eq(player.getMail()), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(personnageBS.getMailResource()).send(Mockito.eq(gameMaster.getMail()), Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	public void testAttribute_Success_RemovePlayer() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		UserBO oldPlayer = AuthentificationBSTest.buildUser("oldPlayer");
		UserBO oldGameMaster = AuthentificationBSTest.buildUser("oldGm");
		personnageWork.setPlayer(oldPlayer);
		personnageWork.setGameMaster(oldGameMaster);
		
		personnageBS.attribute(personnageWork, null, oldGameMaster);
		
		Assert.assertNull(personnageWork.getPlayer());
		Assert.assertEquals(oldGameMaster, personnageWork.getGameMaster());
		Mockito.verify(personnageBS.getMailResource()).send(Mockito.eq(oldPlayer.getMail()), Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	public void testAttribute_Success_RemoveGm() throws Exception{
		PersonnageBS personnageBS = PersonnageBSTest.buildPersonnageBS();
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		UserBO oldPlayer = AuthentificationBSTest.buildUser("oldPlayer");
		UserBO oldGameMaster = AuthentificationBSTest.buildUser("oldGm");
		personnageWork.setPlayer(oldPlayer);
		personnageWork.setGameMaster(oldGameMaster);
		
		personnageBS.attribute(personnageWork, oldPlayer, null);
		
		Assert.assertNull(personnageWork.getGameMaster());
		Assert.assertEquals(oldPlayer, personnageWork.getPlayer());
		Mockito.verify(personnageBS.getMailResource()).send(Mockito.eq(oldGameMaster.getMail()), Mockito.anyString(), Mockito.anyString());
	}
	
	
}
