package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.DownloadAction;
import com.mrprez.gencross.web.bs.face.IGencrossUiPackagerBS;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class DownloadActionTest extends AbstractActionTest {

	@Mock
	private IGencrossUiPackagerBS gencrossUiPackagerBS;

	@InjectMocks
	private DownloadAction downloadAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		downloadAction.setFileName("string_1");

		// Execute
		String result = downloadAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", downloadAction.getFileName());
		Assert.assertEquals("failTest", downloadAction.getGenCrossUI());
		Assert.assertEquals("failTest", downloadAction.getInputStream());
	}
}
