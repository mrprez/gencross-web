package com.mrprez.gencross.web.bs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.PoolPoint;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.Version;
import com.mrprez.gencross.disk.PersonnageFactory;
import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.history.HistoryItem;
import com.mrprez.gencross.value.DoubleValue;
import com.mrprez.gencross.web.bo.PersonnageDataBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.dao.PersonnageDAO;
import com.mrprez.gencross.web.dao.face.IMailResource;
import com.mrprez.gencross.web.dao.face.IUserDAO;

public class PersonnageBSTest {
	
	
	private PersonnageBS buildPeronnageBS() throws Exception{
		PersonnageBS personnageBS = new PersonnageBS();
		PersonnageDAO personnageDAO = Mockito.mock(PersonnageDAO.class);
		personnageBS.setPersonnageDAO(personnageDAO);
		
		personnageBS.setUserDAO(Mockito.mock(IUserDAO.class));
		
		personnageBS.setPersonnageFactory(new PersonnageFactory());
		
		personnageBS.setMailResource(Mockito.mock(IMailResource.class));
		
		return personnageBS;
	}
	
	private PersonnageWorkBO buildPersonnageWork(String pluginName) throws Exception{
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
		PersonnageBS personnageBS = buildPeronnageBS();
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
		PersonnageBS personnageBS = buildPeronnageBS();
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
		PersonnageBS personnageBS = buildPeronnageBS();
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
		PersonnageBS personnageBS = buildPeronnageBS();
		UserBO user = AuthentificationBSTest.buildUser("mrprez");
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(user);
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		
		personnageBS.deletePersonnageFromUser(1, user);
		Mockito.verify(personnageBS.getPersonnageDAO()).deletePersonnage(personnageWork);
	}
	
