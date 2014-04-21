package com.mrprez.gencross.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.Version;
import com.mrprez.gencross.disk.PersonnageFactory;
import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.history.HistoryItem;
import com.mrprez.gencross.web.bo.PersonnageDataBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.PersonnageBS;
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
		personnageWork.setGameMaster(user);
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		
		personnageBS.deletePersonnageFromUser(1, user);
		Mockito.verify(personnageBS.getPersonnageDAO()).deletePersonnage(personnageWork);
	}
	
	@Test
	public void testDeletePersonnageFromUser_WithOtherUser() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		
		UserBO user = AuthentificationBSTest.buildUser("mrprez");
		
		UserBO otherUser = AuthentificationBSTest.buildUser("azerty");
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(user);
		personnageWork.setPlayer(otherUser);
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		
		personnageBS.deletePersonnageFromUser(1, user);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.never()).deletePersonnage(personnageWork);
		Assert.assertEquals(personnageWork.getPlayer(), otherUser);
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
		
		PersonnageWorkBO personnageWork;
		PersonnageWorkBO result;
		
		personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		result = personnageBS.loadPersonnageAsGameMaster(1, AuthentificationBSTest.buildUser("mrprez"));
		Assert.assertNull(result);
		
		personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		result = personnageBS.loadPersonnageAsGameMaster(1, AuthentificationBSTest.buildUser("mrprez"));
		Assert.assertEquals(personnageWork, result);
	}
	
	@Test
	public void getLoadPersonnageAsPlayer() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		
		PersonnageWorkBO personnageWork;
		PersonnageWorkBO result;
		
		personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		result = personnageBS.loadPersonnageAsPlayer(1, AuthentificationBSTest.buildUser("mrprez"));
		Assert.assertEquals(personnageWork, result);
		
		personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(AuthentificationBSTest.buildUser("mrprez"));
		Mockito.when(personnageBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		result = personnageBS.loadPersonnageAsPlayer(1, AuthentificationBSTest.buildUser("mrprez"));
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
	public void testSetNewValue() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		PersonnageWorkBO personnageWork = buildPersonnageWork("Pavillon Noir");

		boolean result = personnageBS.setNewValue(personnageWork, "4", "Attributs#Erudition");
		
		Assert.assertTrue(result);
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals(4, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
		Assert.assertEquals("Attributs#Erudition", personnageWork.getPersonnage().getHistory().get(0).getAbsoluteName());
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
	
}
