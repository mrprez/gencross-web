package com.mrprez.gencross.web.test.action;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.export.DrawerGenerator;
import com.mrprez.gencross.export.FileGenerator;
import com.mrprez.gencross.export.SimpleTxtGenerator;
import com.mrprez.gencross.export.TemplatedFileGenerator;
import com.mrprez.gencross.export.TextGenerator;
import com.mrprez.gencross.web.action.MultiExportAction;
import com.mrprez.gencross.web.bo.MultiExportBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IExportBS;
import com.mrprez.gencross.web.bs.face.ITableBS;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.mrprez.gencross.web.test.bs.PersonnageWorkBSTest;
import com.mrprez.gencross.web.test.bs.TableBSTest;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class MultiExportActionTest extends AbstractActionTest {

	@Mock
	private IExportBS exportBS;

	@Mock
	private ITableBS tableBS;

	@InjectMocks
	private MultiExportAction multiExportAction;



	@Test
	public void testExecute_Fail_BadTableId() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		
		// Execute
		multiExportAction.setTableId(1);
		String result = multiExportAction.execute();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertEquals(1, multiExportAction.getActionErrors().size());
		Assert.assertEquals("Impossible de charger cette table", multiExportAction.getActionErrors().iterator().next());
	}
	
	
	@Test
	public void testExecute_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		TableBO table = TableBSTest.buildTable(user, "Gotham", "DC");
		Integer tableId = table.getId();
		PersonnageWorkBO personnageWork1 = PersonnageWorkBSTest.buildPersonnageWorkMock(1, "Superman", AuthentificationBSTest.buildUser("catwoman"), user);
		table.getPersonnages().add(personnageWork1);
		PersonnageWorkBO personnageWork2 = PersonnageWorkBSTest.buildPersonnageWorkMock(2, "Flash", AuthentificationBSTest.buildUser("robin"), user);
		table.getPersonnages().add(personnageWork2);
		PersonnageWorkBO personnageWork3 = PersonnageWorkBSTest.buildPersonnageWorkMock(3, "Luthor", null, user);
		table.getPersonnages().add(personnageWork3);
		
		Map<Class<? extends TemplatedFileGenerator>, List<String>> templateFiles = new HashMap<Class<? extends TemplatedFileGenerator>, List<String>>();
		templateFiles.put(DrawerGenerator.class, Arrays.asList("Template 1", "Template 2"));
		templateFiles.put(TextGenerator.class, Arrays.asList("Template 3"));
		
		Mockito.when(tableBS.getTableForGM(tableId, user)).thenReturn(table);
		ActionContext.getContext().getSession().put("user", user);
		Mockito.when(exportBS.getTemplateFiles("DC")).thenReturn(templateFiles);
		
		// Execute
		multiExportAction.setTableId(tableId);
		String result = multiExportAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals(2, multiExportAction.getPjList().size());
		Assert.assertEquals("Superman (catwoman)", multiExportAction.getPjList().get(1));
		Assert.assertEquals("Flash (robin)", multiExportAction.getPjList().get(2));
		Assert.assertEquals(1, multiExportAction.getPnjList().size());
		Assert.assertEquals("Luthor", multiExportAction.getPnjList().get(3));
		Assert.assertEquals(2, multiExportAction.getTemplateFiles().size());
		
		Assert.assertEquals(3, multiExportAction.getTemplateFiles().get(DrawerGenerator.class).size());
		Assert.assertEquals("Template 1", multiExportAction.getTemplateFiles().get(DrawerGenerator.class).get(0));
		Assert.assertEquals("Template 2", multiExportAction.getTemplateFiles().get(DrawerGenerator.class).get(1));
		Assert.assertEquals("Uploader un fichier", multiExportAction.getTemplateFiles().get(DrawerGenerator.class).get(2));
		
		Assert.assertEquals(2, multiExportAction.getTemplateFiles().get(TextGenerator.class).size());
		Assert.assertEquals("Template 3", multiExportAction.getTemplateFiles().get(TextGenerator.class).get(0));
		Assert.assertEquals("Uploader un fichier", multiExportAction.getTemplateFiles().get(TextGenerator.class).get(1));
	}


	@Test
	public void testExport_Fail_BadTableId() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		
		// Execute
		String result = multiExportAction.export();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(multiExportAction.getActionErrors().contains("Impossible de charger cette table"));
	}
	
	
	@Test
	public void testExport_Fail_NoPersonnage() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		TableBO table = TableBSTest.buildTable(user, "Gotham", "DC");
		Mockito.when(tableBS.getTableForGM(table.getId(), user)).thenReturn(table);
		
		// Execute
		multiExportAction.setTableId(table.getId());
		String result = multiExportAction.export();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertTrue(multiExportAction.getActionErrors().contains("Vous devez selectionner au moins un personnage."));
		Assert.assertNotNull(multiExportAction.getPjList());
		Assert.assertNotNull(multiExportAction.getPnjList());
	}
	
	
	@Test
	public void testExport_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		TableBO table = TableBSTest.buildTable(user, "Gotham", "DC");
		MultiExportBO multiExport = new MultiExportBO(new Personnage[3]);
		Mockito.when(tableBS.getTableForGM(table.getId(), user)).thenReturn(table);
		Mockito.when(exportBS.multiExportInGrid(Arrays.asList(1,2,3), user)).thenReturn(multiExport);
		
		// Execute
		multiExportAction.setTableId(table.getId());
		multiExportAction.setExportedPjList(Arrays.asList(1,2));
		multiExportAction.setExportedPnjList(Arrays.asList(3));
		String result = multiExportAction.export();

		// Check
		Assert.assertEquals("success", result);
		Assert.assertEquals(multiExport, multiExportAction.getExport());
	}


	@Test
	public void testExportCsv_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		TableBO table = TableBSTest.buildTable(user, "Gotham", "DC");
		MultiExportBO multiExport = buildMultiExport();
		Mockito.when(tableBS.getTableForGM(table.getId(), user)).thenReturn(table);
		Mockito.when(exportBS.multiExportInGrid(Arrays.asList(1,2,3), user)).thenReturn(multiExport);
		
		// Execute
		multiExportAction.setTableId(table.getId());
		multiExportAction.setExportedPjList(Arrays.asList(1,2));
		multiExportAction.setExportedPnjList(Arrays.asList(3));
		String result = multiExportAction.exportCsv();

		// Check
		Assert.assertEquals("file", result);
		Assert.assertNotNull(multiExportAction.getInputStream());
		Assert.assertEquals(39, multiExportAction.getFileSize().intValue());
		Assert.assertEquals("multiExport.csv", multiExportAction.getFileName());
	}
	
	
	@Test
	public void testExportCsv_Fail() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		
		// Execute
		String result = multiExportAction.exportCsv();
		
		// Check
		Assert.assertEquals("error", result);
		Assert.assertEquals("Impossible de charger cette table", multiExportAction.getActionErrors().iterator().next());
	}
	
	
	private MultiExportBO buildMultiExport() throws IOException{
		Personnage[] personnages = new Personnage[3];
		for(int i=0; i<personnages.length; i++){
			personnages[i] = new Personnage();
		}
		MultiExportBO multiExportBO = new MultiExportBO(personnages);
		
		Property[] properties = new Property[3];
		for(int propNb=0; propNb<personnages.length; propNb++){
			properties[propNb] = new Property("Line 1", propNb, personnages[propNb]);
		}
		multiExportBO.addFullLine("Line 1", properties);
		
		properties = new Property[3];
		for(int propNb=0; propNb<personnages.length; propNb++){
			properties[propNb] = new Property("Line 2", personnages[propNb]);
		}
		multiExportBO.addFullLine("Line 2", properties);
		
		multiExportBO.addSimpleElement(new Property("Prop 3", 11, personnages[1]));
		
		return multiExportBO;
	}


	@Test
	public void testExportZip_Fail_BadTableId() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		
		// Execute
		String result = multiExportAction.exportZip();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(multiExportAction.getActionErrors().contains("Impossible de charger cette table"));
	}
	
	@Test
	public void testExportZip_Fail_NoPersonnage() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		TableBO table = TableBSTest.buildTable(user, "Gotham", "DC");
		Mockito.when(tableBS.getTableForGM(table.getId(), user)).thenReturn(table);
		
		// Execute
		multiExportAction.setTableId(table.getId());
		String result = multiExportAction.exportZip();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertTrue(multiExportAction.getActionErrors().contains("Vous devez selectionner au moins un personnage."));
		Assert.assertNotNull(multiExportAction.getPjList());
		Assert.assertNotNull(multiExportAction.getPnjList());
	}
	
	@Test
	public void testExportZip_Fail_NoFileGenerator() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		TableBO table = TableBSTest.buildTable(user, "Gotham", "DC");
		Mockito.when(tableBS.getTableForGM(table.getId(), user)).thenReturn(table);
		
		// Execute
		multiExportAction.setTableId(table.getId());
		multiExportAction.setExportedPjList(Arrays.asList(1,2));
		multiExportAction.setExportedPnjList(Arrays.asList(3));
		String result = multiExportAction.exportZip();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(multiExportAction.getActionErrors().contains("Type d'export introuvable."));
	}
	
	
	@Test
	public void testExportZip_Fail_NoTemplate() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		TableBO table = TableBSTest.buildTable(user, "Gotham", "DC");
		Mockito.when(tableBS.getTableForGM(table.getId(), user)).thenReturn(table);
		Mockito.when(exportBS.getGenerator("DrawerGenerator")).thenReturn(new DrawerGenerator());
		
		// Execute
		multiExportAction.setTableId(table.getId());
		multiExportAction.setExportedPjList(Arrays.asList(1,2));
		multiExportAction.setExportedPnjList(Arrays.asList(3));
		multiExportAction.setFileGeneratorName("DrawerGenerator");
		multiExportAction.setSelectedTemplate("Uploader un fichier");
		String result = multiExportAction.exportZip();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertTrue(multiExportAction.getActionErrors().contains("Vous n'avez chargé aucun fichier template."));
		Assert.assertNotNull(multiExportAction.getPjList());
		Assert.assertNotNull(multiExportAction.getPnjList());
	}
	
	@Test
	public void testExportZip_Success_SimpleGenerator() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		TableBO table = TableBSTest.buildTable(user, "Gotham", "DC");
		FileGenerator fileGenerator = new SimpleTxtGenerator();
		byte[] byteArray = new byte[]{0,1,2,3,4,5,7,8,9};
		Mockito.when(tableBS.getTableForGM(table.getId(), user)).thenReturn(table);
		Mockito.when(exportBS.getGenerator("SimpleTxtGenerator")).thenReturn(fileGenerator);
		Mockito.when(exportBS.multiExport(Arrays.asList(1,2,3), user, fileGenerator)).thenReturn(byteArray);
		
		// Execute
		multiExportAction.setTableId(table.getId());
		multiExportAction.setExportedPjList(Arrays.asList(1,2));
		multiExportAction.setExportedPnjList(Arrays.asList(3));
		multiExportAction.setFileGeneratorName("SimpleTxtGenerator");
		String result = multiExportAction.exportZip();

		// Check
		Assert.assertEquals("file", result);
		Assert.assertEquals(byteArray.length, multiExportAction.getFileSize().intValue());
		Assert.assertEquals("multiExport.zip", multiExportAction.getFileName());
		Assert.assertNotNull(multiExportAction.getInputStream());
		Mockito.verify(exportBS).multiExport(Arrays.asList(1,2,3), user, fileGenerator);
	}
	
	
	@Test
	public void testExportZip_Success_TemplateFileGenerator() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		TableBO table = TableBSTest.buildTable(user, "Gotham", "DC");
		TemplatedFileGenerator fileGenerator = new DrawerGenerator();
		String selectedTemplate = "Template A";
		byte[] byteArray = new byte[]{0,1,2,3,4,5,7,8,9};
		Mockito.when(tableBS.getTableForGM(table.getId(), user)).thenReturn(table);
		Mockito.when(exportBS.getGenerator("DrawerGenerator")).thenReturn(fileGenerator);
		Mockito.when(exportBS.multiExport(Arrays.asList(1,2,3), user, fileGenerator, "DC", selectedTemplate)).thenReturn(byteArray);
		
		// Execute
		multiExportAction.setTableId(table.getId());
		multiExportAction.setExportedPjList(Arrays.asList(1,2));
		multiExportAction.setExportedPnjList(Arrays.asList(3));
		multiExportAction.setFileGeneratorName("DrawerGenerator");
		multiExportAction.setSelectedTemplate(selectedTemplate);
		String result = multiExportAction.exportZip();

		// Check
		Assert.assertEquals("file", result);
		Assert.assertEquals(byteArray.length, multiExportAction.getFileSize().intValue());
		Assert.assertEquals("multiExport.zip", multiExportAction.getFileName());
		Assert.assertNotNull(multiExportAction.getInputStream());
		Mockito.verify(exportBS).multiExport(Arrays.asList(1,2,3), user, fileGenerator, "DC", selectedTemplate);
	}
	
	
	@Test
	public void testExportZip_Success_UploadTemplate() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		TableBO table = TableBSTest.buildTable(user, "Gotham", "DC");
		TemplatedFileGenerator fileGenerator = new DrawerGenerator();
		String selectedTemplate = "Uploader un fichier";
		File templateFile = new File("");
		byte[] byteArray = new byte[]{0,1,2,3,4,5,7,8,9};
		Mockito.when(tableBS.getTableForGM(table.getId(), user)).thenReturn(table);
		Mockito.when(exportBS.getGenerator("DrawerGenerator")).thenReturn(fileGenerator);
		Mockito.when(exportBS.multiExport(Arrays.asList(1,2,3), user, fileGenerator, templateFile)).thenReturn(byteArray);
		
		// Execute
		multiExportAction.setTableId(table.getId());
		multiExportAction.setExportedPjList(Arrays.asList(1,2));
		multiExportAction.setExportedPnjList(Arrays.asList(3));
		multiExportAction.setFileGeneratorName("DrawerGenerator");
		multiExportAction.setSelectedTemplate(selectedTemplate);
		multiExportAction.setTemplateFile(templateFile);
		String result = multiExportAction.exportZip();

		// Check
		Assert.assertEquals("file", result);
		Assert.assertEquals(byteArray.length, multiExportAction.getFileSize().intValue());
		Assert.assertEquals("multiExport.zip", multiExportAction.getFileName());
		Assert.assertNotNull(multiExportAction.getInputStream());
		Mockito.verify(exportBS).multiExport(Arrays.asList(1,2,3), user, fileGenerator, templateFile);
	}
	
}
