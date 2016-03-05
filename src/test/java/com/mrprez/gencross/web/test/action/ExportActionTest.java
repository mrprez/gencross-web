package com.mrprez.gencross.web.test.action;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.export.DrawerGenerator;
import com.mrprez.gencross.export.FileGenerator;
import com.mrprez.gencross.export.TemplatedFileGenerator;
import com.mrprez.gencross.export.TextGenerator;
import com.mrprez.gencross.export.XlsGenerator;
import com.mrprez.gencross.web.action.ExportAction;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IExportBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class ExportActionTest extends AbstractActionTest {

	@Mock
	private IExportBS exportBS;

	@Mock
	private IPersonnageBS personnageBS;

	@InjectMocks
	private ExportAction exportAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPluginName("DC-Comics");
		Map<Class<? extends TemplatedFileGenerator>, List<String>> templateFiles = new HashMap<Class<? extends TemplatedFileGenerator>, List<String>>();
		templateFiles.put(DrawerGenerator.class, Arrays.asList("template1", "template2"));
		templateFiles.put(XlsGenerator.class, Arrays.asList("template3"));
		templateFiles.put(TextGenerator.class, Arrays.asList("template4"));
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(personnageWork);
		Mockito.when(exportBS.getTemplateFiles("DC-Comics")).thenReturn(templateFiles);
		
		// Execute
		exportAction.setPersonnageId(personnageId);
		String result = exportAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals(3, exportAction.getTemplateFiles().size());
		Class<? extends TemplatedFileGenerator> generatorClass;
		Iterator<Class<? extends TemplatedFileGenerator>> templateIt = exportAction.getTemplateFiles().keySet().iterator();
		generatorClass = templateIt.next();
		Assert.assertEquals(DrawerGenerator.class, generatorClass);
		Assert.assertEquals(3, exportAction.getTemplateFiles().get(generatorClass).size());
		Assert.assertEquals("template1", exportAction.getTemplateFiles().get(generatorClass).get(0));
		Assert.assertEquals("template2", exportAction.getTemplateFiles().get(generatorClass).get(1));
		Assert.assertEquals("Uploader un fichier", exportAction.getTemplateFiles().get(generatorClass).get(2));
		
		generatorClass = templateIt.next();
		Assert.assertEquals(TextGenerator.class, generatorClass);
		Assert.assertEquals(2, exportAction.getTemplateFiles().get(generatorClass).size());
		Assert.assertEquals("template4", exportAction.getTemplateFiles().get(generatorClass).get(0));
		Assert.assertEquals("Uploader un fichier", exportAction.getTemplateFiles().get(generatorClass).get(1));
		
		generatorClass = templateIt.next();
		Assert.assertEquals(XlsGenerator.class, generatorClass);
		Assert.assertEquals(2, exportAction.getTemplateFiles().get(generatorClass).size());
		Assert.assertEquals("template3", exportAction.getTemplateFiles().get(generatorClass).get(0));
		Assert.assertEquals("Uploader un fichier", exportAction.getTemplateFiles().get(generatorClass).get(1));
	}


	@Test
	public void testExport_Fail_NoPersonnage() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		
		// Execute
		exportAction.setPersonnageId(personnageId);
		String result = exportAction.export();

		// Check
		Assert.assertEquals("error", result);
	}
	
	
	@Test
	public void testExport_Fail_NoGenerator() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(personnageWork);
		
		// Execute
		exportAction.setPersonnageId(personnageId);
		String result = exportAction.export();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(exportAction.getActionErrors().contains("Type d'export introuvable."));
	}
	
	
	@Test
	public void testExport_Fail_NoUploadedFile() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		String fileGeneratorName = "MockGenerator";
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		FileGenerator fileGenerator = Mockito.mock(FileGenerator.class);
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(personnageWork);
		Mockito.when(exportBS.getGenerator(fileGeneratorName)).thenReturn(fileGenerator);
		
		// Execute
		exportAction.setPersonnageId(personnageId);
		exportAction.setFileGeneratorName(fileGeneratorName);
		exportAction.setSelectedTemplate("Uploader un fichier");
		String result = exportAction.export();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(exportAction.getActionErrors().contains("Vous devez uploader un fichier template"));
	}
	
	
	@Test
	public void testExport_Success_UploadedFile() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		String fileGeneratorName = "MockGenerator";
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		File templateFile = new File("");
		TemplatedFileGenerator fileGenerator = Mockito.mock(TemplatedFileGenerator.class);
		byte[] export = new byte[]{0,1,2,3,4,5,6,7,8,9};
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(personnageWork);
		Mockito.when(exportBS.getGenerator(fileGeneratorName)).thenReturn(fileGenerator);
		Mockito.when(fileGenerator.getOutputExtension()).thenReturn("mck");
		Mockito.when(exportBS.export(personnageWork, fileGenerator, templateFile)).thenReturn(export);
		
		// Execute
		exportAction.setPersonnageId(personnageId);
		exportAction.setFileGeneratorName(fileGeneratorName);
		exportAction.setSelectedTemplate("Uploader un fichier");
		exportAction.setTemplateFile(templateFile);
		String result = exportAction.export();

		// Check
		Assert.assertEquals("success", result);
		Assert.assertEquals(10, exportAction.getFileSize().intValue());
		Assert.assertNotNull(exportAction.getInputStream());
		Assert.assertEquals("export.mck", exportAction.getFileName());
	}
	
	
	@Test
	public void testExport_Success_TemplateFile() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		String fileGeneratorName = "MockGenerator";
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		String selectedTemplate = "template1";
		TemplatedFileGenerator fileGenerator = Mockito.mock(TemplatedFileGenerator.class);
		byte[] export = new byte[]{0,1,2,3,4,5,6,7,8,9};
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(personnageWork);
		Mockito.when(exportBS.getGenerator(fileGeneratorName)).thenReturn(fileGenerator);
		Mockito.when(fileGenerator.getOutputExtension()).thenReturn("mck");
		Mockito.when(exportBS.export(personnageWork, fileGenerator, selectedTemplate)).thenReturn(export);
		
		// Execute
		exportAction.setPersonnageId(personnageId);
		exportAction.setFileGeneratorName(fileGeneratorName);
		exportAction.setSelectedTemplate(selectedTemplate);
		String result = exportAction.export();

		// Check
		Assert.assertEquals("success", result);
		Assert.assertEquals(10, exportAction.getFileSize().intValue());
		Assert.assertNotNull(exportAction.getInputStream());
		Assert.assertEquals("export.mck", exportAction.getFileName());
	}
	
	
	@Test
	public void testExport_Success_SimpleGenerator() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer personnageId = 2;
		String fileGeneratorName = "MockGenerator";
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		FileGenerator fileGenerator = Mockito.mock(FileGenerator.class);
		byte[] export = new byte[]{0,1,2,3,4,5,6,7,8,9};
		Mockito.when(personnageBS.loadPersonnage(personnageId, user)).thenReturn(personnageWork);
		Mockito.when(exportBS.getGenerator(fileGeneratorName)).thenReturn(fileGenerator);
		Mockito.when(fileGenerator.getOutputExtension()).thenReturn("mck");
		Mockito.when(exportBS.export(personnageWork, fileGenerator)).thenReturn(export);
		
		// Execute
		exportAction.setPersonnageId(personnageId);
		exportAction.setFileGeneratorName(fileGeneratorName);
		String result = exportAction.export();

		// Check
		Assert.assertEquals("success", result);
		Assert.assertEquals(10, exportAction.getFileSize().intValue());
		Assert.assertNotNull(exportAction.getInputStream());
		Assert.assertEquals("export.mck", exportAction.getFileName());
	}
	
	
	@Test
	public void testGetGeneratorList(){
		// Execute
		Map<String, Class<? extends FileGenerator>> result = exportAction.getGeneratorList();
		
		// Check
		Assert.assertEquals(FileGenerator.getGeneratorList(), result);
	}
}
