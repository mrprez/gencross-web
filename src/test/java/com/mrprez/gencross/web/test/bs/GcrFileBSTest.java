package com.mrprez.gencross.web.test.bs;

import java.io.File;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.disk.PersonnageFactory;
import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.history.HistoryItem;
import com.mrprez.gencross.impl.pavillonNoir.PavillonNoir;
import com.mrprez.gencross.value.DoubleValue;
import com.mrprez.gencross.web.bo.PersonnageDataBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.GcrFileBS;
import com.mrprez.gencross.web.dao.face.IPersonnageDAO;

@RunWith(MockitoJUnitRunner.class)
public class GcrFileBSTest {
	
	@Mock
	private IPersonnageDAO personnageDao;
	@Mock
	private PersonnageFactory personnageFactory;
	
	@InjectMocks
	private GcrFileBS gcrFileBS = new GcrFileBS();
	
	
	@Test
	public void createPersonnageGcrAsPlayer_FailWrongId() throws Exception{
		// Prepare
		int personnageWorkId = 1;
		UserBO user = AuthentificationBSTest.buildUser("batman");
		
		// Execute
		byte[] result = gcrFileBS.createPersonnageGcrAsPlayer(personnageWorkId, user);
		
		// Check
		Assert.assertNull(result);
	}
	
	@Test
	public void createPersonnageGcrAsPlayer_Success() throws Exception{
		// Prepare
		int personnageWorkId = 1;
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork();
		personnageWork.setPlayer(user);
		Mockito.when(personnageDao.loadPersonnageWork(personnageWorkId)).thenReturn(personnageWork);
		
		// Execute
		byte[] result = gcrFileBS.createPersonnageGcrAsPlayer(personnageWorkId, user);
		
		// Check
		Assert.assertNotNull(result);
		Assert.assertNotNull(personnageWork.getPersonnage().getPassword());
	}
	
	@Test
	public void createPersonnageGcrAsPlayer_FailNoPlayer() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork();
		
		// Execute
		byte[] result = gcrFileBS.createPersonnageGcrAsPlayer(personnageWork, user);
		
