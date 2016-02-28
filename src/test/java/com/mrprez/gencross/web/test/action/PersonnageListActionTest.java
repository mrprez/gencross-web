package com.mrprez.gencross.web.test.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.PersonnageListAction;
import com.mrprez.gencross.web.action.util.PersonnageNameComparator;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IGcrFileBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.mrprez.gencross.web.test.bs.PersonnageWorkBSTest;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class PersonnageListActionTest extends AbstractActionTest {

	@Mock
	private IPersonnageBS personnageBS;

	@Mock
	private IGcrFileBS gcrFileBS;

	@InjectMocks
	private PersonnageListAction personnageListAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		List<PersonnageWorkBO> playerPersonnageList = new ArrayList<PersonnageWorkBO>();
		playerPersonnageList.add(PersonnageWorkBSTest.buildPersonnageWorkMock(1, "B", user, null));
		playerPersonnageList.add(PersonnageWorkBSTest.buildPersonnageWorkMock(2, "A", user, null));
		playerPersonnageList.add(PersonnageWorkBSTest.buildPersonnageWorkMock(3, "C", user, null));
		List<PersonnageWorkBO> gameMasterPersonnageList = new ArrayList<PersonnageWorkBO>();
		gameMasterPersonnageList.add(PersonnageWorkBSTest.buildPersonnageWorkMock(1, "G", user, null));
		gameMasterPersonnageList.add(PersonnageWorkBSTest.buildPersonnageWorkMock(2, "F", user, null));
		gameMasterPersonnageList.add(PersonnageWorkBSTest.buildPersonnageWorkMock(3, "D", user, null));
		ActionContext.getContext().getSession().put("user", user);
		Mockito.when(personnageBS.getPlayerPersonnageList(user)).thenReturn(playerPersonnageList);
		Mockito.when(personnageBS.getGameMasterPersonnageList(user)).thenReturn(gameMasterPersonnageList);
		
		// Execute
		String result = personnageListAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals(3, personnageListAction.getPlayerPersonnageList().size());
		Assert.assertEquals("A", personnageListAction.getPlayerPersonnageList().get(0).getName());
		Assert.assertEquals("B", personnageListAction.getPlayerPersonnageList().get(1).getName());
		Assert.assertEquals("C", personnageListAction.getPlayerPersonnageList().get(2).getName());
		Assert.assertEquals(3, personnageListAction.getGameMasterPersonnageList().size());
		Assert.assertEquals("D", personnageListAction.getGameMasterPersonnageList().get(0).getName());
		Assert.assertEquals("F", personnageListAction.getGameMasterPersonnageList().get(1).getName());
		Assert.assertEquals("G", personnageListAction.getGameMasterPersonnageList().get(2).getName());

	}


	@Test
	public void testDeletePersonnage() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		Integer personnageId = 2;
		ActionContext.getContext().getSession().put("user", user);
		
		// Execute
		personnageListAction.setPersonnageId(personnageId);
		String result = personnageListAction.deletePersonnage();

		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(personnageBS).deletePersonnageFromUser(personnageId, user);
	}


	@Test
	public void testDownloadAsPlayer_Fail() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		Integer personnageId = 2;
		ActionContext.getContext().getSession().put("user", user);
		
		// Execute
		personnageListAction.setPersonnageId(personnageId);
		String result = personnageListAction.downloadAsPlayer();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(personnageListAction.getActionErrors().contains("Impossible de trouver ce personnage"));
		Assert.assertNull(personnageListAction.getFileSize());
		Assert.assertNull(personnageListAction.getInputStream());
		Assert.assertNull(personnageListAction.getFileName());
	}
	
	
	@Test
	public void testDownloadAsPlayer_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		Integer personnageId = 2;
		byte[] byteArray = new byte[]{0,1,2,3,4,5,6,7,8,9};
		PersonnageWorkBO personnageWork =  PersonnageWorkBSTest.buildPersonnageWorkMock(1, "Green Lantern", user, null);
		ActionContext.getContext().getSession().put("user", user);
		Mockito.when(gcrFileBS.createPersonnageGcrAsPlayer(personnageId, user)).thenReturn(byteArray);
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		personnageListAction.setPersonnageId(personnageId);
		String result = personnageListAction.downloadAsPlayer();

		// Check
		Assert.assertEquals("download", result);
		Assert.assertEquals(10, personnageListAction.getFileSize().intValue());
		Assert.assertNotNull(personnageListAction.getInputStream());
		Assert.assertEquals("Green_Lantern.gcr", personnageListAction.getFileName());
	}


	@Test
	public void testDownloadAsGameMaster_Fail() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		Integer personnageId = 2;
		ActionContext.getContext().getSession().put("user", user);
		
		// Execute
		personnageListAction.setPersonnageId(personnageId);
		String result = personnageListAction.downloadAsGameMaster();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(personnageListAction.getActionErrors().contains("Impossible de trouver ce personnage"));
		Assert.assertNull(personnageListAction.getFileSize());
		Assert.assertNull(personnageListAction.getInputStream());
		Assert.assertNull(personnageListAction.getFileName());
	}
	
	
	@Test
	public void testDownloadAsGameMaster_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		Integer personnageId = 2;
		String password = "azerty";
		byte[] byteArray = new byte[]{0,1,2,3,4,5,6,7,8,9};
		PersonnageWorkBO personnageWork =  PersonnageWorkBSTest.buildPersonnageWorkMock(1, "Green Lantern", null, user);
		ActionContext.getContext().getSession().put("user", user);
		Mockito.when(gcrFileBS.createPersonnageGcrAsGameMaster(personnageId, user, password)).thenReturn(byteArray);
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		personnageListAction.setPersonnageId(personnageId);
		personnageListAction.setPassword(password);
		String result = personnageListAction.downloadAsGameMaster();

		// Check
		Assert.assertEquals("download", result);
		Assert.assertEquals(10, personnageListAction.getFileSize().intValue());
		Assert.assertNotNull(personnageListAction.getInputStream());
		Assert.assertEquals("Green_Lantern.gcr", personnageListAction.getFileName());
	}
	
	
	@Test
	public void testSort_NoSort() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		List<PersonnageWorkBO> playerPersonnageList = new ArrayList<PersonnageWorkBO>();
		playerPersonnageList.add(PersonnageWorkBSTest.buildPersonnageWorkMock(1, "B", user, null));
		playerPersonnageList.add(PersonnageWorkBSTest.buildPersonnageWorkMock(2, "A", user, null));
		playerPersonnageList.add(PersonnageWorkBSTest.buildPersonnageWorkMock(3, "C", user, null));
		List<PersonnageWorkBO> gameMasterPersonnageList = new ArrayList<PersonnageWorkBO>();
		gameMasterPersonnageList.add(PersonnageWorkBSTest.buildPersonnageWorkMock(1, "G", user, null));
		gameMasterPersonnageList.add(PersonnageWorkBSTest.buildPersonnageWorkMock(2, "F", user, null));
		gameMasterPersonnageList.add(PersonnageWorkBSTest.buildPersonnageWorkMock(3, "D", user, null));
		ActionContext.getContext().getSession().put("user", user);
		Mockito.when(personnageBS.getPlayerPersonnageList(user)).thenReturn(playerPersonnageList);
		Mockito.when(personnageBS.getGameMasterPersonnageList(user)).thenReturn(gameMasterPersonnageList);
		
		// Execute
		String result = personnageListAction.sort();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals(3, personnageListAction.getPlayerPersonnageList().size());
		Assert.assertEquals("A", personnageListAction.getPlayerPersonnageList().get(0).getName());
		Assert.assertEquals("B", personnageListAction.getPlayerPersonnageList().get(1).getName());
		Assert.assertEquals("C", personnageListAction.getPlayerPersonnageList().get(2).getName());
		Assert.assertEquals(3, personnageListAction.getGameMasterPersonnageList().size());
		Assert.assertEquals("D", personnageListAction.getGameMasterPersonnageList().get(0).getName());
		Assert.assertEquals("F", personnageListAction.getGameMasterPersonnageList().get(1).getName());
		Assert.assertEquals("G", personnageListAction.getGameMasterPersonnageList().get(2).getName());
	}
	

	@Test
	public void testSort() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		List<PersonnageWorkBO> playerPersonnageList = new ArrayList<PersonnageWorkBO>();
		PersonnageWorkBO personnageWork1 = PersonnageWorkBSTest.buildPersonnageWorkMock(1, "B", user, null);
		personnageWork1.setPluginName("Type3");
		PersonnageWorkBO personnageWork2 = PersonnageWorkBSTest.buildPersonnageWorkMock(2, "A", user, null);
		personnageWork2.setPluginName("Type1");
		PersonnageWorkBO personnageWork3 = PersonnageWorkBSTest.buildPersonnageWorkMock(3, "C", user, null);
		personnageWork3.setPluginName("Type2");
		playerPersonnageList.addAll(Arrays.asList(personnageWork1, personnageWork2, personnageWork3));
		List<PersonnageWorkBO> gameMasterPersonnageList = new ArrayList<PersonnageWorkBO>();
		gameMasterPersonnageList.add(PersonnageWorkBSTest.buildPersonnageWorkMock(4, "G", AuthentificationBSTest.buildUser("X"), user));
		gameMasterPersonnageList.add(PersonnageWorkBSTest.buildPersonnageWorkMock(5, "F", null, user));
		gameMasterPersonnageList.add(PersonnageWorkBSTest.buildPersonnageWorkMock(6, "D", AuthentificationBSTest.buildUser("Y"), user));
		ActionContext.getContext().getSession().put("user", user);
		Mockito.when(personnageBS.getPlayerPersonnageList(user)).thenReturn(playerPersonnageList);
		Mockito.when(personnageBS.getGameMasterPersonnageList(user)).thenReturn(gameMasterPersonnageList);
		
		// Execute
		personnageListAction.setPlayerPersonnageSort("type");
		personnageListAction.setGameMasterPersonnageSort("player");
		String result = personnageListAction.sort();
		
		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("Type1", personnageListAction.getPlayerPersonnageList().get(0).getPluginName());
		Assert.assertEquals("Type2", personnageListAction.getPlayerPersonnageList().get(1).getPluginName());
		Assert.assertEquals("Type3", personnageListAction.getPlayerPersonnageList().get(2).getPluginName());
		Assert.assertEquals("X", personnageListAction.getGameMasterPersonnageList().get(0).getPlayer().getUsername());
		Assert.assertEquals("Y", personnageListAction.getGameMasterPersonnageList().get(1).getPlayer().getUsername());
		Assert.assertNull(personnageListAction.getGameMasterPersonnageList().get(2).getPlayer());
	}
	
	
	@Test
	public void testSort_Inverse() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		List<PersonnageWorkBO> playerPersonnageList = new ArrayList<PersonnageWorkBO>();
		PersonnageWorkBO personnageWork1 = PersonnageWorkBSTest.buildPersonnageWorkMock(1, "B", user, null);
		personnageWork1.setPluginName("Type3");
		PersonnageWorkBO personnageWork2 = PersonnageWorkBSTest.buildPersonnageWorkMock(2, "A", user, null);
		personnageWork2.setPluginName("Type1");
		PersonnageWorkBO personnageWork3 = PersonnageWorkBSTest.buildPersonnageWorkMock(3, "C", user, null);
		personnageWork3.setPluginName("Type2");
		playerPersonnageList.addAll(Arrays.asList(personnageWork1, personnageWork2, personnageWork3));
		List<PersonnageWorkBO> gameMasterPersonnageList = new ArrayList<PersonnageWorkBO>();
		gameMasterPersonnageList.add(PersonnageWorkBSTest.buildPersonnageWorkMock(4, "G", AuthentificationBSTest.buildUser("X"), user));
		gameMasterPersonnageList.add(PersonnageWorkBSTest.buildPersonnageWorkMock(5, "F", null, user));
		gameMasterPersonnageList.add(PersonnageWorkBSTest.buildPersonnageWorkMock(6, "D", AuthentificationBSTest.buildUser("Y"), user));
		ActionContext.getContext().getSession().put("user", user);
		ActionContext.getContext().getSession().put("gameMasterPersonnageSort", "player");
		Mockito.when(personnageBS.getPlayerPersonnageList(user)).thenReturn(playerPersonnageList);
		Mockito.when(personnageBS.getGameMasterPersonnageList(user)).thenReturn(gameMasterPersonnageList);
		
		// Execute
		personnageListAction.setPlayerPersonnageSort("type");
		personnageListAction.setGameMasterPersonnageSort("player");
		personnageListAction.sort();
		String result = personnageListAction.sort();
		
		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("Type3", personnageListAction.getPlayerPersonnageList().get(0).getPluginName());
		Assert.assertEquals("Type2", personnageListAction.getPlayerPersonnageList().get(1).getPluginName());
		Assert.assertEquals("Type1", personnageListAction.getPlayerPersonnageList().get(2).getPluginName());
		Assert.assertNull(personnageListAction.getGameMasterPersonnageList().get(0).getPlayer());
		Assert.assertEquals("Y", personnageListAction.getGameMasterPersonnageList().get(1).getPlayer().getUsername());
		Assert.assertEquals("X", personnageListAction.getGameMasterPersonnageList().get(2).getPlayer().getUsername());
	}
	
	
	@Test
	public void testGetGameMasterSortDir_Success_Asc(){
		// Prepare
		ActionContext.getContext().getSession().put("gameMasterPersonnageSort", new PersonnageNameComparator(1));
		
		// Execute
		String result = personnageListAction.getGameMasterSortDir();
		
		// Check
		Assert.assertEquals("asc", result);
	}
	
	
	@Test
	public void testGetGameMasterSortDir_Success_Desc(){
		// Prepare
		ActionContext.getContext().getSession().put("gameMasterPersonnageSort", new PersonnageNameComparator(-1));
		
		// Execute
		String result = personnageListAction.getGameMasterSortDir();
		
		// Check
		Assert.assertEquals("dec", result);
	}
	
	
	@Test
	public void testGetGameMasterSortDir_Fail(){
		// Prepare
		ActionContext.getContext().getSession().put("gameMasterPersonnageSort", new PersonnageNameComparator(0));
		
		// Execute
		IllegalArgumentException exception = null;
		try{
			personnageListAction.getGameMasterSortDir();
		}catch(IllegalArgumentException iae){
			exception = iae;
		}
		
		// Check
		Assert.assertNotNull("desc", exception);
		Assert.assertEquals("Direction should be 1 or -1", exception.getMessage());
	}
	
	
	@Test
	public void testGetPlayerSortDir_Success_Asc(){
		// Prepare
		ActionContext.getContext().getSession().put("playerPersonnageSort", new PersonnageNameComparator(1));
		
		// Execute
		String result = personnageListAction.getPlayerSortDir();
		
		// Check
		Assert.assertEquals("asc", result);
	}
	
	
	@Test
	public void testGetPlayerSortDir_Success_Desc(){
		// Prepare
		ActionContext.getContext().getSession().put("playerPersonnageSort", new PersonnageNameComparator(-1));
		
		// Execute
		String result = personnageListAction.getPlayerSortDir();
		
		// Check
		Assert.assertEquals("dec", result);
	}
	
	
	@Test
	public void testGetPlayerSortDir_Fail(){
		// Prepare
		ActionContext.getContext().getSession().put("playerPersonnageSort", new PersonnageNameComparator(0));
		
		// Execute
		IllegalArgumentException exception = null;
		try{
			personnageListAction.getPlayerSortDir();
		}catch(IllegalArgumentException iae){
			exception = iae;
		}
		
		// Check
		Assert.assertNotNull("desc", exception);
		Assert.assertEquals("Direction should be 1 or -1", exception.getMessage());
	}
	
}
