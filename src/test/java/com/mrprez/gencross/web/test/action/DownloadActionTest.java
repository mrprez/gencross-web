package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.DownloadAction;
import com.mrprez.gencross.web.bs.face.IGencrossUiPackagerBS;

@RunWith(MockitoJUnitRunner.class)
public class DownloadActionTest extends AbstractActionTest {

	@Mock
	private IGencrossUiPackagerBS gencrossUiPackagerBS;

	@InjectMocks
	private DownloadAction downloadAction;



	@Test
	public void testExecute() throws Exception {
		// Execute
		String result = downloadAction.execute();

		// Check
		Assert.assertEquals("input", result);
	}
	
	
	@Test
	public void testGetGenCrossUI() throws Exception {
		// Prepare
		byte[] bytes = new byte[]{0,1,2,3,4,5,6,7,8,9};
		Mockito.when(gencrossUiPackagerBS.buildGencrossUiPackage()).thenReturn(bytes);
		
		// Execute
		String result = downloadAction.getGenCrossUI();

		// Check
		Assert.assertEquals("success", result);
		Assert.assertNotNull(downloadAction.getInputStream());
		for(byte b : bytes){
			Assert.assertEquals(b, (byte)downloadAction.getInputStream().read());
		}
		Assert.assertTrue(downloadAction.getInputStream().read() < 0);
		Assert.assertEquals("GencrossUI.zip", downloadAction.getFileName());
	}
}
