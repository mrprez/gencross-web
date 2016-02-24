package com.mrprez.gencross.web.test.bs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.mrprez.gencross.export.DrawerGenerator;
import com.mrprez.gencross.export.FileGenerator;
import com.mrprez.gencross.export.SimpleTxtGenerator;
import com.mrprez.gencross.export.TemplatedFileGenerator;
import com.mrprez.gencross.web.bo.MultiExportBO;
import com.mrprez.gencross.web.bo.MultiExportBO.MultiExportLine;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.ExportBS;
import com.mrprez.gencross.web.dao.face.IPersonnageDAO;
import com.mrprez.gencross.web.dao.face.ITemplateFileResource;

@RunWith(MockitoJUnitRunner.class)
public class ExportBSTest {
	
	@Mock
	private IPersonnageDAO personnageDao;
	
	@Mock
	private ITemplateFileResource templateFileResource;
	
	@InjectMocks
	private ExportBS exportBS;
	
	
	
	@Test
	public void testExport_withoutFileTemplate() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork();
		FileGenerator fileGenerator = buildMockedFileGenerator();
		
		// Execute
		byte[] byteArrayresult = exportBS.export(personnageWork, fileGenerator);
		
		// Check
		Assert.assertArrayEquals("template".getBytes(), byteArrayresult);
	}
	
	@Test
	public void testExport_withFileTemplate() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork();
		File file = new File("templateFile");
		TemplatedFileGenerator fileGenerator = buildMockedTemplatedFileGenerator();
		
		// Execute
		byte[] byteArrayresult = exportBS.export(personnageWork, fileGenerator, file);
		
		// Check
		Assert.assertArrayEquals("template".getBytes(), byteArrayresult);
		Mockito.verify(fileGenerator).setTemplate(file);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testExport_withStringTemplate() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork();
		File file = new File("templateFile");
		TemplatedFileGenerator fileGenerator = buildMockedTemplatedFileGenerator();
		Mockito.when(exportBS.getTemplateFileResource().getTemplate(Mockito.any(Class.class), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(file);
		
		// Execute
		byte[] byteArrayresult = exportBS.export(personnageWork, fileGenerator, "fileName");
		
		// Check
		Assert.assertArrayEquals("template".getBytes(), byteArrayresult);
		Mockito.verify(fileGenerator).setTemplate(file);
		Mockito.verify(fileGenerator).setTemplate(file);
	}
	
	@Test
	public void testGetGenerator_Success_WithPackageName() throws Exception{
		// Execute
		FileGenerator fileGenerator = exportBS.getGenerator("com.mrprez.gencross.export.TextGenerator");
		
		// Check
		Assert.assertTrue(fileGenerator instanceof com.mrprez.gencross.export.TextGenerator);
	}
	
	@Test
	public void testGetGenerator_Success_WithoutPackageName() throws Exception{
		// Execute
		FileGenerator fileGenerator = exportBS.getGenerator("TextGenerator");
		
		// Check
		Assert.assertTrue(fileGenerator instanceof com.mrprez.gencross.export.TextGenerator);
	}
	
	@Test
	public void testGetGenerator_Fail_WithPackageName() throws Exception{
		// Execute
		FileGenerator fileGenerator = exportBS.getGenerator("com.mrprez.gencross.export.TotoGenerator");
		
		// Check
		Assert.assertNull(fileGenerator);
	}
	
	@Test
	public void testGetGenerator_Fail_WithoutPackageName() throws Exception{
		// Execute
		FileGenerator fileGenerator = exportBS.getGenerator("TotoGenerator");
		
		// Check
		Assert.assertNull(fileGenerator);
	}
	
	@Test
	public void testMultiExport() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		UserBO otherUser = AuthentificationBSTest.buildUser("robin");
		PersonnageWorkBO personnageWork1 = PersonnageWorkBSTest.buildPersonnageWork();
		personnageWork1.setId(1);
		personnageWork1.setPlayer(user);
		personnageWork1.setName("Perso1");
		Mockito.when(exportBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork1);
		PersonnageWorkBO personnageWork2 = PersonnageWorkBSTest.buildPersonnageWork();
		personnageWork2.setId(2);
		personnageWork2.setGameMaster(user);
		personnageWork2.setName("Perso2");
		Mockito.when(exportBS.getPersonnageDAO().loadPersonnageWork(2)).thenReturn(personnageWork2);
		PersonnageWorkBO personnageWork3 = PersonnageWorkBSTest.buildPersonnageWork();
		personnageWork3.setId(3);
		personnageWork3.setPlayer(otherUser);
		personnageWork3.setName("Perso3");
		Mockito.when(exportBS.getPersonnageDAO().loadPersonnageWork(3)).thenReturn(personnageWork3);
		
		// Execute
		byte[] result = exportBS.multiExport(Arrays.asList(1, 2, 3), user, new SimpleTxtGenerator());
		
		// Check
		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(result));
		try{
			ZipEntry zipEntry1 = zis.getNextEntry();
			Assert.assertNotNull(zipEntry1);
			Assert.assertEquals("Perso1_1.txt", zipEntry1.getName());
			ByteArrayOutputStream os1 = new ByteArrayOutputStream();
			new SimpleTxtGenerator().write(personnageWork1.getPersonnage(), os1);
			byte[] zipBytes1 = new byte[os1.toByteArray().length];
			for(int i=0; i<zipBytes1.length; i++){
				int read = zis.read();
				Assert.assertTrue(read>=0);
				zipBytes1[i] = (byte) read;
			}
			Assert.assertTrue(zis.read()<0);
			Assert.assertArrayEquals((byte[])os1.toByteArray(), zipBytes1);
			
			ZipEntry zipEntry2 = zis.getNextEntry();
			Assert.assertNotNull(zipEntry2);
			Assert.assertEquals("Perso2_2.txt", zipEntry2.getName());
			ByteArrayOutputStream os2 = new ByteArrayOutputStream();
			new SimpleTxtGenerator().write(personnageWork1.getPersonnage(), os2);
			byte[] zipBytes2 = new byte[os2.toByteArray().length];
			for(int i=0; i<zipBytes2.length; i++){
				int read = zis.read();
				Assert.assertTrue(read>=0);
				zipBytes2[i] = (byte) read;
			}
			Assert.assertTrue(zis.read()<0);
			Assert.assertArrayEquals((byte[])os2.toByteArray(), zipBytes2);
			
			Assert.assertNull(zis.getNextEntry());
		}finally{
			zis.close();
		}
	}
	
	@Test
	public void testMultiExportInGrid() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		UserBO otherUser = AuthentificationBSTest.buildUser("robin");
		PersonnageWorkBO personnageWork1 = PersonnageWorkBSTest.buildPersonnageWork();
		personnageWork1.getPersonnage().addPropertyToMotherProperty(personnageWork1.getPersonnage().getProperty("Compétences#Connaissance spécialisée").getSubProperties().getOptions().get("Histoire"));
		personnageWork1.setId(1);
		personnageWork1.setPlayer(user);
		personnageWork1.setName("Perso1");
		Mockito.when(exportBS.getPersonnageDAO().loadPersonnageWork(1)).thenReturn(personnageWork1);
		PersonnageWorkBO personnageWork2 = PersonnageWorkBSTest.buildPersonnageWork();
		personnageWork2.getPersonnage().addPropertyToMotherProperty(personnageWork2.getPersonnage().getProperty("Compétences#Connaissance spécialisée").getSubProperties().getOptions().get("Droit"));
		personnageWork2.setId(2);
		personnageWork2.setGameMaster(user);
		personnageWork2.setName("Perso2");
		Mockito.when(exportBS.getPersonnageDAO().loadPersonnageWork(2)).thenReturn(personnageWork2);
		PersonnageWorkBO personnageWork3 = PersonnageWorkBSTest.buildPersonnageWork();
		personnageWork3.getPersonnage().addPropertyToMotherProperty(personnageWork3.getPersonnage().getProperty("Compétences#Connaissance spécialisée").getSubProperties().getOptions().get("Géographie"));
		personnageWork3.setId(3);
		personnageWork3.setPlayer(user);
		personnageWork3.setName("Perso3");
		Mockito.when(exportBS.getPersonnageDAO().loadPersonnageWork(3)).thenReturn(personnageWork3);
		PersonnageWorkBO personnageWork4 = PersonnageWorkBSTest.buildPersonnageWork();
		personnageWork4.setId(4);
		personnageWork4.setPlayer(otherUser);
		personnageWork4.setName("Perso4");
		Mockito.when(exportBS.getPersonnageDAO().loadPersonnageWork(4)).thenReturn(personnageWork4);
		
		// Execute
		MultiExportBO result = exportBS.multiExportInGrid(Arrays.asList(1, 2, 3, 4), user);
		
		// Check
		int count = 0;
		Assert.assertEquals(215, result.getLines().size());
		for(MultiExportLine line : result.getLines()){
			Assert.assertEquals(3, line.getValues().length);
			if(line.getTitle()==null){
				switch (count++) {
				case 0:
					Assert.assertArrayEquals(new String[]{"Histoire:1","Droit:1","Géographie:1"}, line.getValues());
					break;
				case 1:
					Assert.assertArrayEquals(new String[]{"Attribut 1:Erudition","Attribut 1:Erudition","Attribut 1:Erudition"}, line.getValues());
					break;
				case 2:
					Assert.assertArrayEquals(new String[]{"Attribut 2:Erudition","Attribut 2:Erudition","Attribut 2:Erudition"}, line.getValues());
					break;
				case 3:
					Assert.assertArrayEquals(new String[]{"Apprentissage:0","Apprentissage:0","Apprentissage:0"}, line.getValues());
					break;
				default:
					Assert.fail("Too many lines without title: "+StringUtils.join(line.getValues(),","));
					break;
				}
			}else{
				Assert.assertEquals(line.getValues()[0], line.getValues()[1]);
				Assert.assertEquals(line.getValues()[1], line.getValues()[2]);
			}
		}
	}
	
	@Test
	public void testGetTemplateFiles() throws Exception{
		// Prepare
		Map<Class<? extends TemplatedFileGenerator>, List<String>> templateFiles = new HashMap<Class<? extends TemplatedFileGenerator>, List<String>>();
		templateFiles.put(DrawerGenerator.class, Arrays.asList("Toto", "Tutu"));
		Mockito.when(exportBS.getTemplateFiles("Pavillon Noir")).thenReturn(templateFiles);
		
		// Execute
		Map<Class<? extends TemplatedFileGenerator>, List<String>> result = exportBS.getTemplateFiles("Pavillon Noir");
		
		// Check
		Assert.assertEquals(templateFiles, result);
	}
	
	
	private FileGenerator buildMockedFileGenerator(){
		Answer<Object> answer = new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				if(invocation.getMethod().getName().equals("write")){
					ByteArrayOutputStream baos = (ByteArrayOutputStream) invocation.getArguments()[1];
					baos.write("template".getBytes());
				}
				return null;
			}
		};
		return Mockito.mock(FileGenerator.class, answer);
	}
	
	
	private TemplatedFileGenerator buildMockedTemplatedFileGenerator(){
		Answer<Object> answer = new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				if(invocation.getMethod().getName().equals("write")){
					ByteArrayOutputStream baos = (ByteArrayOutputStream) invocation.getArguments()[1];
					baos.write("template".getBytes());
				}
				return null;
			}
		};
		return Mockito.mock(TemplatedFileGenerator.class, answer);
	}
	
	
}