	@Test
	public void testDeletePersonnageFromUser_WithGm() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		
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
		PersonnageBS personnageBS = buildPeronnageBS();
		UserBO user = AuthentificationBSTest.buildUser("mrprez");
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(user);
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		
		personnageBS.deletePersonnageFromUser(1, user);
		Mockito.verify(personnageBS.getPersonnageDAO()).deletePersonnage(personnageWork);
	}
	
	@Test
	public void testDeletePersonnageFromGm_WithUser() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		
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
		PersonnageBS personnageBS = buildPeronnageBS();
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
		PersonnageBS personnageBS = buildPeronnageBS();
		UserBO user = AuthentificationBSTest.buildUser("mrprez");
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageBS.getPersonnageDAO().getGameMasterPersonnageList(user)).thenReturn(Arrays.asList(personnageWork));
		List<PersonnageWorkBO> personnageWorkBOList = personnageBS.getGameMasterPersonnageList(user);
		Assert.assertEquals(personnageWorkBOList.size(), 1);
		Assert.assertEquals(personnageWorkBOList.get(0), personnageWork);
	}
	
	@Test
	public void testGetPersonnageListFromType() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork1 = new PersonnageWorkBO();
		PersonnageWorkBO personnageWork2 = new PersonnageWorkBO();
		Mockito.when(personnageBS.getPersonnageDAO().getPersonnageListFromType("Pavillon Noir")).thenReturn(Arrays.asList(personnageWork1, personnageWork2));
		Collection<PersonnageWorkBO> personnageWorkList = personnageBS.getPersonnageListFromType("Pavillon Noir");
		Assert.assertArrayEquals(personnageWorkList.toArray(new PersonnageWorkBO[0]), new PersonnageWorkBO[]{personnageWork1, personnageWork2});
	}
	
	@Test
	public void testGetPlayerPersonnageList() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		UserBO user = AuthentificationBSTest.buildUser("mrprez");
		Mockito.when(personnageBS.getPersonnageDAO().getPlayerPersonnageList(user)).thenReturn(new ArrayList<PersonnageWorkBO>());
		
		personnageBS.getPlayerPersonnageList(user);
		Mockito.verify(personnageBS.getPersonnageDAO()).getPlayerPersonnageList(user);
	}
	
	@Test
	public void testLoadPersonnage() throws Exception {
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(new Integer(1))).thenReturn(personnageWork);
		
		PersonnageWorkBO resultPersonnageWork = personnageBS.loadPersonnage(new Integer(1));
		
		Mockito.verify(personnageBS.getPersonnageDAO()).loadPersonnageWork(new Integer(1));
		Assert.assertEquals(personnageWork, resultPersonnageWork);
	}
	
	@Test
	public void testLoadPersonnage_Fail_Null() throws Exception {
		PersonnageBS personnageBS = buildPeronnageBS();
		UserBO user = AuthentificationBSTest.buildUser("mrprez");
		
		PersonnageWorkBO resultPersonnageWork = personnageBS.loadPersonnage(null, user);
		
		Assert.assertNull(resultPersonnageWork);
	}
	
	@Test
	public void testLoadPersonnage_WithUser() throws Exception {
		PersonnageBS personnageBS = buildPeronnageBS();
		
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
		PersonnageBS personnageBS = buildPeronnageBS();
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		PersonnageWorkBO result = personnageBS.loadPersonnageAsGameMaster(1, AuthentificationBSTest.buildUser("mrprez"));
		Assert.assertEquals(personnageWork, result);
	}
	
	@Test
	public void getLoadPersonnageAsGameMaster_Fail_GmNull() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		PersonnageWorkBO result = personnageBS.loadPersonnageAsGameMaster(1, AuthentificationBSTest.buildUser("mrprez"));
		Assert.assertNull(result);
	}
	
	
	@Test
	public void getLoadPersonnageAsGameMaster_Fail_NullId() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		
		PersonnageWorkBO result = personnageBS.loadPersonnageAsGameMaster(null, AuthentificationBSTest.buildUser("mrprez"));
		Assert.assertNull(result);
	}
	
	@Test
	public void getLoadPersonnageAsGameMaster_Fail_NotRightGm() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		PersonnageWorkBO result = personnageBS.loadPersonnageAsGameMaster(1, AuthentificationBSTest.buildUser("azerty"));
		Assert.assertNull(result);
	}
	
	@Test
	public void getLoadPersonnageAsPlayer() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		
		PersonnageWorkBO result = personnageBS.loadPersonnageAsPlayer(1, AuthentificationBSTest.buildUser("mrprez"));
		
		Assert.assertEquals(personnageWork, result);
	}
	
	

	@Test
	public void getLoadPersonnageAsPlayer_Fail_PlayerNull() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		
		PersonnageWorkBO result = personnageBS.loadPersonnageAsPlayer(1, AuthentificationBSTest.buildUser("mrprez"));
		
		Assert.assertNull(result);
	}
	
	@Test
	public void getLoadPersonnageAsPlayer_Fail_NotRightPlayer() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(AuthentificationBSTest.buildUser("azerty"));
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		
		PersonnageWorkBO result = personnageBS.loadPersonnageAsPlayer(1, AuthentificationBSTest.buildUser("mrprez"));
		
		Assert.assertNull(result);
	}
	
	@Test
	public void getLoadPersonnageAsPlayer_Fail_NullId() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO result = personnageBS.loadPersonnageAsPlayer(null, AuthentificationBSTest.buildUser("mrprez"));
		
		Assert.assertNull(result);
	}
	
	@Test
	public void testModifyHistory() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		Personnage personnage = personnageWork.getPersonnage();
		personnage.setNewValue("Attributs#Erudition", 3);
		
		personnageBS.modifyHistory(personnageWork, "Expérience", 8, 0);
		Assert.assertEquals(13, personnage.getPointPools().get("Attributs").getRemaining());
		Assert.assertEquals(-8, personnage.getPointPools().get("Expérience").getRemaining());
		Assert.assertEquals("Attributs#Erudition", personnage.getHistory().get(0).getAbsoluteName());
		Assert.assertEquals(HistoryItem.UPDATE, personnage.getHistory().get(0).getAction());
		Assert.assertEquals("Expérience", personnage.getHistory().get(0).getPointPool());
		Assert.assertEquals(8, personnage.getHistory().get(0).getCost());
	}
	
	@Test
	public void testModifyPointPool() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		
		personnageBS.modifyPointPool(personnageWork, "Expérience", 10);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals(personnageWork.getPersonnage().getPointPools().get("Expérience").getRemaining(), 10);
	}
	
	@Test
	public void testModifyPointPool_Fail() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		Map<String, Integer> pointPoolCopy = new HashMap<String, Integer>();
		for(Entry<String, PoolPoint> pointPoolEntry : personnageWork.getPersonnage().getPointPools().entrySet()){
			pointPoolCopy.put(pointPoolEntry.getKey(), pointPoolEntry.getValue().getTotal());
		}
		
		personnageBS.modifyPointPool(personnageWork, "PA", 10);
		
		for(Entry<String, PoolPoint> pointPoolEntry : personnageWork.getPersonnage().getPointPools().entrySet()){
			Assert.assertEquals(pointPoolCopy.get(pointPoolEntry.getKey()).intValue(), pointPoolEntry.getValue().getTotal());
		}
		
	}
	
	@Test
	public void testNextPhase() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPersonnageData(new PersonnageDataBO());
		personnageWork.getPersonnageData().setPersonnage(Mockito.mock(Personnage.class));
		
		personnageBS.nextPhase(personnageWork);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Mockito.verify(personnageWork.getPersonnage()).passToNextPhase();
	}
	
	@Test
	public void testRemoveProperty() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		Property motherProperty = personnageWork.getPersonnage().getProperty("Compétences#Connaissance spécialisée");
		personnageWork.getPersonnage().addPropertyToMotherProperty(motherProperty.getSubProperties().getOptions().get("Droit"));
		
		personnageBS.removeProperty(personnageWork, "Compétences#Connaissance spécialisée#Droit");
		
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertNull(personnageWork.getPersonnage().getProperty("Compétences#Connaissance spécialisée#Droit"));
	}
	
	
	@Test
	public void testRemoveProperty_Fail() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		Property motherProperty = personnageWork.getPersonnage().getProperty("Compétences#Connaissance spécialisée");
		personnageWork.getPersonnage().addPropertyToMotherProperty(motherProperty.getSubProperties().getOptions().get("Droit"));
		
		personnageBS.removeProperty(personnageWork, "Compétences#Commerce");
		
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
	}
	
	@Test
	public void testSavePersonnage() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		
		personnageBS.savePersonnage(personnageWork);
		
		Mockito.verify(personnageBS.getPersonnageDAO()).savePersonnage(personnageWork.getPersonnageData());
	}
	
	@Test
	public void testSavePersonnageBackground() throws Exception {
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		
		personnageBS.savePersonnageBackground(personnageWork, "Background Test");
		
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnageWork(personnageWork);
		Assert.assertEquals("Background Test", personnageWork.getBackground());
	}
	
	@Test
	public void testSavePersonnageWork() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");

		personnageBS.savePersonnageWork(personnageWork);
		
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnageWork(personnageWork);
	}

	@Test
	public void testSetNewValue_Success() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");

		boolean result = personnageBS.setNewValue(personnageWork, "4", "Attributs#Erudition");
		
		Assert.assertTrue(result);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals(4, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
		Assert.assertEquals("Attributs#Erudition", personnageWork.getPersonnage().getHistory().get(0).getAbsoluteName());
	}
	
	@Test
	public void testSetNewValue_Fail() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");

		boolean result = personnageBS.setNewValue(personnageWork, "7", "Attributs#Erudition");
		
		Assert.assertFalse(result);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals(1, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	@Test
	public void testSetNewValue_Success_Options() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		personnageWork.getPersonnage().getProperty("Attributs#Erudition").setOptions(new int[]{1,2,3,4,5});

		boolean result = personnageBS.setNewValue(personnageWork, "4", "Attributs#Erudition");
		
		Assert.assertTrue(result);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals(4, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
		Assert.assertEquals("Attributs#Erudition", personnageWork.getPersonnage().getHistory().get(0).getAbsoluteName());
	}
	
	@Test
	public void testSetNewValue_Fail_Options() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		personnageWork.getPersonnage().getProperty("Attributs#Erudition").setOptions(new int[]{1,3,5});

		boolean result = personnageBS.setNewValue(personnageWork, "4", "Attributs#Erudition");
		
		Assert.assertFalse(result);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals(1, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	@Test
	public void testSetNewValue_Success_IntOffset() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().setOffset(new Integer(2));

		boolean result = personnageBS.setNewValue(personnageWork, "3", "Attributs#Erudition");
		
		Assert.assertTrue(result);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals(3, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
		Assert.assertEquals("Attributs#Erudition", personnageWork.getPersonnage().getHistory().get(0).getAbsoluteName());
	}
	
	@Test
	public void testSetNewValue_Fail_IntOffset() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().setOffset(new Integer(2));

		boolean result = personnageBS.setNewValue(personnageWork, "4", "Attributs#Erudition");
		
		Assert.assertFalse(result);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals(1, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	@Test
	public void testSetNewValue_Success_StringOffset() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		personnageWork.getPersonnage().getProperty("Nom").getValue().setOffset("a");

		boolean result = personnageBS.setNewValue(personnageWork, "aaaa", "Nom");
		
		Assert.assertTrue(result);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals("aaaa", personnageWork.getPersonnage().getProperty("Nom").getValue().getString());
		Assert.assertEquals("Nom", personnageWork.getPersonnage().getHistory().get(0).getAbsoluteName());
	}
	
	@Test
	public void testSetNewValue_Fail_StringOffset() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		personnageWork.getPersonnage().getProperty("Nom").getValue().setOffset("a");

		boolean result = personnageBS.setNewValue(personnageWork, "tata", "Nom");
		
		Assert.assertFalse(result);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals("", personnageWork.getPersonnage().getProperty("Nom").getValue().getString());
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	@Test
	public void testSetNewValue_Success_DoubleOffset() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		personnageWork.getPersonnage().getProperty("Compétences#Cartographie").setValue(new DoubleValue(0.0));
		personnageWork.getPersonnage().getProperty("Compétences#Cartographie").getValue().setOffset(new Double(2.0));
		personnageWork.getPersonnage().getProperty("Compétences#Cartographie").setMax(null);
		personnageWork.getPersonnage().getProperty("Compétences#Cartographie").setMin(null);

		boolean result = personnageBS.setNewValue(personnageWork, "4", "Compétences#Cartographie");
		
		Assert.assertTrue(result);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals(4.0, personnageWork.getPersonnage().getProperty("Compétences#Cartographie").getValue().getDouble(), 0.1);
		Assert.assertEquals("Compétences#Cartographie", personnageWork.getPersonnage().getHistory().get(0).getAbsoluteName());
	}
	
	@Test
	public void testSetNewValue_Fail_DoubletOffset() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		personnageWork.getPersonnage().getProperty("Attributs#Erudition").setValue(new DoubleValue(1.0));
		personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().setOffset(new Double(2.0));

		boolean result = personnageBS.setNewValue(personnageWork, "4", "Attributs#Erudition");
		
		Assert.assertFalse(result);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals(1, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	@Test
	public void testSetNewValue_Success_NullOffset() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		personnageWork.getPersonnage().getProperty("Nom").getValue();

		boolean result = personnageBS.setNewValue(personnageWork, "Loïc", "Nom");
		
		Assert.assertTrue(result);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals("Loïc", personnageWork.getPersonnage().getProperty("Nom").getValue().getString());
		Assert.assertEquals("Nom", personnageWork.getPersonnage().getHistory().get(0).getAbsoluteName());
	}
	
	@Test
	public void testUnvalidatePersonnage() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		personnageWork.setValidPersonnageData(new PersonnageDataBO());
		personnageWork.getValidPersonnageData().setPersonnage(personnageWork.getPersonnage().clone());
		personnageWork.getPersonnage().setNewValue("Attributs#Erudition", 4);
		
		personnageBS.unvalidatePersonnage(personnageWork);
		
		Assert.assertEquals(1, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
	}
	
	@Test
	public void testValidatePersonnage() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		personnageWork.setValidPersonnageData(new PersonnageDataBO());
		personnageWork.getValidPersonnageData().setPersonnage(personnageWork.getPersonnage().clone());
		personnageWork.getPersonnage().setNewValue("Attributs#Erudition", 4);
		
		personnageBS.validatePersonnage(personnageWork);
		
		Assert.assertEquals(4, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
	}
	
	@Test
	public void testAddPropertyFromOption_Success() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		
		boolean result = personnageBS.addPropertyFromOption(personnageWork, "Compétences#Connaissance spécialisée", "Droit", null);
		
		Assert.assertTrue(result);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertNotNull(personnageWork.getPersonnage().getProperty("Compétences#Connaissance spécialisée#Droit"));
		Assert.assertEquals("Compétences#Connaissance spécialisée#Droit", personnageWork.getPersonnage().getHistory().get(0).getAbsoluteName());
		
	}
	
	@Test
	public void testAddPropertyFromOption_Success_Specification() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		
		boolean result = personnageBS.addPropertyFromOption(personnageWork, "Faiblesses", "Ennemi - ", "Barbe noire");
		
		Assert.assertTrue(result);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertNotNull(personnageWork.getPersonnage().getProperty("Faiblesses#Ennemi - Barbe noire"));
		Assert.assertEquals("Faiblesses#Ennemi - Barbe noire", personnageWork.getPersonnage().getHistory().get(0).getAbsoluteName());
	}
	
	@Test
	public void testAddPropertyFromOption_Fail_MotherNull() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		
		boolean result = personnageBS.addPropertyFromOption(personnageWork, "Toto", "Droit", null);
		
		Assert.assertFalse(result);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertNull(personnageWork.getPersonnage().getProperty("Toto#Droit"));
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	@Test
	public void testAddPropertyFromOption_Fail_NoSubList() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		
		boolean result = personnageBS.addPropertyFromOption(personnageWork, "Attributs#Erudition", "Droit", null);
		
		Assert.assertFalse(result);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertNull(personnageWork.getPersonnage().getProperty("Attributs#Eruditio#Droit"));
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	@Test
	public void testAddPropertyFromOption_Fail_BadOption() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		
		boolean result = personnageBS.addPropertyFromOption(personnageWork, "Compétences#Connaissance spécialisée", "Toto", null);
		
		Assert.assertFalse(result);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertNull(personnageWork.getPersonnage().getProperty("Compétences#Connaissance spécialisée#Toto"));
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	@Test
	public void testAddPropertyFromOption_Fail_NullSpecification() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		
		boolean result = personnageBS.addPropertyFromOption(personnageWork, "Faiblesses", "Ennemi - ", null);
		
		Assert.assertFalse(result);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertTrue(personnageWork.getPersonnage().getProperty("Faiblesses").getSubProperties().isEmpty());
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
		
	}
	
	@Test
	public void testAddPropertyFromOption_Fail_EmptySpecification() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		
		boolean result = personnageBS.addPropertyFromOption(personnageWork, "Faiblesses", "Ennemi - ", "");
		
		Assert.assertFalse(result);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertTrue(personnageWork.getPersonnage().getProperty("Faiblesses").getSubProperties().isEmpty());
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
		
	}
	
	@Test
	public void testAddPropertyFromOption_Fail() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");
		
		boolean result = personnageBS.addPropertyFromOption(personnageWork, "Faiblesses", "Ennemi - ", "Barbe#noire");
		
		Assert.assertFalse(result);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertTrue(personnageWork.getPersonnage().getProperty("Faiblesses").getSubProperties().isEmpty());
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
}
