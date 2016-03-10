package com.mrprez.gencross.web.test.action;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.web.action.HelpFileAction;
import com.mrprez.gencross.web.bo.PersonnageDataBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class HelpFileActionTest extends AbstractActionTest {

	@InjectMocks
	private HelpFileAction helpFileAction;



	@Test
	public void testExecute_Fail_NoPersonnageInSession() throws Exception {
		// Execute
		String result = helpFileAction.execute();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(helpFileAction.getActionErrors().contains("Session invalide."));
	}
	
	
	@Test
	public void testExecute_Fail_PersonnageNotInSession() throws Exception {
		// Prepare
		Map<Integer,PersonnageWorkBO> personnagesWorks = new HashMap<Integer, PersonnageWorkBO>();
		ActionContext.getContext().getSession().put("personnagesWorks", personnagesWorks);
		Integer personnageWorkId = 2;
		
		// Execute
		helpFileAction.setPersonnageWorkId(personnageWorkId);
		String result = helpFileAction.execute();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(helpFileAction.getActionErrors().contains("Session invalide."));
	}
	
	
	@Test
	public void testExecute_Fail_NoHelpFile() throws Exception {
		// Prepare
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPersonnageData(new PersonnageDataBO());
		personnageWork.getPersonnageData().setPersonnage(new Personnage());
		personnageWork.getPersonnage().setPluginDescriptor(new PluginDescriptor());
		Integer personnageWorkId = 2;
		Map<Integer,PersonnageWorkBO> personnagesWorks = new HashMap<Integer, PersonnageWorkBO>();
		personnagesWorks.put(personnageWorkId, personnageWork);
		ActionContext.getContext().getSession().put("personnagesWorks", personnagesWorks);
		
		// Execute
		helpFileAction.setPersonnageWorkId(personnageWorkId);
		String result = helpFileAction.execute();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(helpFileAction.getActionErrors().contains("Pas de fichier d'aide pour ce type de personnage."));
	}
	
	
	@Test
	public void testExecute_Success() throws Exception {
		// Prepare
		PluginDescriptor pluginDescriptor = new PluginDescriptor();
		pluginDescriptor.setHelpFileName("Help File Example.txt");
		pluginDescriptor.setHelpFileSize(100L);
		Personnage personnage = new Personnage();
		personnage.setPluginDescriptor(pluginDescriptor);
		Personnage personnageMock = Mockito.spy(personnage);
		InputStream inputStream = Mockito.mock(InputStream.class);
		Mockito.when(personnageMock.getHelpFileInputStream()).thenReturn(inputStream);
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPersonnageData(new PersonnageDataBO());
		personnageWork.getPersonnageData().setPersonnage(personnageMock);
		Integer personnageWorkId = 2;
		Map<Integer,PersonnageWorkBO> personnagesWorks = new HashMap<Integer, PersonnageWorkBO>();
		personnagesWorks.put(personnageWorkId, personnageWork);
		ActionContext.getContext().getSession().put("personnagesWorks", personnagesWorks);
		
		// Execute
		helpFileAction.setPersonnageWorkId(personnageWorkId);
		String result = helpFileAction.execute();

		// Check
		Assert.assertEquals("success", result);
		Assert.assertEquals("100", helpFileAction.getContentLength());
		Assert.assertSame(inputStream, helpFileAction.getHelpFileInputStream());
		Assert.assertEquals("attachment;filename=\"Help File Example.txt\"", helpFileAction.getContentDisposition());
	}
	
	
	
}
