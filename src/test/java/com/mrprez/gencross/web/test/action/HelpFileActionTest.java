package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.HelpFileAction;

@RunWith(MockitoJUnitRunner.class)
public class HelpFileActionTest extends AbstractActionTest {

	@InjectMocks
	private HelpFileAction helpFileAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		helpFileAction.setPersonnageWorkId(1);

		// Execute
		String result = helpFileAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", helpFileAction.getContentDisposition());
		Assert.assertEquals("failTest", helpFileAction.getContentLength());
		Assert.assertEquals("failTest", helpFileAction.getHelpFileInputStream());
		Assert.assertEquals("failTest", helpFileAction.getPersonnageWorkId());
	}
}