		// Check
		Assert.assertNull(result);
	}
	
	@Test
	public void createPersonnageGcrAsPlayer_FailWrongPlayer() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork();
		personnageWork.setPlayer(AuthentificationBSTest.buildUser("robin"));
		
		// Execute
		byte[] result = gcrFileBS.createPersonnageGcrAsPlayer(personnageWork, user);
		
		// Check
		Assert.assertNull(result);
	}
	
	@Test
	public void createPersonnageGcrAsGameMaster_FailWrongId() throws Exception{
		// Prepare
		int personnageWorkId = 1;
		UserBO user = AuthentificationBSTest.buildUser("batman");
		
		// Execute
		byte[] result = gcrFileBS.createPersonnageGcrAsGameMaster(personnageWorkId, user, "azerty");
		
		// Check
		Assert.assertNull(result);
	}
	
	@Test
	public void createPersonnageGcrAsGameMaster_Success() throws Exception{
		// Prepare
		int personnageWorkId = 1;
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork();
		personnageWork.setGameMaster(user);
		Mockito.when(personnageDao.loadPersonnageWork(personnageWorkId)).thenReturn(personnageWork);
		
		// Execute
		byte[] result = gcrFileBS.createPersonnageGcrAsGameMaster(personnageWorkId, user, "azerty");
		
		// Check
		Assert.assertNotNull(result);
		Assert.assertEquals("azerty", personnageWork.getPersonnage().getPassword());
	}
	
	@Test
	public void createPersonnageGcrAsGameMaster_FailNoPlayer() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork();
		
		// Execute
		byte[] result = gcrFileBS.createPersonnageGcrAsGameMaster(personnageWork, user, "azerty");
		
		// Check
		Assert.assertNull(result);
	}
	
	@Test
	public void createPersonnageGcrAsGameMaster_FailWrongPlayer() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork();
		personnageWork.setGameMaster(AuthentificationBSTest.buildUser("robin"));
		
		// Execute
		byte[] result = gcrFileBS.createPersonnageGcrAsGameMaster(personnageWork, user, "azerty");
		
		// Check
		Assert.assertNull(result);
	}
	
	@Test
	public void testCreatePersonnageAsPlayer() throws Exception{
		// Prepare
		File gcrFile = new File("");
		String personnageName = "Joker";
		UserBO user = AuthentificationBSTest.buildUser("batman");
		Personnage filePersonnage = new Personnage();
		filePersonnage.setPluginDescriptor(new PluginDescriptor());
		filePersonnage.getPluginDescriptor().setName("DC-Comics");
		Mockito.when(personnageFactory.loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		// Execute
		PersonnageWorkBO result = gcrFileBS.createPersonnageAsPlayer(gcrFile, personnageName, user);
		
		// Check
		Assert.assertEquals(personnageName, result.getName());
		Assert.assertEquals(user, result.getPlayer());
		Assert.assertEquals(filePersonnage, result.getPersonnage());
		Mockito.verify(personnageDao, Mockito.atLeast(2)).savePersonnage(Mockito.any(PersonnageDataBO.class));
		Mockito.verify(personnageDao, Mockito.atLeast(1)).savePersonnageWork(Mockito.any(PersonnageWorkBO.class));
	}
	
	@Test
	public void testCreatePersonnageAsGameMaster_Success() throws Exception{
		// Prepare
		File gcrFile = new File("");
		String personnageName = "Joker";
		UserBO user = AuthentificationBSTest.buildUser("batman");
		Personnage filePersonnage = new Personnage();
		filePersonnage.setPassword("azerty");
		filePersonnage.setPluginDescriptor(new PluginDescriptor());
		filePersonnage.getPluginDescriptor().setName("DC-Comics");
		Mockito.when(personnageFactory.loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		// Execute
		PersonnageWorkBO result = gcrFileBS.createPersonnageAsGameMaster(gcrFile, personnageName, user, "azerty");
		
		// Check
		Assert.assertEquals(personnageName, result.getName());
		Assert.assertEquals(user, result.getGameMaster());
		Assert.assertEquals(filePersonnage, result.getPersonnage());
		Mockito.verify(personnageDao, Mockito.atLeast(2)).savePersonnage(Mockito.any(PersonnageDataBO.class));
		Mockito.verify(personnageDao, Mockito.atLeast(1)).savePersonnageWork(Mockito.any(PersonnageWorkBO.class));
	}
	
	@Test
	public void testCreatePersonnageAsGameMaster_Fail_NoPassword() throws Exception{
		// Prepare
		File gcrFile = new File("");
		String personnageName = "Joker";
		UserBO user = AuthentificationBSTest.buildUser("batman");
		Personnage filePersonnage = new Personnage();
		filePersonnage.setPluginDescriptor(new PluginDescriptor());
		filePersonnage.getPluginDescriptor().setName("DC-Comics");
		Mockito.when(personnageFactory.loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		// Execute
		PersonnageWorkBO result = gcrFileBS.createPersonnageAsGameMaster(gcrFile, personnageName, user, "azerty");
		
		// Check
		Assert.assertNull(result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(Mockito.any(PersonnageDataBO.class));
		Mockito.verify(personnageDao, Mockito.never()).savePersonnageWork(Mockito.any(PersonnageWorkBO.class));
	}
	
	@Test
	public void testCreatePersonnageAsGameMaster_Fail_WrongPassword() throws Exception{
		// Prepare
		File gcrFile = new File("");
		String personnageName = "Joker";
		UserBO user = AuthentificationBSTest.buildUser("batman");
		Personnage filePersonnage = new Personnage();
		filePersonnage.setPassword("qwerty");
		filePersonnage.setPluginDescriptor(new PluginDescriptor());
		filePersonnage.getPluginDescriptor().setName("DC-Comics");
		Mockito.when(personnageFactory.loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		// Execute
		PersonnageWorkBO result = gcrFileBS.createPersonnageAsGameMaster(gcrFile, personnageName, user, "azerty");
		
		// Check
		Assert.assertNull(result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(Mockito.any(PersonnageDataBO.class));
		Mockito.verify(personnageDao, Mockito.never()).savePersonnageWork(Mockito.any(PersonnageWorkBO.class));
	}
	
	@Test
	public void testUploadGcrPersonnage_Success_AsGameMaster() throws Exception{
		// Prepare
		File gcrFile = new File("");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		personnageWork.setGameMaster(user);
		Personnage filePersonnage = new PavillonNoir();
		filePersonnage.setPassword("azerty");
		filePersonnage.setPluginDescriptor(new PluginDescriptor());
		filePersonnage.getPluginDescriptor().setName("Pavillon Noir");
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		Mockito.when(personnageFactory.loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		// Execute
		String result = gcrFileBS.uploadGcrPersonnage(gcrFile, 1, user, "azerty");
		
		// Check
		Assert.assertNull(result);
		Assert.assertEquals(filePersonnage, personnageWork.getPersonnage());
		Mockito.verify(personnageDao).savePersonnage(Mockito.any(PersonnageDataBO.class));
	}
	
	@Test
	public void testUploadGcrPersonnage_Fail_WrongGameMasterPassword() throws Exception{
		// Prepare
		File gcrFile = new File("");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		personnageWork.setGameMaster(user);
		Personnage filePersonnage = new PavillonNoir();
		filePersonnage.setPassword("azerty");
		filePersonnage.setPluginDescriptor(new PluginDescriptor());
		filePersonnage.getPluginDescriptor().setName("Pavillon Noir");
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		Mockito.when(personnageFactory.loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		// Execute
		String result = gcrFileBS.uploadGcrPersonnage(gcrFile, 1, user, "qwerty");
		
		// Check
		Assert.assertEquals("Mot de passe incorrect", result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(Mockito.any(PersonnageDataBO.class));
	}
	
	@Test
	public void testUploadGcrPersonnage_Success_NoGameMasterPassword() throws Exception{
		// Prepare
		File gcrFile = new File("");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		personnageWork.setGameMaster(user);
		Personnage filePersonnage = new PavillonNoir();
		filePersonnage.setPluginDescriptor(new PluginDescriptor());
		filePersonnage.getPluginDescriptor().setName("Pavillon Noir");
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		Mockito.when(personnageFactory.loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		// Execute
		String result = gcrFileBS.uploadGcrPersonnage(gcrFile, 1, user, "azerty");
		
		// Check
		Assert.assertNull(result);
		Assert.assertEquals(filePersonnage, personnageWork.getPersonnage());
		Mockito.verify(personnageDao).savePersonnage(Mockito.any(PersonnageDataBO.class));
	}
	
	@Test
	public void testUploadGcrPersonnage_Fail_AsGameMasterWrongPersonnageType() throws Exception{
		// Prepare
		File gcrFile = new File("");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		personnageWork.setGameMaster(user);
		Personnage filePersonnage = new Personnage();
		filePersonnage.setPassword("azerty");
		filePersonnage.setPluginDescriptor(new PluginDescriptor());
		filePersonnage.getPluginDescriptor().setName("Vampire");
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		Mockito.when(personnageFactory.loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		// Execute
		String result = gcrFileBS.uploadGcrPersonnage(gcrFile, 1, user, "azerty");
		
		// Check
		Assert.assertEquals("Ce fichier n'est pas un personnage Pavillon Noir", result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(Mockito.any(PersonnageDataBO.class));
	}
	
	@Test
	public void testUploadGcrPersonnage_Success_AsPlayer() throws Exception{
		// Prepare
		File gcrFile = new File("");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = buildPersonnageWorkWithHistory();
		personnageWork.setPlayer(user);
		personnageWork.setValidationDate(new Date());
		Personnage filePersonnage = personnageWork.getPersonnage().clone();
		Thread.sleep(1000);
		addHistoryItem(filePersonnage);
		addHistoryItem(personnageWork.getPersonnage());
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		Mockito.when(personnageFactory.loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		// Execute
		String result = gcrFileBS.uploadGcrPersonnage(gcrFile, 1, user, null);
		
		// Check
		Assert.assertNull(result);
		Assert.assertEquals(filePersonnage, personnageWork.getPersonnage());
		Mockito.verify(personnageDao).savePersonnage(Mockito.any(PersonnageDataBO.class));
	}
	
	@Test
	public void testUploadGcrPersonnage_Success_AsPlayerWithoutValidation() throws Exception{
		// Prepare
		File gcrFile = new File("");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = buildPersonnageWorkWithHistory();
		personnageWork.setPlayer(user);
		Thread.sleep(1000);
		Personnage filePersonnage = buildPersonnageWorkWithHistory().getPersonnage().clone();
		addHistoryItem(filePersonnage);
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		Mockito.when(personnageFactory.loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		// Execute
		String result = gcrFileBS.uploadGcrPersonnage(gcrFile, 1, user, null);
		
		// Check
		Assert.assertNull(result);
		Assert.assertEquals(filePersonnage, personnageWork.getPersonnage());
		Mockito.verify(personnageDao).savePersonnage(Mockito.any(PersonnageDataBO.class));
	}
	
	@Test
	public void testUploadGcrPersonnage_Fail_AsPlayerWrongPersonnageType() throws Exception{
		// Prepare
		File gcrFile = new File("");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		personnageWork.setPlayer(user);
		Personnage filePersonnage = new Personnage();
		filePersonnage.setPluginDescriptor(new PluginDescriptor());
		filePersonnage.getPluginDescriptor().setName("Vampire");
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		Mockito.when(personnageFactory.loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		// Execute
		String result = gcrFileBS.uploadGcrPersonnage(gcrFile, 1, user, null);
		
		// Check
		Assert.assertEquals("Ce fichier n'est pas un personnage Pavillon Noir", result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(Mockito.any(PersonnageDataBO.class));
	}
	
	@Test
	public void testUploadGcrPersonnage_Fail_AsPlayerHistoryNotMatch() throws Exception{
		// Prepare
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
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		Mockito.when(personnageFactory.loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		// Execute
		String result = gcrFileBS.uploadGcrPersonnage(gcrFile, 1, user, null);
		
		// Check
		Assert.assertEquals("Il ne s'agit pas du personnage: Joker", result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(Mockito.any(PersonnageDataBO.class));
	}
	
	@Test
	public void testUploadGcrPersonnage_Fail_AsPlayerBeforeValidation() throws Exception{
		// Prepare
		File gcrFile = new File("");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = buildPersonnageWorkWithHistory();
		personnageWork.setName("Joker");
		personnageWork.setPlayer(user);
		Personnage filePersonnage = personnageWork.getPersonnage().clone();
		addHistoryItem(personnageWork.getPersonnage());
		Thread.sleep(1000);
		personnageWork.setValidationDate(new Date());
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		Mockito.when(personnageFactory.loadPersonnageFromGcr(gcrFile)).thenReturn(filePersonnage);
		
		// Execute
		String result = gcrFileBS.uploadGcrPersonnage(gcrFile, 1, user, null);
		
		// Check
		Assert.assertEquals("Ce fichier date d'avant la dernière validation du MJ, vous ne pouvez pas le charger en tant que joueur.", result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(Mockito.any(PersonnageDataBO.class));
	}
	
	@Test
	public void testUploadGcrPersonnage_Fail_NotPersoOwner() throws Exception{
		// Prepare
		File gcrFile = new File("");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		PersonnageWorkBO personnageWork = buildPersonnageWorkWithHistory();
		Mockito.when(personnageDao.loadPersonnageWork(1)).thenReturn(personnageWork);
		
		// Execute
		String result = gcrFileBS.uploadGcrPersonnage(gcrFile, 1, user, null);
		
		// Check
		Assert.assertEquals("Ce personnage ne vous appartient pas", result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(Mockito.any(PersonnageDataBO.class));
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
