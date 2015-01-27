package com.mrprez.gencross.web.bs;

import java.io.File;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.disk.PersonnageFactory;
import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.history.HistoryItem;
import com.mrprez.gencross.impl.pavillonNoir.PavillonNoir;
import com.mrprez.gencross.value.DoubleValue;
import com.mrprez.gencross.web.bo.PersonnageDataBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.dao.face.IPersonnageDAO;

public class GcrFileBSTest {
	
	@Test
	public void createPersonnageGcrAsPlayer_FailWrongId() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		int personnageWorkId = 1;
		UserBO user = AuthentificationBSTest.buildUser("batman");
		
		byte[] result = gcrFileBS.createPersonnageGcrAsPlayer(personnageWorkId, user);
		
		Assert.assertNull(result);
	}
	
	@Test
	public void createPersonnageGcrAsPlayer_Success() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		int personnageWorkId = 1;
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork();
		personnageWork.setPlayer(user);
		Mockito.when(gcrFileBS.getPersonnageDAO().loadPersonnageWork(personnageWorkId)).thenReturn(personnageWork);
		
		byte[] result = gcrFileBS.createPersonnageGcrAsPlayer(personnageWorkId, user);
		
		Assert.assertNotNull(result);
		Assert.assertNotNull(personnageWork.getPersonnage().getPassword());
	}
	
	@Test
	public void createPersonnageGcrAsPlayer_FailNoPlayer() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork();
		
		byte[] result = gcrFileBS.createPersonnageGcrAsPlayer(personnageWork, user);
		
		Assert.assertNull(result);
	}
	
	@Test
	public void createPersonnageGcrAsPlayer_FailWrongPlayer() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork();
		personnageWork.setPlayer(AuthentificationBSTest.buildUser("robin"));
		
		byte[] result = gcrFileBS.createPersonnageGcrAsPlayer(personnageWork, user);
		
		Assert.assertNull(result);
	}
	
	@Test
	public void createPersonnageGcrAsGameMaster_FailWrongId() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		int personnageWorkId = 1;
		UserBO user = AuthentificationBSTest.buildUser("batman");
		
		byte[] result = gcrFileBS.createPersonnageGcrAsGameMaster(personnageWorkId, user, "azerty");
		
		Assert.assertNull(result);
	}
	
	@Test
	public void createPersonnageGcrAsGameMaster_Success() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		int personnageWorkId = 1;
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork();
		personnageWork.setGameMaster(user);
		Mockito.when(gcrFileBS.getPersonnageDAO().loadPersonnageWork(personnageWorkId)).thenReturn(personnageWork);
		
		byte[] result = gcrFileBS.createPersonnageGcrAsGameMaster(personnageWorkId, user, "azerty");
		
		Assert.assertNotNull(result);
		Assert.assertEquals("azerty", personnageWork.getPersonnage().getPassword());
	}
	
	@Test
	public void createPersonnageGcrAsGameMaster_FailNoPlayer() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork();
		
		byte[] result = gcrFileBS.createPersonnageGcrAsGameMaster(personnageWork, user, "azerty");
		
		Assert.assertNull(result);
	}
	
	@Test
	public void createPersonnageGcrAsGameMaster_FailWrongPlayer() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork();
		personnageWork.setGameMaster(AuthentificationBSTest.buildUser("robin"));
		
		byte[] result = gcrFileBS.createPersonnageGcrAsGameMaster(personnageWork, user, "azerty");
		
		Assert.assertNull(result);
	}
	
	@Test
	public void testCreatePersonnageAsPlayer() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		File gcrFile = new File("");
		String personnageName = "Joker";
		UserBO user = AuthentificationBSTest.buildUser("batman");
		Personnage filePersonnage = new Personnage();
		filePersonnage.setPluginDescriptor(new PluginDescriptor());
		filePersonnage.getPluginDescriptor().setName("DC-Comics");
		Mockito.when(gcrFileBS.getPersonnageFactory().loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		PersonnageWorkBO result = gcrFileBS.createPersonnageAsPlayer(gcrFile, personnageName, user);
		
		Assert.assertEquals(personnageName, result.getName());
		Assert.assertEquals(user, result.getPlayer());
		Assert.assertEquals(filePersonnage, result.getPersonnage());
		Mockito.verify(gcrFileBS.getPersonnageDAO(), Mockito.atLeast(2)).savePersonnage(Mockito.any(PersonnageDataBO.class));
		Mockito.verify(gcrFileBS.getPersonnageDAO(), Mockito.atLeast(1)).savePersonnageWork(Mockito.any(PersonnageWorkBO.class));
	}
	
	@Test
	public void testCreatePersonnageAsGameMaster_Success() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		File gcrFile = new File("");
		String personnageName = "Joker";
		UserBO user = AuthentificationBSTest.buildUser("batman");
		Personnage filePersonnage = new Personnage();
		filePersonnage.setPassword("azerty");
		filePersonnage.setPluginDescriptor(new PluginDescriptor());
		filePersonnage.getPluginDescriptor().setName("DC-Comics");
		Mockito.when(gcrFileBS.getPersonnageFactory().loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		PersonnageWorkBO result = gcrFileBS.createPersonnageAsGameMaster(gcrFile, personnageName, user, "azerty");
		
		Assert.assertEquals(personnageName, result.getName());
		Assert.assertEquals(user, result.getGameMaster());
		Assert.assertEquals(filePersonnage, result.getPersonnage());
		Mockito.verify(gcrFileBS.getPersonnageDAO(), Mockito.atLeast(2)).savePersonnage(Mockito.any(PersonnageDataBO.class));
		Mockito.verify(gcrFileBS.getPersonnageDAO(), Mockito.atLeast(1)).savePersonnageWork(Mockito.any(PersonnageWorkBO.class));
	}
	
	@Test
	public void testCreatePersonnageAsGameMaster_Fail_NoPassword() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		File gcrFile = new File("");
		String personnageName = "Joker";
		UserBO user = AuthentificationBSTest.buildUser("batman");
		Personnage filePersonnage = new Personnage();
		filePersonnage.setPluginDescriptor(new PluginDescriptor());
		filePersonnage.getPluginDescriptor().setName("DC-Comics");
		Mockito.when(gcrFileBS.getPersonnageFactory().loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		PersonnageWorkBO result = gcrFileBS.createPersonnageAsGameMaster(gcrFile, personnageName, user, "azerty");
		
		Assert.assertNull(result);
		Mockito.verify(gcrFileBS.getPersonnageDAO(), Mockito.never()).savePersonnage(Mockito.any(PersonnageDataBO.class));
		Mockito.verify(gcrFileBS.getPersonnageDAO(), Mockito.never()).savePersonnageWork(Mockito.any(PersonnageWorkBO.class));
	}
	
	@Test
	public void testCreatePersonnageAsGameMaster_Fail_WrongPassword() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		File gcrFile = new File("");
		String personnageName = "Joker";
		UserBO user = AuthentificationBSTest.buildUser("batman");
		Personnage filePersonnage = new Personnage();
		filePersonnage.setPassword("qwerty");
		filePersonnage.setPluginDescriptor(new PluginDescriptor());
		filePersonnage.getPluginDescriptor().setName("DC-Comics");
		Mockito.when(gcrFileBS.getPersonnageFactory().loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		PersonnageWorkBO result = gcrFileBS.createPersonnageAsGameMaster(gcrFile, personnageName, user, "azerty");
		
		Assert.assertNull(result);
		Mockito.verify(gcrFileBS.getPersonnageDAO(), Mockito.never()).savePersonnage(Mockito.any(PersonnageDataBO.class));
		Mockito.verify(gcrFileBS.getPersonnageDAO(), Mockito.never()).savePersonnageWork(Mockito.any(PersonnageWorkBO.class));
	}
	
	@Test
	public void testUploadGcrPersonnage_Success_AsGameMaster() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		File gcrFile = new File("");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		personnageWork.setGameMaster(user);
		Personnage filePersonnage = new PavillonNoir();
		filePersonnage.setPassword("azerty");
		Mockito.when(gcrFileBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		Mockito.when(gcrFileBS.getPersonnageFactory().loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		String result = gcrFileBS.uploadGcrPersonnage(gcrFile, 1, user, "azerty");
		
		Assert.assertNull(result);
		Assert.assertEquals(filePersonnage, personnageWork.getPersonnage());
		Mockito.verify(gcrFileBS.getPersonnageDAO()).savePersonnage(Mockito.any(PersonnageDataBO.class));
	}
	
	@Test
	public void testUploadGcrPersonnage_Fail_WrongGameMasterPassword() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		File gcrFile = new File("");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		personnageWork.setGameMaster(user);
		Personnage filePersonnage = new PavillonNoir();
		filePersonnage.setPassword("azerty");
		Mockito.when(gcrFileBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		Mockito.when(gcrFileBS.getPersonnageFactory().loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		String result = gcrFileBS.uploadGcrPersonnage(gcrFile, 1, user, "qwerty");
		
		Assert.assertEquals("Mot de passe incorrect", result);
		Mockito.verify(gcrFileBS.getPersonnageDAO(), Mockito.never()).savePersonnage(Mockito.any(PersonnageDataBO.class));
	}
	
	@Test
	public void testUploadGcrPersonnage_Success_NoGameMasterPassword() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		File gcrFile = new File("");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		personnageWork.setGameMaster(user);
		Personnage filePersonnage = new PavillonNoir();
		Mockito.when(gcrFileBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		Mockito.when(gcrFileBS.getPersonnageFactory().loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		String result = gcrFileBS.uploadGcrPersonnage(gcrFile, 1, user, "azerty");
		
		Assert.assertNull(result);
		Assert.assertEquals(filePersonnage, personnageWork.getPersonnage());
		Mockito.verify(gcrFileBS.getPersonnageDAO()).savePersonnage(Mockito.any(PersonnageDataBO.class));
	}
	
	@Test
	public void testUploadGcrPersonnage_Fail_AsGameMasterWrongPersonnageType() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		File gcrFile = new File("");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		personnageWork.setGameMaster(user);
		Personnage filePersonnage = new Personnage();
		filePersonnage.setPassword("azerty");
		Mockito.when(gcrFileBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		Mockito.when(gcrFileBS.getPersonnageFactory().loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		String result = gcrFileBS.uploadGcrPersonnage(gcrFile, 1, user, "azerty");
		
		Assert.assertEquals("Ce fichier n'est pas un personnage PavillonNoir", result);
		Mockito.verify(gcrFileBS.getPersonnageDAO(), Mockito.never()).savePersonnage(Mockito.any(PersonnageDataBO.class));
	}
	
	@Test
	public void testUploadGcrPersonnage_Success_AsPlayer() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		File gcrFile = new File("");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = buildPersonnageWorkWithHistory();
		personnageWork.setPlayer(user);
		personnageWork.setValidationDate(new Date());
		Personnage filePersonnage = personnageWork.getPersonnage().clone();
		Thread.sleep(1000);
		addHistoryItem(filePersonnage);
		addHistoryItem(personnageWork.getPersonnage());
		Mockito.when(gcrFileBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		Mockito.when(gcrFileBS.getPersonnageFactory().loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		String result = gcrFileBS.uploadGcrPersonnage(gcrFile, 1, user, null);
		
		Assert.assertNull(result);
		Assert.assertEquals(filePersonnage, personnageWork.getPersonnage());
		Mockito.verify(gcrFileBS.getPersonnageDAO()).savePersonnage(Mockito.any(PersonnageDataBO.class));
	}
	
	@Test
	public void testUploadGcrPersonnage_Success_AsPlayerWithoutValidation() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		File gcrFile = new File("");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = buildPersonnageWorkWithHistory();
		personnageWork.setPlayer(user);
		Thread.sleep(1000);
		Personnage filePersonnage = buildPersonnageWorkWithHistory().getPersonnage().clone();
		addHistoryItem(filePersonnage);
		Mockito.when(gcrFileBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		Mockito.when(gcrFileBS.getPersonnageFactory().loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		String result = gcrFileBS.uploadGcrPersonnage(gcrFile, 1, user, null);
		
		Assert.assertNull(result);
		Assert.assertEquals(filePersonnage, personnageWork.getPersonnage());
		Mockito.verify(gcrFileBS.getPersonnageDAO()).savePersonnage(Mockito.any(PersonnageDataBO.class));
	}
	
	@Test
	public void testUploadGcrPersonnage_Fail_AsPlayerWrongPersonnageType() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		File gcrFile = new File("");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = buildPersonnageWorkWithHistory();
		personnageWork.setPlayer(user);
		Personnage filePersonnage = new Personnage();
		Mockito.when(gcrFileBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		Mockito.when(gcrFileBS.getPersonnageFactory().loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		String result = gcrFileBS.uploadGcrPersonnage(gcrFile, 1, user, null);
		
		Assert.assertEquals("Ce fichier n'est pas un personnage PavillonNoir", result);
		Mockito.verify(gcrFileBS.getPersonnageDAO(), Mockito.never()).savePersonnage(Mockito.any(PersonnageDataBO.class));
	}
	
	@Test
	public void testUploadGcrPersonnage_Fail_AsPlayerHistoryNotMatch() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		File gcrFile = new File("");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = buildPersonnageWorkWithHistory();
		personnageWork.setName("Joker");
		personnageWork.setPlayer(user);
		Personnage filePersonnage = personnageWork.getPersonnage().clone();
		addHistoryItem(filePersonnage);
		Thread.sleep(1000);
		addHistoryItem(personnageWork.getPersonnage());
		personnageWork.setValidationDate(new Date());
		Mockito.when(gcrFileBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		Mockito.when(gcrFileBS.getPersonnageFactory().loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		String result = gcrFileBS.uploadGcrPersonnage(gcrFile, 1, user, null);
		
		Assert.assertEquals("Il ne s'agit pas du personnage: Joker", result);
		Mockito.verify(gcrFileBS.getPersonnageDAO(), Mockito.never()).savePersonnage(Mockito.any(PersonnageDataBO.class));
	}
	
	@Test
	public void testUploadGcrPersonnage_Fail_AsPlayerBeforeValidation() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		File gcrFile = new File("");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = buildPersonnageWorkWithHistory();
		personnageWork.setName("Joker");
		personnageWork.setPlayer(user);
		Personnage filePersonnage = personnageWork.getPersonnage().clone();
		addHistoryItem(personnageWork.getPersonnage());
		Thread.sleep(1000);
		personnageWork.setValidationDate(new Date());
		Mockito.when(gcrFileBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		Mockito.when(gcrFileBS.getPersonnageFactory().loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		String result = gcrFileBS.uploadGcrPersonnage(gcrFile, 1, user, null);
		
		Assert.assertEquals("Ce fichier date d'avant la dernière validation du MJ, vous ne pouvez pas le charger en tant que joueur.", result);
		Mockito.verify(gcrFileBS.getPersonnageDAO(), Mockito.never()).savePersonnage(Mockito.any(PersonnageDataBO.class));
	}
	
	@Test
	public void testUploadGcrPersonnage_Fail_NotPersoOwner() throws Exception{
		GcrFileBS gcrFileBS = buildGcrFileBS();
		File gcrFile = new File("");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = buildPersonnageWorkWithHistory();
		Mockito.when(gcrFileBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork);
		
		String result = gcrFileBS.uploadGcrPersonnage(gcrFile, 1, user, null);
		
		Assert.assertEquals("Ce personnage ne vous appartient pas", result);
		Mockito.verify(gcrFileBS.getPersonnageDAO(), Mockito.never()).savePersonnage(Mockito.any(PersonnageDataBO.class));
	}
	
	
	
	private GcrFileBS buildGcrFileBS(){
		GcrFileBS gcrFileBS = new GcrFileBS();
		gcrFileBS.setPersonnageDAO(Mockito.mock(IPersonnageDAO.class));
		gcrFileBS.setPersonnageFactory(Mockito.mock(PersonnageFactory.class));
		
		return gcrFileBS;
	}
	
	private PersonnageWorkBO buildPersonnageWorkWithHistory() throws Exception{
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork();
		
		for(int i=0; i<3; i++){
			addHistoryItem(personnageWork.getPersonnage());
		}
		
		return personnageWork;
	}
	
	private void addHistoryItem(Personnage personnage){
		HistoryItem historyItem = new HistoryItem();
		historyItem.setAbsoluteName("Property_"+personnage.getHistory().size());
		historyItem.setAction((int) (Math.random()*3+1));
		historyItem.setCost((int) (Math.random()*10));
		historyItem.setNewValue(new DoubleValue(Math.random()*10));
		historyItem.setOldValue(new DoubleValue(Math.random()*10));
		historyItem.setPhase("Start");
		historyItem.setPointPool("XP");
		
		personnage.getHistory().add(historyItem);
	}

}
