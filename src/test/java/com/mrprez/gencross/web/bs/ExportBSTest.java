package com.mrprez.gencross.web.bs;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mrprez.gencross.export.FileGenerator;
import com.mrprez.gencross.export.TemplatedFileGenerator;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
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
