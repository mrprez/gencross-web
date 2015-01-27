package com.mrprez.gencross.web.bs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mrprez.gencross.export.DrawerGenerator;
import com.mrprez.gencross.export.FileGenerator;
import com.mrprez.gencross.export.SimpleTxtGenerator;
import com.mrprez.gencross.export.TemplatedFileGenerator;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.dao.face.IPersonnageDAO;
import com.mrprez.gencross.web.dao.face.ITemplateFileResource;


public class ExportBSTest {
	
	
	@Test
	public void testExport_withoutFileTemplate() throws Exception{
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork();
		FileGenerator fileGenerator = buildMockedFileGenerator();
		ExportBS exportBS = buildExportBS();
		
		byte[] byteArrayresult = exportBS.export(personnageWork, fileGenerator);
		
		Assert.assertArrayEquals("template".getBytes(), byteArrayresult);
	}
	
	@Test
	public void testExport_withFileTemplate() throws Exception{
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork();
		File file = new File("templateFile");
		TemplatedFileGenerator fileGenerator = buildMockedTemplatedFileGenerator();
		ExportBS exportBS = buildExportBS();
		
		byte[] byteArrayresult = exportBS.export(personnageWork, fileGenerator, file);
		
		Assert.assertArrayEquals("template".getBytes(), byteArrayresult);
		Mockito.verify(fileGenerator).setTemplate(file);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testExport_withStringTemplate() throws Exception{
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork();
		File file = new File("templateFile");
		TemplatedFileGenerator fileGenerator = buildMockedTemplatedFileGenerator();
		ExportBS exportBS = buildExportBS();
		Mockito.when(exportBS.getTemplateFileResource().getTemplate(Mockito.any(Class.class), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(file);
		
		byte[] byteArrayresult = exportBS.export(personnageWork, fileGenerator, "fileName");
		
		Assert.assertArrayEquals("template".getBytes(), byteArrayresult);
		Mockito.verify(fileGenerator).setTemplate(file);
		Mockito.verify(fileGenerator).setTemplate(file);
	}
	
	@Test
	public void testGetGenerator_Success_WithPackageName() throws Exception{
		ExportBS exportBS = buildExportBS();
		
		FileGenerator fileGenerator = exportBS.getGenerator("com.mrprez.gencross.export.TextGenerator");
		
		Assert.assertTrue(fileGenerator instanceof com.mrprez.gencross.export.TextGenerator);
	}
	
	@Test
	public void testGetGenerator_Success_WithoutPackageName() throws Exception{
		ExportBS exportBS = buildExportBS();
		
		FileGenerator fileGenerator = exportBS.getGenerator("TextGenerator");
		
		Assert.assertTrue(fileGenerator instanceof com.mrprez.gencross.export.TextGenerator);
	}
	
	@Test
	public void testGetGenerator_Fail_WithPackageName() throws Exception{
		ExportBS exportBS = buildExportBS();
		
		FileGenerator fileGenerator = exportBS.getGenerator("com.mrprez.gencross.export.TotoGenerator");
		
		Assert.assertNull(fileGenerator);
	}
	
	@Test
	public void testGetGenerator_Fail_WithoutPackageName() throws Exception{
		ExportBS exportBS = buildExportBS();
		
		FileGenerator fileGenerator = exportBS.getGenerator("TotoGenerator");
		
		Assert.assertNull(fileGenerator);
	}
	
	@Test
	public void testMultiExport() throws Exception{
		ExportBS exportBS = buildExportBS();
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
		
		byte[] result = exportBS.multiExport(Arrays.asList(1, 2, 3), user, new SimpleTxtGenerator());
		
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
		ExportBS exportBS = buildExportBS();
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
		
		List<String[]> result = exportBS.multiExportInGrid(Arrays.asList(1, 2, 3), user);
		
		for(String[] stringTab : result){
			Assert.assertEquals(3, stringTab.length);
			Assert.assertEquals(stringTab[1], stringTab[2]);
			Assert.assertTrue(stringTab[0].length() > 0);
		}
	}
	
	@Test
	public void testGetTemplateFiles() throws Exception{
		ExportBS exportBS = buildExportBS();
		Map<Class<? extends TemplatedFileGenerator>, List<String>> templateFiles = new HashMap<Class<? extends TemplatedFileGenerator>, List<String>>();
		templateFiles.put(DrawerGenerator.class, Arrays.asList("Toto", "Tutu"));
		Mockito.when(exportBS.getTemplateFiles("Pavillon Noir")).thenReturn(templateFiles);
		
		Map<Class<? extends TemplatedFileGenerator>, List<String>> result = exportBS.getTemplateFiles("Pavillon Noir");
		
		Assert.assertEquals(templateFiles, result);
	}
	
	
	private ExportBS buildExportBS(){
		ExportBS exportBS = new ExportBS();
		exportBS.setPersonnageDAO(Mockito.mock(IPersonnageDAO.class));
		exportBS.setTemplateFileResource(Mockito.mock(ITemplateFileResource.class));
		return exportBS;
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
