package com.mrprez.gencross.web.test.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.web.action.CreatePersonnageAction;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class CreatePersonnageActionTest extends AbstractActionTest {

	@Mock
	private IPersonnageBS personnageBS;

	@Mock
	private IAuthentificationBS authentificationBS;

	@InjectMocks
	private CreatePersonnageAction createPersonnageAction;

	private String personnageName = "superman";
	private String pluginName = "DC";
	private Integer personnageId = 8;
	private UserBO user = AuthentificationBSTest.buildUser("batman");
	


	@Test
	public void testExecute() throws Exception {
		// Prepare
		ActionContext.getContext().getSession().put("user", user);
		Collection<PluginDescriptor> personnageTypeList = Arrays.asList(new PluginDescriptor());
		List<UserBO> userList = new ArrayList<UserBO>();
		userList.add(AuthentificationBSTest.buildUser("robin"));
		userList.add(AuthentificationBSTest.buildUser("alfred"));
		userList.add(AuthentificationBSTest.buildUser("batman"));
		Mockito.when(personnageBS.getAvailablePersonnageTypes()).thenReturn(personnageTypeList);
		Mockito.when(authentificationBS.getUserList()).thenReturn(userList);

		// Execute
		String result = createPersonnageAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals(personnageTypeList, createPersonnageAction.getPersonnageTypeList());
		Assert.assertEquals(2, createPersonnageAction.getUserList().size());
		Assert.assertTrue(createPersonnageAction.getUserList().contains(AuthentificationBSTest.buildUser("robin")));
		Assert.assertTrue(createPersonnageAction.getUserList().contains(AuthentificationBSTest.buildUser("alfred")));
	}
	
	
	@Test
	public void testCreate_Error_BlankName() throws Exception {
		// Prepare
		Mockito.when(authentificationBS.getUserList()).thenReturn(new ArrayList<UserBO>());
		
		// Execute
		createPersonnageAction.setPersonnageName(" ");
		String result = createPersonnageAction.create();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(createPersonnageAction.getActionErrors().contains("Veuillez renseigner un nom pour ce personnage."));
		Mockito.verify(personnageBS, Mockito.never()).createPersonnageAsGameMaster(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(UserBO.class));
		Mockito.verify(personnageBS, Mockito.never()).createPersonnageAsPlayer(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(UserBO.class));
		Mockito.verify(personnageBS, Mockito.never()).createPersonnageAsGameMasterAndPlayer(Mockito.anyString(), Mockito.anyString(), Mockito.any(UserBO.class));
	}
	
	
	@Test
	public void testCreate_Error_NoRole() throws Exception {
		// Prepare
		ActionContext.getContext().getSession().put("user", user);
		String personnageName = "superman";
		Mockito.when(authentificationBS.getUserList()).thenReturn(new ArrayList<UserBO>());
		
		// Execute
		createPersonnageAction.setPersonnageName(personnageName);
		createPersonnageAction.setRole("");
		String result = createPersonnageAction.create();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(createPersonnageAction.getActionErrors().contains("Vous devez choisir un rôle par rapport à votre personnage."));
		Mockito.verify(personnageBS, Mockito.never()).createPersonnageAsGameMaster(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(UserBO.class));
		Mockito.verify(personnageBS, Mockito.never()).createPersonnageAsPlayer(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(UserBO.class));
		Mockito.verify(personnageBS, Mockito.never()).createPersonnageAsGameMasterAndPlayer(Mockito.anyString(), Mockito.anyString(), Mockito.any(UserBO.class));
	}
	
	
	private void prepareCreateSuccess(PersonnageWorkBO businessMethodCall){
		ActionContext.getContext().getSession().put("user", user);
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setId(personnageId);
		Mockito.when(businessMethodCall).thenReturn(personnageWork);
		
		createPersonnageAction.setPersonnageName(personnageName);
		createPersonnageAction.setSelectedPersonnageTypeName(pluginName);
	}
	
	
	@Test
	public void testCreate_Success_GameMasterWithPlayer() throws Exception {
		// Prepare
		String playerName = "robin";
		prepareCreateSuccess(personnageBS.createPersonnageAsGameMaster(pluginName, personnageName, playerName, user));
		
		// Execute
		createPersonnageAction.setRole("Maître de jeux");
		createPersonnageAction.setPlayerName(playerName);
		String result = createPersonnageAction.create();

		// Check
		Assert.assertEquals("success", result);
		Assert.assertEquals(personnageId, createPersonnageAction.getPersonnageId());
		Mockito.verify(personnageBS).createPersonnageAsGameMaster(pluginName, personnageName, playerName, user);
		Mockito.verify(personnageBS, Mockito.never()).createPersonnageAsPlayer(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(UserBO.class));
		Mockito.verify(personnageBS, Mockito.never()).createPersonnageAsGameMasterAndPlayer(Mockito.anyString(), Mockito.anyString(), Mockito.any(UserBO.class));
	}
	
	
	@Test
	public void testCreate_Success_GameMasterWithoutPlayer() throws Exception {
		// Prepare
		String playerName = "_no_one_";
		prepareCreateSuccess(personnageBS.createPersonnageAsGameMaster(pluginName, personnageName, null, user));
		
		// Execute
		createPersonnageAction.setRole("Maître de jeux");
		createPersonnageAction.setPlayerName(playerName);
		String result = createPersonnageAction.create();

		// Check
		Assert.assertEquals("success", result);
		Assert.assertEquals(personnageId, createPersonnageAction.getPersonnageId());
		Mockito.verify(personnageBS).createPersonnageAsGameMaster(pluginName, personnageName, null, user);
		Mockito.verify(personnageBS, Mockito.never()).createPersonnageAsPlayer(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(UserBO.class));
		Mockito.verify(personnageBS, Mockito.never()).createPersonnageAsGameMasterAndPlayer(Mockito.anyString(), Mockito.anyString(), Mockito.any(UserBO.class));
	}
	
	
	@Test
	public void testCreate_Success_PlayerWithGameMaster() throws Exception {
		// Prepare
		String gameMasterName = "robin";
		prepareCreateSuccess(personnageBS.createPersonnageAsPlayer(pluginName, personnageName, gameMasterName, user));
		
		// Execute
		createPersonnageAction.setRole("Joueur");
		createPersonnageAction.setGameMasterName(gameMasterName);
		String result = createPersonnageAction.create();

		// Check
		Assert.assertEquals("success", result);
		Assert.assertEquals(personnageId, createPersonnageAction.getPersonnageId());
		Mockito.verify(personnageBS, Mockito.never()).createPersonnageAsGameMaster(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(UserBO.class));
		Mockito.verify(personnageBS).createPersonnageAsPlayer(pluginName, personnageName, gameMasterName, user);
		Mockito.verify(personnageBS, Mockito.never()).createPersonnageAsGameMasterAndPlayer(Mockito.anyString(), Mockito.anyString(), Mockito.any(UserBO.class));
	}
	
	
	@Test
	public void testCreate_Success_PlayerWithoutGameMaster() throws Exception {
		// Prepare
		String gameMasterName = "_no_one_";
		prepareCreateSuccess(personnageBS.createPersonnageAsPlayer(pluginName, personnageName, null, user));
		
		// Execute
		createPersonnageAction.setRole("Joueur");
		createPersonnageAction.setGameMasterName(gameMasterName);
		String result = createPersonnageAction.create();

		// Check
		Assert.assertEquals("success", result);
		Assert.assertEquals(personnageId, createPersonnageAction.getPersonnageId());
		Mockito.verify(personnageBS, Mockito.never()).createPersonnageAsGameMaster(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(UserBO.class));
		Mockito.verify(personnageBS).createPersonnageAsPlayer(pluginName, personnageName, null, user);
		Mockito.verify(personnageBS, Mockito.never()).createPersonnageAsGameMasterAndPlayer(Mockito.anyString(), Mockito.anyString(), Mockito.any(UserBO.class));
	}
	
	
	@Test
	public void testCreate_Success_PlayerAndGameMaster() throws Exception {
		// Prepare
		prepareCreateSuccess(personnageBS.createPersonnageAsGameMasterAndPlayer(pluginName, personnageName, user));
		
		// Execute
		createPersonnageAction.setRole("Les deux");
		String result = createPersonnageAction.create();

		// Check
		Assert.assertEquals("success", result);
		Assert.assertEquals(personnageId, createPersonnageAction.getPersonnageId());
		Mockito.verify(personnageBS, Mockito.never()).createPersonnageAsGameMaster(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(UserBO.class));
		Mockito.verify(personnageBS, Mockito.never()).createPersonnageAsPlayer(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(UserBO.class));
		Mockito.verify(personnageBS).createPersonnageAsGameMasterAndPlayer(pluginName, personnageName, user);
	}
	
	
	
	
}
