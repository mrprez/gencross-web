package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.ExportAction;
import com.mrprez.gencross.web.bs.face.IExportBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;

@Ignore
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
		exportAction.setFileGeneratorName("string_1");
		exportAction.setFileName("string_2");
		exportAction.setFileSize(3);
		exportAction.setSelectedTemplate("string_4");
		exportAction.setPersonnageId(5);

		// Execute
		String result = exportAction.execute();

		// Check
		Assert.assertEquals("input", result);
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
		String result = exportAction.export();

		// Check
		Assert.assertEquals("input", result);
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
