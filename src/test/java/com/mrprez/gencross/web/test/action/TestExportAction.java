package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.ExportAction;
import com.mrprez.gencross.web.bs.face.IExportBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;

@RunWith(MockitoJUnitRunner.class)
public class TestExportAction {

	@Mock
	private IExportBS exportBS;

	@Mock
	private IPersonnageBS personnageBS;

	@InjectMocks
	private ExportAction exportAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		exportAction.setFileGeneratorName("string_1");
		exportAction.setFileName("string_2");
		exportAction.setFileSize(3);
		exportAction.setSelectedTemplate("string_4");
		exportAction.setPersonnageId(5);

		// Execute
		exportAction.execute();

		// Check
		Assert.assertEquals("failTest", exportAction.getFileName());
		Assert.assertEquals("failTest", exportAction.getFileGeneratorName());
		Assert.assertEquals("failTest", exportAction.getTemplateFile());
		Assert.assertEquals("failTest", exportAction.getFileSize());
		Assert.assertEquals("failTest", exportAction.getPersonnageId());
		Assert.assertEquals("failTest", exportAction.getInputStream());
		Assert.assertEquals("failTest", exportAction.getGeneratorList());
		Assert.assertEquals("failTest", exportAction.getTemplateFiles());
		Assert.assertEquals("failTest", exportAction.getSelectedTemplate());
	}


	@Test
	public void testExport() throws Exception {
		// Prepare
		exportAction.setFileGeneratorName("string_1");
		exportAction.setFileName("string_2");
		exportAction.setFileSize(3);
		exportAction.setSelectedTemplate("string_4");
		exportAction.setPersonnageId(5);

		// Execute
		exportAction.export();

		// Check
		Assert.assertEquals("failTest", exportAction.getFileName());
		Assert.assertEquals("failTest", exportAction.getFileGeneratorName());
		Assert.assertEquals("failTest", exportAction.getTemplateFile());
		Assert.assertEquals("failTest", exportAction.getFileSize());
		Assert.assertEquals("failTest", exportAction.getPersonnageId());
		Assert.assertEquals("failTest", exportAction.getInputStream());
		Assert.assertEquals("failTest", exportAction.getGeneratorList());
		Assert.assertEquals("failTest", exportAction.getTemplateFiles());
		Assert.assertEquals("failTest", exportAction.getSelectedTemplate());
	}
}
