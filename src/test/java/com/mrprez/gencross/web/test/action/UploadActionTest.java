package com.mrprez.gencross.web.test.action;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.UploadAction;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IGcrFileBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class UploadActionTest extends AbstractActionTest {

	@Mock
	private IPersonnageBS personnageBS;

	@Mock
	private IGcrFileBS gcrFileBS;

	@InjectMocks
	private UploadAction uploadAction;



	@Test
	public void testExecute_Fail() throws Exception {
		// Execute
		String result = uploadAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertNull(uploadAction.getPersonnageName());
	}
	
	
	@Test
	public void testSuccess_GameMaster() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		String personnageName = "superman";
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setName(personnageName);
		personnageWork.setGameMaster(user);
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		uploadAction.setPersonnageId(personnageId);
		String result = uploadAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals(personnageName, uploadAction.getPersonnageName());
		Assert.assertTrue(uploadAction.isGm());
	}
	
	
	@Test
	public void testSuccess_Player() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		String personnageName = "superman";
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setName(personnageName);
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		uploadAction.setPersonnageId(personnageId);
		String result = uploadAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals(personnageName, uploadAction.getPersonnageName());
		Assert.assertFalse(uploadAction.isGm());
	}


	@Test
	public void testUpload_Fail_NoGcrFile() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		
		// Execute
		String result = uploadAction.upload();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(uploadAction.getActionErrors().contains("Vous devez charger un fichier Gencross"));
	}
	
	
	@Test
	public void testUpload_Fail_IssueOnUploadBusiness() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		String error = "Business error";
		File gcrFile = new File("");
		int personnageId = 2;
		String password = "I am batman";
		Mockito.when(gcrFileBS.uploadGcrPersonnage(gcrFile, personnageId, user, password)).thenReturn(error);
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(new PersonnageWorkBO());
		
		// Execute
		uploadAction.setGcrFile(gcrFile);
		uploadAction.setPersonnageId(personnageId);
		uploadAction.setPassword(password);
		String result = uploadAction.upload();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(uploadAction.getActionErrors().contains(error));
	}
	
	
	@Test
	public void testUpload_Success_Upload() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		File gcrFile = new File("");
		int personnageId = 2;
		String password = "I am batman";
		Mockito.when(gcrFileBS.uploadGcrPersonnage(gcrFile, personnageId, user, password)).thenReturn(null);
		Mockito.when(personnageBS.loadPersonnageAsGameMaster(personnageId, user)).thenReturn(new PersonnageWorkBO());
		
		// Execute
		uploadAction.setGcrFile(gcrFile);
		uploadAction.setPersonnageId(personnageId);
		uploadAction.setPassword(password);
		String result = uploadAction.upload();

		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(gcrFileBS).uploadGcrPersonnage(gcrFile, personnageId, user, password);
	}
	
	
	@Test
	public void testUpload_Fail_CreationAsGm() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		File gcrFile = new File("");
		String personnageName = "superman";
		String password = "bad password";
		Mockito.when(gcrFileBS.createPersonnageAsGameMaster(gcrFile, personnageName, user, password)).thenReturn(null);
		
		// Execute
		uploadAction.setGm(true);
		uploadAction.setPersonnageName(personnageName);
		uploadAction.setGcrFile(gcrFile);
		uploadAction.setPassword(password);
		String result = uploadAction.upload();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(uploadAction.getActionErrors().contains("Mot de passe du MJ erroné."));
		Assert.assertNull(uploadAction.getPersonnageId());
	}
	
	
	@Test
	public void testUpload_Success_CreationAsGm() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		File gcrFile = new File("");
		String personnageName = "superman";
		String password = "bad password";
		Integer personnageId = 3;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setId(personnageId);
		Mockito.when(gcrFileBS.createPersonnageAsGameMaster(gcrFile, personnageName, user, password)).thenReturn(personnageWork);
		
		// Execute
		uploadAction.setGm(true);
		uploadAction.setPersonnageName(personnageName);
		uploadAction.setGcrFile(gcrFile);
		uploadAction.setPassword(password);
		String result = uploadAction.upload();

		// Check
		Assert.assertEquals("success", result);
		Assert.assertEquals(personnageId, uploadAction.getPersonnageId());
		Mockito.verify(gcrFileBS).createPersonnageAsGameMaster(gcrFile, personnageName, user, password);
		Mockito.verifyNoMoreInteractions(gcrFileBS);
	}
	
	
	@Test
	public void testUpload_Success_CreationAsPlayer() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		File gcrFile = new File("");
		String personnageName = "superman";
		Integer personnageId = 3;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setId(personnageId);
		Mockito.when(gcrFileBS.createPersonnageAsPlayer(gcrFile, personnageName, user)).thenReturn(personnageWork);
		
		// Execute
		uploadAction.setGm(false);
		uploadAction.setPersonnageName(personnageName);
		uploadAction.setGcrFile(gcrFile);
		String result = uploadAction.upload();

		// Check
		Assert.assertEquals("success", result);
		Assert.assertEquals(personnageId, uploadAction.getPersonnageId());
		Mockito.verify(gcrFileBS).createPersonnageAsPlayer(gcrFile, personnageName, user);
		Mockito.verifyNoMoreInteractions(gcrFileBS);
	}

}
