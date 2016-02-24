package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.MultiExportAction;
import com.mrprez.gencross.web.bs.face.IExportBS;
import com.mrprez.gencross.web.bs.face.ITableBS;

@RunWith(MockitoJUnitRunner.class)
public class TestMultiExportAction {

	@Mock
	private IExportBS exportBS;

	@Mock
	private ITableBS tableBS;

	@InjectMocks
	private MultiExportAction multiExportAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		multiExportAction.setFileGeneratorName("string_1");
		multiExportAction.setFileName("string_2");
		multiExportAction.setTableId(3);
		multiExportAction.setFileSize(4);
		multiExportAction.setSelectedTemplate("string_5");

		// Execute
		multiExportAction.execute();

		// Check
		Assert.assertEquals("failTest", multiExportAction.getExportedPjList());
		Assert.assertEquals("failTest", multiExportAction.getFileSize());
		Assert.assertEquals("failTest", multiExportAction.getPjList());
		Assert.assertEquals("failTest", multiExportAction.getTable());
		Assert.assertEquals("failTest", multiExportAction.getTableId());
		Assert.assertEquals("failTest", multiExportAction.getSelectedTemplate());
		Assert.assertEquals("failTest", multiExportAction.getExport());
		Assert.assertEquals("failTest", multiExportAction.getFileName());
		Assert.assertEquals("failTest", multiExportAction.getExportedPnjList());
		Assert.assertEquals("failTest", multiExportAction.getTemplateFile());
		Assert.assertEquals("failTest", multiExportAction.getFileGeneratorName());
		Assert.assertEquals("failTest", multiExportAction.getInputStream());
		Assert.assertEquals("failTest", multiExportAction.getPnjList());
		Assert.assertEquals("failTest", multiExportAction.getTemplateFiles());
		Assert.assertEquals("failTest", multiExportAction.getGeneratorList());
	}


	@Test
	public void testExport() throws Exception {
		// Prepare
		multiExportAction.setFileGeneratorName("string_1");
		multiExportAction.setFileName("string_2");
		multiExportAction.setTableId(3);
		multiExportAction.setFileSize(4);
		multiExportAction.setSelectedTemplate("string_5");

		// Execute
		multiExportAction.export();

		// Check
		Assert.assertEquals("failTest", multiExportAction.getExportedPjList());
		Assert.assertEquals("failTest", multiExportAction.getFileSize());
		Assert.assertEquals("failTest", multiExportAction.getPjList());
		Assert.assertEquals("failTest", multiExportAction.getTable());
		Assert.assertEquals("failTest", multiExportAction.getTableId());
		Assert.assertEquals("failTest", multiExportAction.getSelectedTemplate());
		Assert.assertEquals("failTest", multiExportAction.getExport());
		Assert.assertEquals("failTest", multiExportAction.getFileName());
		Assert.assertEquals("failTest", multiExportAction.getExportedPnjList());
		Assert.assertEquals("failTest", multiExportAction.getTemplateFile());
		Assert.assertEquals("failTest", multiExportAction.getFileGeneratorName());
		Assert.assertEquals("failTest", multiExportAction.getInputStream());
		Assert.assertEquals("failTest", multiExportAction.getPnjList());
		Assert.assertEquals("failTest", multiExportAction.getTemplateFiles());
		Assert.assertEquals("failTest", multiExportAction.getGeneratorList());
	}


	@Test
	public void testExportCsv() throws Exception {
		// Prepare
		multiExportAction.setFileGeneratorName("string_1");
		multiExportAction.setFileName("string_2");
		multiExportAction.setTableId(3);
		multiExportAction.setFileSize(4);
		multiExportAction.setSelectedTemplate("string_5");

		// Execute
		multiExportAction.exportCsv();

		// Check
		Assert.assertEquals("failTest", multiExportAction.getExportedPjList());
		Assert.assertEquals("failTest", multiExportAction.getFileSize());
		Assert.assertEquals("failTest", multiExportAction.getPjList());
		Assert.assertEquals("failTest", multiExportAction.getTable());
		Assert.assertEquals("failTest", multiExportAction.getTableId());
		Assert.assertEquals("failTest", multiExportAction.getSelectedTemplate());
		Assert.assertEquals("failTest", multiExportAction.getExport());
		Assert.assertEquals("failTest", multiExportAction.getFileName());
		Assert.assertEquals("failTest", multiExportAction.getExportedPnjList());
		Assert.assertEquals("failTest", multiExportAction.getTemplateFile());
		Assert.assertEquals("failTest", multiExportAction.getFileGeneratorName());
		Assert.assertEquals("failTest", multiExportAction.getInputStream());
		Assert.assertEquals("failTest", multiExportAction.getPnjList());
		Assert.assertEquals("failTest", multiExportAction.getTemplateFiles());
		Assert.assertEquals("failTest", multiExportAction.getGeneratorList());
	}


	@Test
	public void testExportZip() throws Exception {
		// Prepare
		multiExportAction.setFileGeneratorName("string_1");
		multiExportAction.setFileName("string_2");
		multiExportAction.setTableId(3);
		multiExportAction.setFileSize(4);
		multiExportAction.setSelectedTemplate("string_5");

		// Execute
		multiExportAction.exportZip();

		// Check
		Assert.assertEquals("failTest", multiExportAction.getExportedPjList());
		Assert.assertEquals("failTest", multiExportAction.getFileSize());
		Assert.assertEquals("failTest", multiExportAction.getPjList());
		Assert.assertEquals("failTest", multiExportAction.getTable());
		Assert.assertEquals("failTest", multiExportAction.getTableId());
		Assert.assertEquals("failTest", multiExportAction.getSelectedTemplate());
		Assert.assertEquals("failTest", multiExportAction.getExport());
		Assert.assertEquals("failTest", multiExportAction.getFileName());
		Assert.assertEquals("failTest", multiExportAction.getExportedPnjList());
		Assert.assertEquals("failTest", multiExportAction.getTemplateFile());
		Assert.assertEquals("failTest", multiExportAction.getFileGeneratorName());
		Assert.assertEquals("failTest", multiExportAction.getInputStream());
		Assert.assertEquals("failTest", multiExportAction.getPnjList());
		Assert.assertEquals("failTest", multiExportAction.getTemplateFiles());
		Assert.assertEquals("failTest", multiExportAction.getGeneratorList());
	}
}
